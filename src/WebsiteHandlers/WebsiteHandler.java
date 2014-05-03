package websitehandlers;

import org.jsoup.nodes.Document;

import datastructures.ClientInfo;
import datastructures.RequestInfo;

public interface WebsiteHandler {

	public void process(RequestInfo ri, ClientInfo ci, Document html);
}
