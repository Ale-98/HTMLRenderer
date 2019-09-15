package com.textRendering;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageDownloader {

	private Document parsedHtml;

	/**
	 * Constructs a PageDownloader downloading html form given url
	 * @param url Url to download html from
	 * @throws IOException In case of IO error.
	 */
	public PageDownloader(String url) throws IOException{
		// Get html file to be parsed from given url
		parsedHtml = Jsoup.connect(url).timeout(10*1000).userAgent("Mozilla").ignoreHttpErrors(true).get();
	}

	/**
	 * Constructs a PageDownloader by parsing a local html file
	 * @param file The local html file to parse
	 * @throws IOException In case of IO error.
	 */
	public PageDownloader(File file) throws IOException {
		parsedHtml = Jsoup.parse(file, null); //Parses a local html file into a document 
	}

	public String getParsedHtml() {
		return parsedHtml.toString();
	}
}