package com.taikang.pension.blockchain.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taikang.pension.blockchain.admin.service.DictService;
import com.taikang.pension.blockchain.admin.util.Constants;
import com.xiaoleilu.hutool.date.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taikang.pension.blockchain.admin.entity.TransferRecord;
import com.taikang.pension.blockchain.admin.service.TransferRecordService;
import com.baomidou.mybatisplus.plugins.Page;
import com.taikang.pension.blockchain.admin.util.LayerData;
import com.taikang.pension.blockchain.admin.util.RestResponse;
import com.taikang.pension.blockchain.admin.annotation.SysLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 转移记录  前端控制器
 * </p>
 *
 * @author houhx02
 * @since 2018-12-01
 */
@Controller
@RequestMapping("/admin/transferRecord")
public class TransferRecordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferRecordController.class);
    @Autowired
    private TransferRecordService transferRecordService;
    @Autowired
    private DictService dictService;
    @Value("${file.uploadFolder}")
    private String uploadFolder;

    private static final String SUFFIX_2003 = ".xls";
    private static final String SUFFIX_2007 = ".xlsx";


    @Autowired
    protected RestTemplate restTemplate;

    @GetMapping("list")
    @SysLog("跳转转移记录列表")
    public String list(){
        return "/admin/transferRecord/list";
    }

    @PostMapping("list")
    @ResponseBody
    @SysLog("请求转移记录列表数据")
    public LayerData<TransferRecord> list(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                      @RequestParam(value = "limit",defaultValue = "10")Integer limit,
                                      ServletRequest request){
        Map map = WebUtils.getParametersStartingWith(request, "s_");
        LayerData<TransferRecord> layerData = new LayerData<>();
        EntityWrapper<TransferRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag",false);
        if(!map.isEmpty()){
            String transferOrgCode = (String) map.get("transferOrgCode");
            if(StringUtils.isNotBlank(transferOrgCode)) {
                wrapper.eq("transfer_org_code",transferOrgCode);
            }else{
                map.remove("transferOrgCode");
            }

            String direction = (String) map.get("direction");
            if(StringUtils.isNotBlank(direction)) {
                wrapper.eq("direction",direction);
            }else{
                map.remove("direction");
            }

        }
        Page<TransferRecord> pageData = transferRecordService.selectPage(new Page<>(page,limit),wrapper);
        layerData.setData(pageData.getRecords());
        layerData.setCount(pageData.getTotal());
        return layerData;
    }

    @GetMapping("add")
    @SysLog("跳转新增转移记录页面")
    public String add(){
        return "/admin/transferRecord/add";
    }

    @PostMapping("add")
    @SysLog("保存新增转移记录数据")
    @ResponseBody
    public RestResponse add(TransferRecord transferRecord,HttpServletRequest request,@RequestParam(required = true,value = "file")MultipartFile file){

        if(StringUtils.isBlank(transferRecord.getTransferOrgCode())){
            return RestResponse.failure("转移机构代码不能为空");
        }
        transferRecord.setTransferOrgName(dictService.getDictByValueAndType(transferRecord.getTransferOrgCode(),Constants.DICT_TYPE_ORG).getLabel());
        if(transferRecord.getDirection() == null){
            return RestResponse.failure("转移方向不能为空");
        }
        // 文件名
        String fileName = file.getOriginalFilename();
        // 文件后缀
        if(fileName.lastIndexOf(".")==-1){
            return RestResponse.failure("文件必须为Excel格式的文件");
        }
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if(StringUtils.isBlank(suffixName)||(!suffixName.endsWith("xls")&&!suffixName.endsWith("xlsx"))){
            return RestResponse.failure("文件必须为Excel格式的文件");
        }
        // 重新生成唯一文件名，用于存储数据库
        String newFileName = UUID.randomUUID().toString()+suffixName;
        LOGGER.info("新的文件名： " + newFileName);
        transferRecord.setFilePath(newFileName);
        //创建文件
        File dest = new File(uploadFolder + newFileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            LOGGER.error("上传文件出错："+e.getMessage());
            return RestResponse.failure("上传文件出错!");
        }
        //Parse Excel
        String chainData = "";
        try {
            JSONObject obj = parseExcel2JSON(dest, transferRecord);
            chainData = obj.toJSONString();
            LOGGER.info("chainData:"+chainData);
        }catch(Exception e){
            LOGGER.error("解析文件出错："+e.getMessage());
            return RestResponse.failure("解析文件出错!");
        }
        // 数据上链
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            String url = Constants.ENDPOINT_URL+"/PutInfo";
            HttpEntity<String> entity = new HttpEntity<String>(chainData,headers);
            RestResponse answer = restTemplate.postForObject(url, entity, RestResponse.class);
            if((Boolean)answer.get("success")==false){
                 throw new Exception(answer.get("message").toString());
            }
        } catch (Exception e) {
            String errMessage ="Error when putting public person info into blockchain";
            LOGGER.error(errMessage, e);
            return RestResponse.failure("数据上链失败:"+e.getMessage());
        }
        transferRecordService.insertRecordAndDetail(transferRecord,chainData);
        return RestResponse.success();
    }

    private JSONObject parseExcel2JSON(File file,TransferRecord transferRecord) {

        //获取文件的名字
        String filename = file.getName();
        Workbook workbook = null;
        try {
            if (filename.endsWith(SUFFIX_2003)) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else if (filename.endsWith(SUFFIX_2007)) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }
        } catch (Exception e) {
            LOGGER.error("Invalid excel format:"+e.getMessage());
            throw new RuntimeException("Excel格式错误");
        }

        //获取一个sheet也就是一个工作簿
            Sheet sheet = workbook.getSheetAt(0);
            if(sheet==null||sheet.getLastRowNum()<9){
                throw new RuntimeException("Excel数据为空");
            }
            int lastRowNum = sheet.getLastRowNum();
            //从第6行开始
            int count = 0;
            double transamtSum = 0;
            int j = 5;
        JSONArray infoObj = new JSONArray();
        while(j<lastRowNum){
                Row row = sheet.getRow(j);
                if(row.getCell(0).toString().equals("其他说明事项:")){
                    break;
                }
                count ++;
                JSONObject rowObj = new JSONObject();
                rowObj.put("SerioNo",row.getCell(0).toString());
                rowObj.put("Trustee",row.getCell(0).toString());
                rowObj.put("Planname",row.getCell(0).toString());
                rowObj.put("Compplanname",row.getCell(0).toString());
                rowObj.put("Companyname",row.getCell(0).toString());
                rowObj.put("Staffid",row.getCell(0).toString());
                rowObj.put("Staffname",row.getCell(0).toString());
                rowObj.put("Idtype",row.getCell(0).toString());
                rowObj.put("Idno",row.getCell(0).toString());
                rowObj.put("Gender",row.getCell(0).toString());
                rowObj.put("BirthDate",row.getCell(0).toString());
                rowObj.put("Transfertype",row.getCell(0).toString());
                rowObj.put("Transamt",row.getCell(0).toString());
                rowObj.put("TOTrustee",row.getCell(0).toString());
                rowObj.put("Toplanname",row.getCell(0).toString());
                rowObj.put("Tocompplanname",row.getCell(0).toString());
                rowObj.put("Tocompanyname",row.getCell(0).toString());
                rowObj.put("Tobankaccname",row.getCell(0).toString());
                rowObj.put("TobankName",row.getCell(0).toString());
                rowObj.put("Tobankaccno",row.getCell(0).toString());
                rowObj.put("Tobankprov",row.getCell(0).toString());
                rowObj.put("Tobankcity",row.getCell(0).toString());
                rowObj.put("Tobankbranchname",row.getCell(0).toString());
                rowObj.put("Staffbefortaxsum",row.getCell(0).toString());
                rowObj.put("Staffaftertaxsum",row.getCell(0).toString());
                rowObj.put("Staffbefortaxprin",row.getCell(0).toString());
                rowObj.put("Staffaftertaxprin",row.getCell(0).toString());
                rowObj.put("TransferDate",row.getCell(0).toString());
                rowObj.put("PhoneNo",row.getCell(0).toString());
                rowObj.put("Email",row.getCell(0).toString());
                rowObj.put("Postcode",row.getCell(0).toString());
                rowObj.put("Address",row.getCell(0).toString());
                rowObj.put("AccountHolderType",row.getCell(0).toString());
                rowObj.put("LastNameCN",row.getCell(0).toString());
                rowObj.put("FirstNameCN",row.getCell(0).toString());
                rowObj.put("LastName",row.getCell(0).toString());
                rowObj.put("MiddleName",row.getCell(0).toString());
                rowObj.put("FirstName",row.getCell(0).toString());
                rowObj.put("CountryCN",row.getCell(0).toString());
                rowObj.put("ProvinceCN",row.getCell(0).toString());
                rowObj.put("CityCN",row.getCell(0).toString());
                rowObj.put("AddressFreeCN",row.getCell(0).toString());
                rowObj.put("CountryEN",row.getCell(0).toString());
                rowObj.put("CityEN",row.getCell(0).toString());
                rowObj.put("AddressFreeEN",row.getCell(0).toString());
                rowObj.put("BirthDate2",row.getCell(0).toString());
                rowObj.put("PlaceOfBirth",row.getCell(0).toString());
                rowObj.put("ResCountryCode1",row.getCell(0).toString());
                rowObj.put("TIN1",row.getCell(0).toString());
                rowObj.put("Explanation",row.getCell(0).toString());
                rowObj.put("Explanation2",row.getCell(0).toString());
                rowObj.put("BankAccname",row.getCell(0).toString());
                rowObj.put("BankName",row.getCell(0).toString());
                rowObj.put("BankAccno",row.getCell(0).toString());
                rowObj.put("BankProv",row.getCell(0).toString());
                rowObj.put("BankCity",row.getCell(0).toString());
                rowObj.put("BankBranchName",row.getCell(0).toString());
            infoObj.add(rowObj);
            j++;
            }


        JSONObject summaryObj = new JSONObject();
        summaryObj.put("BatchNo",getBatchNo());
        summaryObj.put("Transtype","02");
        summaryObj.put("Sendcode","T11");
        summaryObj.put("Receivecode",transferRecord.getTransferOrgCode());
        summaryObj.put("TranCount",count);
        summaryObj.put("TransamtSum",transamtSum);

        JSONObject retObj = new JSONObject();
        retObj.put("From",Constants.TAIKANG_ORG_CODE);
        retObj.put("To",transferRecord.getTransferOrgCode());
        retObj.put("Summary",summaryObj);
        retObj.put("Info",infoObj);

        return retObj;
    }

    private String getBatchNo(){
        return "CJST20181027000123";
    }

    @GetMapping("edit")
    @SysLog("跳转编辑转移记录页面")
    public String edit(Long id,Model model){
        TransferRecord transferRecord = transferRecordService.selectById(id);
        model.addAttribute("transferRecord",transferRecord);
        return "/admin/transferRecord/edit";
    }

    @PostMapping("edit")
    @ResponseBody
    @SysLog("保存编辑转移记录数据")
    public RestResponse edit(TransferRecord transferRecord){
        if(null == transferRecord.getId() || 0 == transferRecord.getId()){
            return RestResponse.failure("ID不能为空");
        }

        if(StringUtils.isBlank(transferRecord.getTransferOrgCode())){
            return RestResponse.failure("转移机构代码不能为空");
        }
        if(StringUtils.isBlank(transferRecord.getTransferOrgName())){
            return RestResponse.failure("机构名称不能为空");
        }
        if(transferRecord.getDirection() == null){
            return RestResponse.failure("转移方向不能为空");
        }
        if(StringUtils.isBlank(transferRecord.getFilePath())){
            return RestResponse.failure("Excel 文件不能为空");
        }


        transferRecordService.updateById(transferRecord);
        return RestResponse.success();
    }

    @PostMapping("delete")
    @ResponseBody
    @SysLog("删除转移记录数据")
    public RestResponse delete(@RequestParam(value = "id",required = false)Long id){
        if(null == id || 0 == id){
            return RestResponse.failure("ID不能为空");
        }
        TransferRecord transferRecord = transferRecordService.selectById(id);
        transferRecord.setDelFlag(true);
        transferRecordService.updateById(transferRecord);
        return RestResponse.success();
    }

}