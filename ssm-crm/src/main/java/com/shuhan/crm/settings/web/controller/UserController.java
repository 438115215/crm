package com.shuhan.crm.settings.web.controller;

import com.shuhan.crm.settings.domain.User;
import com.shuhan.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.SimpleBeanInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/tologin.do")
    public String toLogin(){
    return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd,
                                      HttpServletRequest request, HttpSession session, HttpServletResponse response){
        Map<String,Object> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user=userService.queryUserByLoginActAndPwd(map);

        Map<String,Object>retmap=new HashMap<>();
        if(user==null){
            retmap.put("code",0);
            retmap.put("message","账号或密码错误");
        }else{
            new Date();

            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:hh:ss");
            String nowStr=simpleDateFormat.format(new Date());

            if(nowStr.compareTo(user.getExpireTime())>0){
                retmap.put("code",0);
                retmap.put("message","账号已过期");

            }else if ("0".equals(user.getLockState())){
                retmap.put("code",0);
                retmap.put("message","账号被锁定");

            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){
                retmap.put("code",0);
                retmap.put("message","ip受限");

            }else{
                retmap.put("code",1);
                session.setAttribute("sessionUser",user);

                if ("true".equals(isRemPwd)){
                    Cookie c1=new Cookie("loginAct",loginAct);
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);

                    Cookie c2=new Cookie("loginPwd",loginPwd);
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c2);

                }
                else{
                    Cookie c1=new Cookie("loginAct",loginAct);
                    c1.setMaxAge(0);
                    response.addCookie(c1);

                    Cookie c2=new Cookie("loginPwd",loginPwd);
                    c1.setMaxAge(0);
                    response.addCookie(c2);
                }
            }



        }
return retmap;
    }
}
