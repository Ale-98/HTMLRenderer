package com.textRendering;

import java.io.FileWriter;
import java.io.IOException;
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

	public Renderer(String parsed) {
		fileToRender = cleanContent(parsed);
	}
	
	public Renderer(PageDownloader pd) {
		fileToRender = cleanContent(pd.getParsedHtml());
	}

	public String getFile() {
		return fileToRender;
	}

	/**
	 * Scrive la stringa che rappresenta questo oggetto in un File. 
	 * @return Il File creato.
	 * @throws IOException In caso di errore di IO.
	 */
	public void toFile() throws IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		System.out.println("Dove memorizzo il File?");
		String fileDirectory = "C:/Users/Ale/eclipse-workspace/textRenderer/src/main/resources/prova.txt";		
		FileWriter fw = new FileWriter(fileDirectory);
		fw.write(fileToRender);
		fw.flush();
		fw.close();
	}

	public void rendering() {
		StringTokenizer stk = new StringTokenizer(fileToRender, "\n");
		Reducer red = new Reducer(threads);
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
}
