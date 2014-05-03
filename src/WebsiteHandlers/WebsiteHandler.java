package websitehandlers;

import org.jsoup.nodes.Document;

public interface WebsiteHandler {

	public void process(Document html);
}
