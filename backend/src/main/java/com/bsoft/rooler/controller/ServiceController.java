package com.bsoft.rooler.controller;

import com.alibaba.fastjson.JSON;
import com.bsoft.rooler.zookeeper.ZookeeperMain;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.rooler.controller
 * @Description:
 * @date : 2021/11/27 23:15
 */
@RestController("")
public class ServiceController {

    @Autowired
    ZookeeperMain zookeeperMain;

    @GetMapping("/getServices")
    public Map<String, Object>  getServices(){
        Map<String, Object> servcieList = zookeeperMain.getServcieList();
        return servcieList;
    }


    @PostMapping("/linkService")
    public Boolean  setServices(@RequestBody  HashMap<String, Object> map){

        String httpPath;
        String json;

        try {
            LinkedHashMap o1 = (LinkedHashMap<String,Object>) map.get("source");
            LinkedHashMap o2 = (LinkedHashMap<String,Object>) map.get("target");
            httpPath = (String) o1.get("httpPath");
            json = JSON.toJSONString(o2);
        }catch(Exception e){
            return false;
        };

        try {
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url(httpPath+"/*.balance")
//                    .addHeader("X-Service-Id",domain+".balanceRpc")
//                    .addHeader("X-Service-Method","changeBalance")
                    .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json)).build();
            System.out.println(json);
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                System.out.println(response);
                if(200 != response.code() ){
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }finally {

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
