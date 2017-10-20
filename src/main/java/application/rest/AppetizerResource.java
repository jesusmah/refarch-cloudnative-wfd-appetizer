package application.rest;

import java.util.Arrays;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

@Path("appetizer")
public class AppetizerResource {
    
	@Inject
	Config config;
	
	@GET
	public Response getAllProperties(){
		Config config = ConfigProvider.getConfig();
		Appetizer local = new Appetizer();
		local.setMenu(Arrays.asList(config.getValue("menu", String.class)));
		local.setOrder(Integer.parseInt(config.getValue("order", String.class)));
		local.setType(config.getValue("type", String.class));
		return Response.ok(local, MediaType.APPLICATION_JSON).build();
	}
}
