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
	 */
	public PageDownloader(String url){
		try {
			// Get html file to be parsed from given url
			parsedHtml = Jsoup.connect(url).timeout(10*1000).userAgent("Mozilla").ignoreHttpErrors(true).get();
		} catch (IOException e) {
			System.err.println("PageDownloader(url): Errore di I/O");
		}
	}
	
	/**
	 * Constructs a PageDownloader by parsing a local html file
	 * @param file The local html file to parse
	 */
	public PageDownloader(File file) {
		try {
			parsedHtml = Jsoup.parse(file, null); //Parses a local html file into a document 
		} catch (IOException e) {
			System.err.println("PageDownloader(file): Errore di I/O");
		}
	}
	
	public String getParsedHtml() {
		return parsedHtml.toString();
	}
}