package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;

import javax.naming.Context;

/**
 * @author longke
 * @description
 * @create 2022/6/29 19:13
 */

@SpringBootTest
public class MailTests {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void testTextmail(){
        mailClient.setMail("1205547998@qq.com","Test","welcome");
    }
    @Test
    public void testHtmlMail(){
        org.thymeleaf.context.Context context=new org.thymeleaf.context.Context();
        context.setVariable("username","123456");
         String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.setMail("1205547998@qq.com","HTML",content);

    }
}
