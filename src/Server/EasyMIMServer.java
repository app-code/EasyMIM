package Server;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import org.eclipse.jetty.util.Jetty;

import sun.org.mozilla.javascript.internal.json.JsonParser;

import DataStructures.ClientInfo;
import DataStructures.EasyMIMConfig;
import DataStructures.LogElement;
import Logger.Logger;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class EasyMIMServer {
	public HashMap<String,ClientInfo> sessions = new HashMap<String,ClientInfo>();
	public Server server;
	public ServletContextHandler context;
	public WebsiteProcessor wp;
	public EasyMIMConfig config;
	public Logger logger;
	
	class EasyMIMServlet extends HttpServlet
	{
	    public EasyMIMServlet(){}
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	    	try{
		    	if(!sessions.containsKey(request.getRemoteAddr())){
		    		sessions.put(request.getRemoteAddr(),new ClientInfo());
		    	}
		    	if(request.getRequestURI().contains("log")){
		    		logger.logRequest(config.logSavePath, request);
		    		return;
		    	}
		    	ClientInfo ci = sessions.get(request.getRemoteAddr());
				response.setStatus(HttpServletResponse.SC_OK);
				wp.processRequest(request, response,ci);
				//System.out.println(this.getServletContext());
	    	}catch(Exception e){
	    		
	    	}
	    	
	    }
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	    	doGet(request,response);
	    }
	}
	private void setUpServer(){
        server = new Server(8080);
        context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new EasyMIMServlet()),"/*");
	}
	public EasyMIMServer(){
		this(new EasyMIMConfig());
	}
	public EasyMIMServer(EasyMIMConfig config){
		this.config = config;
		wp = new WebsiteProcessor(config);
		logger = new Logger();
		this.setUpServer();
	}
	public void startServer() throws Exception{
        server.start();
        server.join();
	}
	public void terminateServer() throws Exception{
		server.stop();
	}
	public static void main(String[] args) throws Exception
	{
		new EasyMIMServer().startServer();
	}
	public static void generateServer(EasyMIMConfig config){
		
	}
}
