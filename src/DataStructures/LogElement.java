package datastructures;

public class LogElement {
	public String url;
	public String key;
	public String value;
	
	@Override
	public String toString(){
		return "url="+url+";key="+key+";value="+value;
	}
}
