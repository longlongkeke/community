package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author longke
 * @description
 * @create 2022/7/3 22:27
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Value("${community.path.domain}")
    private String domain;
    @Value("${community.path.upload}")
    private String uploadpath;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }
    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerimage, Model model){
        if(headerimage==null){
            model.addAttribute("error","您还没选择图片！");
            return "/site/setting";
        }
        String filename = headerimage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }
        //生成随机文件名称
        String s = CommunityUtil.generateUUID() + suffix;
        //确定文件存放的位置
        File dest=new File(uploadpath+"/"+s);
        try {
            headerimage.transferTo(dest);
        } catch (IOException e) {
            logger.error("文件上传失败！"+e.getMessage());
            throw  new RuntimeException("文件上传失败",e);
        }
        User user = hostHolder.getUserThreadLocal();
        String headerUrl=domain+"/community/"+"user/header/"+s;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";

    }
    @RequestMapping(path = "/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename")String filename, HttpServletResponse response){
        filename=uploadpath+"/"+filename;
        String suffix = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/"+suffix);

        try {
            OutputStream outputStream = response.getOutputStream();
            FileInputStream fis =new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            int b=0;
            while ((b=fis.read(buffer))!=-1){
                outputStream.write(buffer,0,b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
