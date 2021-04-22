package com.carlos.wiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class WikiApplication {

    private static final Logger LOG = LoggerFactory.getLogger(WikiApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication();
        ConfigurableEnvironment environment = app.run(WikiApplication.class, args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("地址：\thttp://localhost:{}",environment.getProperty("server.port"));
    }

}
