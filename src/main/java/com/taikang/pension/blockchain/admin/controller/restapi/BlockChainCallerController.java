package com.taikang.pension.blockchain.admin.controller.restapi;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taikang.pension.blockchain.admin.util.RestResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/api/v1/blockchain")
public class BlockChainCallerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockChainCallerController.class);
    @Autowired
    protected RestTemplate restTemplate;
    private final static String ENDPOINT_URL ="http://localhost:3000/";

    @PostMapping("/putPubkey")
    @ResponseBody
    public RestResponse putPubkey (ServletRequest request) {
        String url = ENDPOINT_URL+"/PutPubkey";
        String publicKey = request.getContentType();
        JSONObject data  = new JSONObject();
        data.put("orgname","T11");
        data.put("pubkey ",publicKey);

        String requestJson = data.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {

            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            RestResponse answer = restTemplate.postForObject(url, entity, RestResponse.class);
            return answer;
        } catch (Exception e) {
            String errMessage ="Error when putting public key to blockchain";
            LOGGER.error(errMessage, e);
            return RestResponse.failure(errMessage+"：" + e.getMessage());
        }
    }
    @PostMapping("/putInfo")
    @ResponseBody
    public RestResponse putInfo(ServletRequest request) {
        String url = ENDPOINT_URL+"/PutInfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson =  constructInfoBody();
        try {
            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            RestResponse answer = restTemplate.postForObject(url, entity, RestResponse.class);
            return answer;
        } catch (Exception e) {
            String errMessage ="Error when putting public person info to blockchain";
            LOGGER.error(errMessage, e);
            return RestResponse.failure(errMessage+"：" + e.getMessage());
        }
    }

    private String constructInfoBody() {
        return null;
    }

    @PostMapping("/queryPubKey")
    @ResponseBody
    public RestResponse queryPubKey(ServletRequest request) {

        String url = ENDPOINT_URL+"/QueryInfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson =  constructQueryInfoRequestBody();
        try {
            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            RestResponse answer = restTemplate.postForObject(url, entity, RestResponse.class);
            return answer;
        } catch (Exception e) {
            String errMessage ="Error when putting  person info to blockchain";
            LOGGER.error(errMessage, e);
            return RestResponse.failure(errMessage+"：" + e.getMessage());
        }
    }

    private String constructQueryInfoRequestBody() {

        JSONObject data  = new JSONObject();
        data.put("orgname","T11");
        data.put("pubkey ","");
        return data.toString();
    }

    @PostMapping("/deleteInfo")
    @ResponseBody
    public RestResponse deleteInfo(ServletRequest request) {

        String url = ENDPOINT_URL+"/DeleteInfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson =  constructInfoBody();
        try {
            HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
            RestResponse answer = restTemplate.postForObject(url, entity, RestResponse.class);
            return answer;
        } catch (Exception e) {
            String errMessage ="Error when deleting  person info from blockchain";
            LOGGER.error(errMessage, e);
            return RestResponse.failure(errMessage+"：" + e.getMessage());
        }
    }




}
