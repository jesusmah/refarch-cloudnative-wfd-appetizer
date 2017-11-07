package com.ibm.microservices.wfd.appetizer;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.ibm.microservices.wfd.appetizer.model.Appetizer;

@RestController
@EnableConfigurationProperties
@ResponseBody
public class AppetizerController {

    @Autowired
    private AppetizerConfiguration config;

    @ApiOperation(value = "Get the appetizers", notes = "Returns a list of the appetizers available on the menu")
    @RequestMapping(method = RequestMethod.GET, path="/appetizers", produces = "application/json")
    @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = Appetizer.class),
      @ApiResponse(code = 401, message = "Unauthorized"),
      @ApiResponse(code = 403, message = "Forbidden"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Failure")})
    public Appetizer getMealMenu() {
      Appetizer local = new Appetizer();
      local.setMenu(this.config.getMenu());
      local.setType(this.config.getType());
      local.setOrder(this.config.getOrder());
      return local;
    }


}
