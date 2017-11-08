package com.ibm.microservices.wfd.appetizer;

/**
 *  Patterned after https://github.com/aykutakin/SpringConfigurationPropertiesSample
 *  due to issues with YAML-formatted lists
 **/

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Configuration
@ConfigurationProperties(prefix = "wfd.appetizer")
public class AppetizerConfiguration{

  private List<String> menu;

  @Value("${wfd.appetizer.type}")
  private String type;

  @Value("${wfd.appetizer.order}")
  private int order;

  AppetizerConfiguration(){
	this.menu = new ArrayList<>();
    this.type = new String();
    this.order = 0;
  }

  public List<String> getMenu(){
    return this.menu;
  }

  public String getType(){
    return this.type;
  }

  public int getOrder(){
    return this.order;
  }

}
