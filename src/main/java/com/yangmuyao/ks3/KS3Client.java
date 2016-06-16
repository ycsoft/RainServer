package com.yangmuyao.ks3;

/**
 * Created by fengyuyangchen on 16/6/12.
 */

import com.ksyun.ks3.dto.GetObjectResult;
import com.ksyun.ks3.http.HttpClientConfig;
import com.ksyun.ks3.service.Ks3;
import com.ksyun.ks3.service.Ks3Client;
import com.ksyun.ks3.service.Ks3ClientConfig;
import com.ksyun.ks3.service.request.PutObjectRequest;

import java.io.File;

public class KS3Client{

    private Ks3ClientConfig config;
    private Ks3             client;

    public static final String          AK        = "qrAz3cjIltbgmX0iGjJn";
    public static final String          SK        = "0USJAAOhnvczTIA8C44dKMbNTS34vTaf3fHeSd2f";
    public static final String          BEIJING   = "ks3-cn-beijing.ksyun.com";
    public static final String          SHANGHAI  = "ks3-cn-shanghai.ksyun.com";
    public static final String          XIANGGANG = "ks3-cn-hk-1.ksyun.com";
    public static final String          AMERICA   = "ks3-us-west-1.ksyun.com";

    private String bucket                         = "citrusjoy.matrix.storage";

    /**
     * 设置服务地址</br>
     * 中国（北京）| ks3-cn-beijing.ksyun.com
     * 中国（上海）| ks3-cn-shanghai.ksyun.com
     * 中国（香港）| ks3-cn-hk-1.ksyun.com
     * 美国（圣克拉拉）| ks3-us-west-1.ksyun.com
     */
    public KS3Client(String region){
        config = new Ks3ClientConfig();
        config.setProtocol(Ks3ClientConfig.PROTOCOL.http);
        config.setPathStyleAccess(false);

        HttpClientConfig  httpClientConfig = new HttpClientConfig();
        httpClientConfig.setMaxRetry(3);

        config.setHttpClientConfig(httpClientConfig);

        config.setEndpoint(region);
        client = new Ks3Client(AK,SK,config);
    }
    public void setBucket(String bucket){
        this.bucket = bucket;
    }
    public String getBucket(){
        return this.bucket;
    }

    public boolean putFile(String key ,String value){
        PutObjectRequest    putObjectRequest  = new PutObjectRequest(bucket,key,new File(value));
        try{
            client.putObject(putObjectRequest);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean putSimple(String key, String value){

        try {
            client.putObject(bucket, key, value);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public String getSimple(String key) throws Exception{
        GetObjectResult result = client.getObject(bucket,key);
        byte [] res = new byte[128];
        int len = result.getObject().getObjectContent().read(res);
        res[len] = 0;
        System.out.println("Length:" + len);
        return  new String (res,0,len);
    }
}
