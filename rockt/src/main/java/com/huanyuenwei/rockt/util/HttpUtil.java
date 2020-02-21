package com.huanyuenwei.rockt.util;


import net.sf.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static String Enter = "\r\n";
    // 设置边界
    private static String BOUNDARY = "----------" + System.currentTimeMillis();

    public static void main(String[] args) throws IOException {
        String setpdfurl = "http://www.daizhihua.cn/ancc-manager-web/invoice/setpdf";
        String getpdfUrl = "http://www.daizhihua.cn/ancc-manager-web/invoice/getpdf";
        String createCard = "http://www.daizhihua.cn/ancc-manager-web/invoice/createcard";
        String insertPdf = "http://www.daizhihua.cn/ancc-manager-web/invoice/insertInvoice";
        String sendMessage = "http://www.daizhihua.cn/ancc-manager-web/wXLoginController/sendmessagegzh";

        //上传pdf
            /*JSONObject send = send(setpdfurl, "D:\\node\\invoice_1_.pdf", "20191100000000P21141");
            System.out.println(send);*/

        //查询上传的pdf
            /*Map<String,String> map = new HashMap<>();
            map.put("s_media_id","2595810184280998468");
            JSONObject post = httpUrl(getpdfUrl, map);*/

        //插入发票到微信卡包
         /*   Map<String,String>map = new HashMap<>();
            map.put("sn","20191100000000394742");
            map.put("s_pdf_media_id","2522505125679399468");
            map.put("money","4501");
            map.put("firmName","测试公司");
            map.put("billing_no","011001900111");
            map.put("billing_code","75494126");
            map.put("fee_without_tax","560312");
            map.put("tax","5610");
            map.put("check_code","74217645684014614757");
            map.put("branchcode","1105");
            map.put("custtaxno","0");
        JSONObject jsonObject = httpUrl(insertPdf, map);
        System.out.println(jsonObject);*/

        //创建会员卡的card
        /*Map<String,String>map = new HashMap<>();
        map.put("title","环宇恩维");
        map.put("logo_url","wwww.daizhihua.cn");
        map.put("type","税普通发票");
        map.put("payee","北京司");
        JSONObject jsonObject = httpUrl(createCard, map);
        System.out.println(jsonObject);*/

        //发送消息

      /*  Map<String,String> map = new HashMap<>();
        map.put("phonenum","18010091126");
        map.put("type","注册");
        map.put("status","通过");
        map.put("createTime","2018-11-12");*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", "daizhihua");
        jsonObject.put("password", "daizhihua1996");
        String result = sendPost("http://www.jiatingting.cn:3000/api/v1/login", jsonObject.toString());
        System.out.println(result);
//        System.out.println(result);
        /*JSONObject json = JSONObject.fromObject(result);
        System.out.println(json.get("status"));
        json.get("data");
        if(json.get("data")instanceof JSONObject){
           System.out.println(((JSONObject) json.get("data")).get("authToken"));
        }
        System.out.println(json.get("data"));
*/
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));

                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            System.out.println(buffer.toString());
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            System.out.println("Weixin server connection timed out.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("https request error:{}");
//            log.error("https request error:{}", e);
        }
        return jsonObject;
    }


    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
// 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();

// 设置通用的请求属性

            connection.setRequestProperty("accept", "*/*");

            connection.setRequestProperty("connection", "Keep-Alive");

            connection.setRequestProperty("user-agent",

                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

// 建立实际的连接

            connection.connect();

// 获取所有响应头字段

            Map<String, List<String>> map = connection.getHeaderFields();

// 遍历所有的响应头字段

            for (String key : map.keySet()) {

                System.out.println(key + "--->" + map.get(key));

            }

// 定义 BufferedReader输入流来读取URL的响应

            in = new BufferedReader(new InputStreamReader(

                    connection.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {

                result += line;

            }

        } catch (Exception e) {

            System.out.println("发送GET请求出现异常！" + e);

            e.printStackTrace();

        }

// 使用finally块来关闭输入流

        finally {

            try {

                if (in != null) {

                    in.close();

                }

            } catch (Exception e2) {

                e2.printStackTrace();

            }

        }

        return result;

    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */

    public static String sendPost(String url, String param) {

        PrintWriter out = null;

        BufferedReader in = null;

        String result = "";

        try {

            URL realUrl = new URL(url);

// 打开和URL之间的连接

            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
// 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
// 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
// 发送请求参数
            out.print(param);
// flush输出流的缓冲
            out.flush();
// 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
//使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {

                    out.close();
                }

                if (in != null) {

                    in.close();

                }

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        return result;

    }

}
