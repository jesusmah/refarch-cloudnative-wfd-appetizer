package com.ibm.microservices.wfd;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@EnableConfigurationProperties
@ResponseBody
public class AppetizerController {

    @Autowired
    private AppetizerConfiguration config;

    @RequestMapping("/menu")
    public List<String> getMealMenu() {
        return this.config.getMenu();
    }

    @RequestMapping("/type")
    public String getMealType(){
      return this.config.getType();
    }

    @RequestMapping("/order")
    public int getMealOrder(){
      return this.config.getOrder();
    }

}
