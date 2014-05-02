package Models;

public class EasyMIMConfig {
	public String targetURL;
	public String imageURL;
	public String popUpMessage;
	public boolean keylogger;
	public String logSavePath;
	
	public EasyMIMConfig(){
		this.targetURL = "google.com";
		this.keylogger = true;
		this.logSavePath = "/log.txt";
	}
}
