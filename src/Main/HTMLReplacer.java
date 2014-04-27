package Main;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLReplacer {
	public static HashMap<String,String[]>domainToSubdomains = new HashMap<String,String[]>();
	static{
		domainToSubdomains.put("google.com", new String[]{"www","content","maps","mail","accounts"});
		domainToSubdomains.put("att.com", new String[]{"www"});
		domainToSubdomains.put("yahoo.com", new String[]{"www"});
		domainToSubdomains.put("amazon.com", new String[]{"www"});
	}
		
	private final String USER_AGENT = "Mozilla/5.0";
	public enum Protocol{HTTP,HTTPS};
	private List<String> cookies;
	public HTMLReplacer(){
		
	}
	public void addMapping(String regex, String html){
		
	}
	public String processHTMLPage(String targetDomainMain, String[] targetSubdomains,String HTMLData){
		//"https://www\\.google\\.com/"
		ArrayList<String> sites = new ArrayList<String>();
		sites.add(targetDomainMain);
		sites.add("google.com");
		
		String[] protocols = {"http://", "https://"};
		String out = HTMLData;
		for(String targetDomain: sites){
			for(String protocol:protocols){
				for(String subdomain:targetSubdomains){
					String expr = protocol+subdomain+"\\."+targetDomain+"/";
					System.out.println(expr);
					//out = m.replaceAll(protocols[0]+subdomain+"\\."+targetDomain);
					Pattern p = Pattern.compile(expr);
					Matcher m = p.matcher(out);
					out = m.replaceAll("/");
					String expr1 = subdomain+"\\."+targetDomain+"/";
					//System.out.println(expr);
					Pattern p1 = Pattern.compile(expr1);
					Matcher m1 = p1.matcher(out);
					out = m1.replaceAll("/");
					String expr2 = "//"+protocol+subdomain+"\\."+targetDomain+"/";
					//System.out.println(expr);
					Pattern p2 = Pattern.compile(expr2);
					Matcher m2 = p2.matcher(out);
					out = m2.replaceAll("/");
				}
			}
			String[] tagPrefixs = {"src=\"","url\\("};//"href=\""  
			for(String tagPrefix:tagPrefixs){
				for(String protocol:protocols){
					/*Works for YouTube*/
					String newTagPrefix = tagPrefix+"[^//]"+protocol+"www\\."+targetDomain;
					Pattern p = Pattern.compile(newTagPrefix);
					Matcher m = p.matcher(out);
					out = m.replaceAll(tagPrefix+"http://www."+targetDomain);
					/*Works for YouTube*/
					/*String newTagPrefix1 = tagPrefix+"/";
					Pattern p1 = Pattern.compile(newTagPrefix1);
					Matcher m1 = p1.matcher(out);
					out = m1.replaceAll(tagPrefix+"http://www."+targetDomain);*/
					
					switch(targetDomain){
						case "att.com":
						case "google.com":
							/*String newTagPrefix2 = tagPrefix+"/";
							Pattern p2 = Pattern.compile(newTagPrefix2);
							Matcher m2 = p2.matcher(out);
							out = m2.replaceAll(tagPrefix+"http://www."+targetDomain+"/");
							break;*/
					}
				}
			}
			/*String[] jsEvents = {"onclick"};
			for(String jsEvent: jsEvents){
				String func=jsEvent+"=\".*\"";
				Pattern p = Pattern.compile(func);
				Matcher m = p.matcher(out);
				String newFunc = jsEvent+"=\"";
				switch(targetDomain){
					case "att.com":
						//userPassword
					default:
						newFunc+="alert('incorrect action')";
						break;
				}
				newFunc+="\"";
				out = m.replaceAll(newFunc);
			}*/
		}
		return out;
	}
	public String handleReplace(String targetDomain, String url, Map<String, String[]> params){
		return handleReplace(targetDomain, url,params,false,"");
	}
	public String handleReplace(String targetDomain, String url, Map<String, String[]> params, boolean write, String dataToSend){
		StringBuilder sb = new StringBuilder();
		try{
			URL u = new URL("http://"+url);
			HttpURLConnection  conn = (HttpURLConnection) u.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setUseCaches(false);
			System.out.println(url);
			for(String key:params.keySet()){
				//System.out.println(key);
				//System.out.println(params.get(key)[0]);
				conn.addRequestProperty(key, params.get(key)[0]);
			}
			//connection.setDoOutput(true);
			//conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			//conn.addRequestProperty("User-Agent", "Mozilla");
			conn.setInstanceFollowRedirects( true );
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			if (cookies != null) {
				for (String cookie : this.cookies) {
					conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
				}
			}
			//conn.addRequestProperty("Referer", "google.com");
	        //connection.setRequestMethod("GET");
			conn.connect();
	        
	        OutputStreamWriter wr = null;
	        BufferedReader rd  = null;
	        String line = null;
	        //wr = new OutputStreamWriter(connection.getOutputStream());
	       // wr.write("");
	       // wr.flush();
	        
	        //read the result from the server
	        rd  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        
	      
	        while ((line = rd.readLine()) != null)
	        {
	            sb.append(line + '\n');
	        }
		}catch(Exception e){
			
		}
        String[] subdomains = new String[]{"www"};
        if(domainToSubdomains.containsKey(targetDomain)){
        	subdomains = domainToSubdomains.get(targetDomain);
        }
        //System.out.println(processHTMLPage(targetDomain,subdomains,sb.toString()));
        return processHTMLPage(targetDomain,subdomains,sb.toString());
	}
}
