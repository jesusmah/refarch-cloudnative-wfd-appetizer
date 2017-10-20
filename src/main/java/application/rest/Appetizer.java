package application.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public class Appetizer {
	
	  @Inject
	  @ConfigProperty(name="order")
	  private int order;

	  @Inject
	  @ConfigProperty(name="menu")
	  private List<String> menu;

	  @Inject
	  @ConfigProperty(name="type")
	  private String type;

	  public Appetizer(){
	    this.order = 0;
	    this.menu = new ArrayList<String>();
	    this.type = "none";
	  }

	  public Appetizer(int order, List<String> menu, String type){
	    this.order = order;
	    this.menu = menu;
	    this.type = type;
	  }

	  public void setOrder(int order){
	    this.order = order;
	  }

	  public int getOrder(){
	    return this.order;
	  }

	  public void setMenu(List<String> menu){
	    this.menu = menu;
	  }

	  public List<String> getMenu(){
	    return this.menu;
	  }

	  public void setType(String type){
	    this.type = type;
	  }

	  public String getType(){
	    return this.type;
	  }



}
