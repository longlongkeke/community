package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author longke
 * @description
 * @create 2022/6/30 10:49
 */
@Controller
public class LoginController implements CommunityConstant{

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private Producer producer;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegister(){
        return "site/register";
    }
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String Register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(map==null||map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已经向您邮箱发送了一封激活邮件，请尽快激活。");
            model.addAttribute("target","/index");
            return "site/operate-result";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }

    }
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String login(){
        return "/site/login";
    }
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code){
        int activation = userService.activation(userId, code);
        logger.debug("activation:{}",activation);
        if(activation==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功！");
            model.addAttribute("target","/login");
            return "site/operate-result";
        }else if(activation==ACTIVATION_REPEATE){
            model.addAttribute("msg","重复激活！");
            model.addAttribute("target","/index");
            return "site/operate-result";
        }else{
            model.addAttribute("msg","激活失败！");
            model.addAttribute("target","/index");
            return "site/operate-result";
        }

    }
    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码
        String text = producer.createText();
        logger.debug("text:{}",text);
        BufferedImage image = producer.createImage(text);


        //session.setAttribute("kaptcha",text);//将验证码存到session

        //验证码的归属
        String Kaptchaowner = CommunityUtil.generateUUID();
        Cookie cookie=new Cookie("Kaptchaowner",Kaptchaowner);
        cookie.setMaxAge(60);
        cookie.setPath("/community");
        response.addCookie(cookie);
        String kaptchakey = RedisKeyUtil.getKaptchakey(Kaptchaowner);
        redisTemplate.opsForValue().set(kaptchakey,text,60, TimeUnit.SECONDS);


        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
           logger.error("响应验证码失败！"+e.getMessage());
        }
    }
    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String username,String password,String code,boolean rememeber,Model model,
                        /*HttpSession session,*/HttpServletResponse response,HttpServletRequest request,
                        @CookieValue(value = "Kaptchaowner") String kaptchaOwner){
        //String kaptcha =(String) session.getAttribute("kaptcha");
         //kaptchaOwner = CookieUtil.getValue(request,"Kaptchaowner");
        String kaptcha=null;
        if(StringUtils.isNoneBlank(kaptchaOwner)){
            String kaptchakey = RedisKeyUtil.getKaptchakey(kaptchaOwner);
            System.out.println(kaptchakey);
             kaptcha = (String)redisTemplate.opsForValue().get(kaptchakey);

        }
        System.out.println("kaptchaOwner:"+kaptchaOwner);
        System.out.println("kaptcha"+kaptcha);
        System.out.println(code);
        if(StringUtils.isBlank(code)||StringUtils.isBlank(kaptcha)||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }
        //
        int expiredSeconds = (rememeber?REMEMEBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS);
        Map<String,Object> map=userService.login(username,password,expiredSeconds);
        logger.debug("map:{}",map);
        if(map.containsKey("ticket")){
            Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath("/community");
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);

            return "redirect:/index";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }

    }
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }


}
