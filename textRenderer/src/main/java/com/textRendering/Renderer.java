package com.textRendering;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.CyclicBarrier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class Renderer {

	private TreeSet<TagRender> threads = new TreeSet<TagRender>();
	private String fileToRender;
	private String where;

	/**
	 * Construct a Renderer from the given String.
	 * @param parsed The parsed String to be rendered.
	 * @param where Directory in which save the results.
	 */
	public Renderer(String parsed, String where) {
		fileToRender = cleanContent(parsed);
		this.where = where;
	}
	
	/**
	 * Construct a Renderer from the given Document.
	 * @param pd The parsed Document to be parsed.
	 * @param where Directory in which save the results.
	 */
	public Renderer(PageDownloader pd, String where) {
		fileToRender = cleanContent(pd.getParsedHtml());
		this.where = where;
	}

	public String getFile() {
		return fileToRender;
	}

	/**
	 * Implements a MapReduce pattern for elaborating the file to be rendered.
	 * Uses the file to be rendered(which is usually obtained from e PageDownloader) 
	 * and generate a tagRender to elaborate every line of the file throw a map operation.
	 * Generated runnables objects are sorted in TreeSet for final reduce operation.
	 */
	public void rendering() {
		StringTokenizer stk = new StringTokenizer(fileToRender, "\n");
		Reducer red = new Reducer(threads, where);
		CyclicBarrier theBarrier = new CyclicBarrier(stk.countTokens(), red);
		while(stk.hasMoreTokens()) {
			TagRender tg = new TagRender(stk.nextToken(), theBarrier);
			threads.add(tg);
			new Thread(tg).start();
		}
	}	
	

	/**
	 * Cleans the html content leaving only the following tags: b, em, i, strong, u, br, cite, em, i, p, strong, img, li, ul, ol, sup, sub, s
	 * @param content html content
	 * @param extraTags any other tags that you may want to keep, e. g. "a"
	 * @return the cleaned HTML file(ready to be rendered)
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
}
