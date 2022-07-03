package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author longke
 * @description
 * @create 2022/6/29 12:09
 */
@Controller
@RequestMapping("/test")
public class testController {

    @RequestMapping("/hello")
    @ResponseBody
    public String test(){
        return "hello";
    }
}
