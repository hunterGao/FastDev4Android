package com.huntergao.dailyweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HunterGao on 16/1/20.
 */
public class HttpUtils {

    /**
     * 向服务端发送get请求，在线程中执行获取数据
     * @param address 服务端ip地址
     * @param listener 回调接口
     */
    public static void sendHttpRequest(final String address,
                                       final HttpCallBackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getHttpData(address, listener);
            }


        }).start();
    }

    private static void getHttpData(final String address,
                                    final HttpCallBackListener listener) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            //设置连接的属性
            connection.setRequestMethod("GET");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while(null != (line = br.readLine())){
                response.append(line);
            }
            if(null != listener){
                listener.onFinish(response.toString());
            }
        } catch (Exception e) {
            if(null != listener){
                listener.onError(e);
            }
        } finally{
            if(null != connection){
                connection.disconnect();
            }
        }
    }

    public static String getHttpData(final String address) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            //设置连接的属性
            connection.setRequestMethod("GET");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while(null != (line = br.readLine())){
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(null != connection){
                connection.disconnect();
            }
        }
        return null;
    }

    public interface HttpCallBackListener {

        /**
         * 连接结束时
         * @param response 获得服务端返回的数据
         */
        void onFinish(String response);

        /**
         * 连接出错时
         * @param e
         */
        void onError(Exception e);
    }
}