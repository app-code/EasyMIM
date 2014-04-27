package Server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
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

import MIM.WebsiteProcessor;
import Main.HTMLReplacer;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class EasyMIMServer {
	public static HashMap<String,String> sessions = new HashMap<String,String>();
	
	static class EasyMIMServlet extends HttpServlet
	{
		WebsiteProcessor wp;
	    public EasyMIMServlet(){
	    	wp = new WebsiteProcessor();
	    }
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	    	String base = request.getParameter("url");
	    	String url = base;
	    	if(sessions.containsKey(request.getRemoteAddr())){
	    		String tempBase = sessions.get(request.getRemoteAddr());
	    		if(base!=null && tempBase!=null && !tempBase.equals(base)){
	    			sessions.put(request.getRemoteAddr(),base);
	    		}
    			base = sessions.get(request.getRemoteAddr());
    			url = base+request.getRequestURI();
	    	}else{
	    		sessions.put(request.getRemoteAddr(),base);
	    	}
			response.setStatus(HttpServletResponse.SC_OK);
			wp.processRequest(base,url,request, response);
	    	
	    }
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	    {
	    	doGet(request,response);
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
        //context.addServlet(new ServletHolder(new HelloServlet("Bonjour le Monde")),"/fr/*");
        server.start();
        server.join();
	}
}
