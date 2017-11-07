package com.ibm.microservices.wfd.appetizer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest( properties = { "spring.cloud.config.enabled:false",
																"spring.cloud.discovery.enabled:false" } )
public class AppetizerApplicationTests {
	
	//Config and Discovery services are disabled for contextLoads tests

	@Test
	public void contextLoads() {
	}

}
