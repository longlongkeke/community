package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author longke
 * @description
 * @create 2022/6/30 21:07
 */
@SpringBootTest
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void testsensitiveFilter(){
        String text="这里可以赌博，可以嫖娼，可以开票，哈哈哈哈";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);

        String text1="这里可以☆赌☆博☆，可以嫖娼，可以开票，哈哈哈哈";
        String filter1 = sensitiveFilter.filter(text1);
        System.out.println(filter1);

    }
}
