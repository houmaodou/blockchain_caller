package com.taikang.pension.blockchain.admin.util.quartz.task;



import com.alibaba.fastjson.JSONObject;
import com.taikang.pension.blockchain.admin.entity.TransferRecord;
import com.taikang.pension.blockchain.admin.service.DictService;
import com.taikang.pension.blockchain.admin.service.TransferRecordService;
import com.taikang.pension.blockchain.admin.util.Constants;
import com.taikang.pension.blockchain.admin.util.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by houhx02 on 2018/1/26.
 * 系统定时任务
 */
@Component("systemTask")
public class SystemTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemTask.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private TransferRecordService transferRecordService;
    @Autowired
    private DictService dictService;

    @Autowired
    protected RestTemplate restTemplate;
    /**
     *  定时通过区块链接口获取转入数据
     *
     */
    public void getAndSaveTransferInData() {

        String url = Constants.ENDPOINT_URL+"/QueryInfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson =  buildQueryInfoRequestBody();
        try {
            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            RestResponse answer = restTemplate.postForObject(url, entity, RestResponse.class);
            if((Boolean) answer.get("success")){
                String chainData = answer.get("message").toString();
                LOGGER.info("Get chain data:"+chainData);
                JSONObject  dataJson = JSONObject.parseObject(chainData);
                if(dataJson==null){
                    return;
                }
                JSONObject summaryJson = dataJson.getJSONObject("Summary");
                if(summaryJson==null){
                    return;
                }
                TransferRecord record =  buildTransferRecordFromChainData(dataJson);
                transferRecordService.insertRecordAndDetail(record,chainData);
            }
        } catch (Exception e) {
            String errMessage ="Error when getting  person info from blockchain";
            LOGGER.error(errMessage, e);
        }

    }

    private TransferRecord buildTransferRecordFromChainData(JSONObject chainData) {
        JSONObject summaryJson = chainData.getJSONObject("Summary");
        String senderOrgCode = summaryJson.getString("Sendcode");
        String senderOrgName = dictService.getDictByValueAndType(senderOrgCode,Constants.DICT_TYPE_ORG).getLabel();
        TransferRecord record = new TransferRecord();
        record.setTransferOrgCode(senderOrgCode);
        record.setTransferOrgName(senderOrgName);
        record.setDirection(1);// 转入
        return  record;

    }

    private String buildQueryInfoRequestBody() {
        JSONObject obj = new JSONObject();
        obj.put("orgname",Constants.TAIKANG_ORG_CODE);
        obj.put("privkey",Constants.PRIVATE_KEY);
        return obj.toJSONString();

    }

}
