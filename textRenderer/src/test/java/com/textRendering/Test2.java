package com.textRendering;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class Test2 {

	private static Document doc;
	private static String cleaned;
	private String title;
	private String text;
	private String linkHref;
	private String linkText;
	private String linkOuterHtml;
	private String linkInnerHtml;
	
	public Test2() {}
	
	public void extractDataWithJsoup(String href){
		Whitelist wt = new Whitelist();
		wt.addTags("h3" ,"strong", "ul", "li");
		Cleaner cleaner = new Cleaner(wt);
		try {
			doc = Jsoup.connect(href).timeout(10*1000).userAgent("Mozilla").ignoreHttpErrors(true).get();
//			doc = Jsoup.parse(new File(href), null);
//			doc = cleaner.clean(doc);
			cleaned = cleanContent(doc.toString());
		} catch (IOException e) {
			//Your exception handling here
		}
		if(doc != null){
			title = doc.title();
			text = doc.body().text();
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				linkHref = link.attr("href");
				linkText = link.text();
				linkOuterHtml = link.outerHtml(); 
				linkInnerHtml = link.html();
			}
		}
	}
	
	public void display() {
		System.out.println(doc.title()+"\n");
		Elements element = doc.select("body");
	    System.out.println("Text: "+element.text());
	}
	
	public void print() {
		System.out.println(title);
		System.out.println();
		System.out.println(text);
		System.out.println(linkHref);
		System.out.println(linkText);
		System.out.println(linkOuterHtml);
		System.out.println(linkInnerHtml);
	}
	
	/**
	 * Cleans the html content leaving only the following tags: b, em, i, strong, u, br, cite, em, i, p, strong, img, li, ul, ol, sup, sub, s
	 * @param content html content
	 * @param extraTags any other tags that you may want to keep, e. g. "a"
	 * @return
	 */
	public String cleanContent(String content, String ... extraTags) {
		Whitelist allowedTags = Whitelist.simpleText(); // This whitelist allows only simple text formatting: b, em, i, strong, u. All other HTML (tags and attributes) will be removed.
		allowedTags.addTags("br", "cite", "em", "i", "p", "strong", "img", "li", "ul", "ol", "sup", "sub", "s");
		allowedTags.addTags(extraTags);
		allowedTags.addAttributes("p", "style"); // Serve per l'allineamento a destra e sinistra
		allowedTags.addAttributes("img", "src", "style", "class"); 
		if (Arrays.asList(extraTags).contains("a")) {
			allowedTags.addAttributes("a", "href", "target"); 
		}
		Document dirty = Jsoup.parseBodyFragment(content, "");
		Cleaner cleaner = new Cleaner(allowedTags);
		Document clean = cleaner.clean(dirty);
//		clean.outputSettings().escapeMode(EscapeMode.xhtml); // Non fa l'escape dei caratteri utf-8
		String safe = clean.body().html();
		return safe;
	}
	
	public static void main(String[] args) {
		Test2 t2 = new Test2();
//		t2.extractDataWithJsoup(args[0]);
		t2.extractDataWithJsoup("https://www.topristorazione.com/frigoriferi-vino/3544-espositore-verticale-vino-vetri-fume-3-porte-verticali-temperatura-520c-modello-vinity3v.html");
//		t2.extractDataWithJsoup("https://www.ebay.it/");
//		t2.display();
		Renderer r = new Renderer(cleaned);
		r.rendering();
//		System.out.println(doc);
	}

}
