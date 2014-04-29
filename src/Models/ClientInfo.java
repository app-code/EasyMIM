package Models;

import java.util.List;

public class ClientInfo {
	private List<String> cookies;
	String base;
	
	public String getBase(){
		return this.base;
	}
	public void setBase(String base){
		if(base!=null && base!=this.base){
			this.base = base;
		}
	}
}
