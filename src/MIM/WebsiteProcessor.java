package MIM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.*;

public class WebsiteProcessor {
	private final String USER_AGENT = "Mozilla/5.0";
	public enum Protocol{HTTP,HTTPS};
	private List<String> cookies;
	public void processRequest(String base, String url,HttpServletRequest request, HttpServletResponse response) throws IOException{
		String html = getRawResponseAndSetBasic(url,"GET",request,response);
		String modifiedHtml = processHTML(base,html);
		response.getWriter().write(modifiedHtml);
	}
	public String processHTML(String base, String html){
		Document doc = Jsoup.parse(html);
		Element link = doc.select("a");
	}
	public String getRawResponseAndSetBasic(String url,String method,HttpServletRequest request, HttpServletResponse response) throws IOException{
		URL u = new URL("http://"+url);
		HttpURLConnection  conn = (HttpURLConnection) u.openConnection();
		conn.setReadTimeout(5000);
		conn.setRequestMethod(method);
		conn.setUseCaches(false);
		Map<String, String[]> params = request.getParameterMap();
		for(String key:params.keySet()){
			conn.addRequestProperty(key, params.get(key)[0]);
		}
		if(method.equals("POST")){
			conn.setDoOutput(true);
		}

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
		conn.connect();
        
        OutputStreamWriter wr = null;
        BufferedReader rd  = null;
        String line = null;
        StringBuilder sb = new StringBuilder();
        if(method.equals("POST")){
	       wr = new OutputStreamWriter(conn.getOutputStream());
	       wr.write("something");
	       wr.flush();
        }
        
        //read the result from the server
        rd  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        
      
        while ((line = rd.readLine()) != null)
        {
            sb.append(line + '\n');
        }
        return sb.toString();
	}
}
