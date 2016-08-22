package com.ibm.microservices.wfd.appetizer;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.microservices.wfd.appetizer.model.Appetizer;

@RestController
@EnableConfigurationProperties
@ResponseBody
public class AppetizerController {

    @Autowired
    private AppetizerConfiguration config;

    @RequestMapping("/menu")
    public Appetizer getMealMenu() {
      Appetizer local = new Appetizer();
      local.setMenu(this.config.getMenu());
      local.setType(this.config.getType());
      local.setOrder(this.config.getOrder());
      return local;
    }


}
