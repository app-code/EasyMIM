package websitehandlers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import datastructures.ClientInfo;
import datastructures.RequestInfo;

public class GoogleHandler implements WebsiteHandler{

	@Override
	public void process(RequestInfo ri, ClientInfo ci, Document html) {
		// TODO Auto-generated method stub
		/*Needed for Bank of google*/
		Elements googs = html.select("#lang-chooser-wrap");
		for(Element goog:googs){
			goog.html("");
		}
	}

}
