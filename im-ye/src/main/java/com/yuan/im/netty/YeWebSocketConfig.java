package com.yuan.im.netty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yeauty.standard.ServerEndpointExporter;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P>https://blog.csdn.net/chengxi8732/article/details/100704973?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_title-1&spm=1001.2101.3001.4242</P>
 *
 * @author: YuanJiaMin
 * @date: 2021/3/16 3:21 下午
 */
@Configuration
public class YeWebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}


