package com.yuan.im;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImDemoApplicationTests {

    @Resource
    private ApplicationContext ioc;

    @Test
    public void contextLoads() {
    }


}
