package websitehandlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import datastructures.ClientInfo;
import datastructures.RequestInfo;

public class BankOfAmericanHandler implements WebsiteHandler{

	@Override
	public void process(RequestInfo ri, ClientInfo ci, Document html) {
		/*Needed for Bank of America*/
		Elements boas = html.select("div[data-fallback]");
		for(Element boa:boas){
			boa.attr("data-mboxer", "");
			String dataF = boa.attr("data-fallback");
			Pattern p = Pattern.compile("src=\"");
			Matcher m = p.matcher(dataF);
			dataF=m.replaceAll("src=\"http://"+ci.getBase());
			boa.append(dataF);
		}
		/*Needed for Bank of America*/
	}

}
