package com.huanyuenwei.rockt.controller;

import com.huanyuenwei.rockt.common.ProjectContos;
import com.huanyuenwei.rockt.util.HttpUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
//@RequestMapping(value = "/login")
public class LoginController {
    @RequestMapping(value = "/login")
//    @ResponseBody
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    public void sunmit(@RequestParam("user")String user, String password, HttpServletResponse response, HttpServletRequest res) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        System.out.println("user是"+user);
        System.out.println("passwrd"+password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",user);
        jsonObject.put("password",password);
        //String result = ;
        JSONObject reslut =  JSONObject.fromObject(HttpUtil.sendPost(ProjectContos.LOGIN_URL, jsonObject.toString()));
        if(reslut.has("status")){
            reslut.get("data");
            if(reslut.get("data") instanceof JSONObject){
                System.out.println(((JSONObject) reslut.get("data")).get("authToken"));
                String token = ((JSONObject) reslut.get("data")).get("authToken").toString();
                response.setContentType("text/html");
                String string = "<script type='text/javascript'>" +
//                       "alert(10);"+
                "window.parent.postMessage({" +
                "event: 'login-with-token', " +
                "loginToken: '"+token+"'}"+
                ",'you name url');" +
                        "</script>";
                PrintWriter writer = response.getWriter();
                String srt = "<script type='text/javascript'>alert(1)</script>";
                writer.println(string);
                //response.getWriter().write(string);
            }
        }else{
            response.getWriter().write("连接失败");
        }

    }
}
