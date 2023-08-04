package com.example.cbepis.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.example.cbepis.entity.User;
import com.example.cbepis.service.UserService;
import com.example.cbepis.vo.ViewData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;


    @RequestMapping("toLogin")
    public String toLogin(){
        return "login";
    }

    //验证码
    @RequestMapping("/login/getCode")
    public void getCode(HttpServletResponse response, HttpSession session) throws IOException {
        //验证码对象 定义图形验证码的长和宽，验证码位数，干扰线条数
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 36, 4, 20);
        //放入Session
        session.setAttribute("code",lineCaptcha.getCode());
        //输出
        ServletOutputStream outputStream=response.getOutputStream();
        lineCaptcha.write(outputStream);
        outputStream.close();
    }
    //登陆请求
    @RequestMapping("/login/login")
    @ResponseBody
    public ViewData login(String username,String password,String code,HttpSession session){
        ViewData viewData=new ViewData();
        String SessionCode = (String) session.getAttribute("code");
        if(code!=null && SessionCode.equals(code)){
            //User user=userService.login(username,password);普通登录
            //shiro登录
            Subject subject= SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            subject.login(usernamePasswordToken);
            User user=(User)subject.getPrincipal();
            if(user!=null){
                session.setAttribute("user",user);
                viewData.setCode(200);
                viewData.setMsg("登陆成功");
                return viewData;
            } else {
                viewData.setCode(100);
                viewData.setMsg("用户名或密码错误");
                return viewData;
            }
        }else{
            viewData.setCode(100);
            viewData.setMsg("验证码错误");
            return  viewData;
        }
    }

    @RequestMapping("/login/logout")
    public String logout(){
        Subject subject=SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }
}
