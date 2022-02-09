package com.bsoft.rooler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.rooler.config
 * @Description:
 * @date : 2021/12/14 11:24
 */
@Component
public class WebOpenConfig implements CommandLineRunner {

    @Autowired
    private ServletWebServerApplicationContext context;

    @Override
    public void run(String... args) {

        try {
            Runtime.getRuntime().exec("cmd   /c   start   http://localhost:"+context.getWebServer().getPort());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}