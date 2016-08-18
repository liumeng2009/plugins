package com.meng.plugin.pushservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by liumeng on 2016/8/16.
 */
public class HttpUtil {
    public static HttpClient httpclient=new DefaultHttpClient();
    public static final String BASE_URL="http://liumeng.iego.cn/api/";
    public static String getRequest(final String url) throws Exception{
        FutureTask<String> task=new FutureTask<String>(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        HttpGet get=new HttpGet(url);
                        HttpResponse httpResponse=httpclient.execute(get);
                        if(httpResponse.getStatusLine().getStatusCode()==200){
                            String result= EntityUtils.toString(httpResponse.getEntity());
                            return result;
                        }
                        return null;
                    }
                }
        );
        new Thread(task).start();
        return task.get();
    }
}
