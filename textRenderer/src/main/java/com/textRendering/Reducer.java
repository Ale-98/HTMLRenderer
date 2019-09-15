package com.textRendering;

import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

public class Reducer implements Runnable{

	private TreeSet<TagRender> threads;
	private String renderedText, where;
	
	public Reducer(TreeSet<TagRender> threads, String where) {
		this.threads = threads;
		this.where = where;
	}
	
	public String getResult() {
		return renderedText;
	}
	
	/**
	 * Write the elaborated text in a File created in the directory "where".
	 * @throws IOException In case of I/O error.
	 */
	public void toFile(String where) throws IOException {
//		String fileDirectory = "C:/Users/Ale/eclipse-workspace/textRenderer/src/main/resources/prova.txt";		
		FileWriter fw = new FileWriter(where);
		fw.write(renderedText);
		fw.flush();
		fw.close();
	}
	
	public void run() {
		for(TagRender tg:threads) {
			renderedText+=tg.getRenderedLine().strip()+"\n";
		}
		try {
			toFile(where);
		} catch (IOException e) {
			System.err.println("IO error writing results in file");
			e.printStackTrace();
		}
		System.out.println(renderedText);
	}
}
