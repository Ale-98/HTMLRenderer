package com.textRendering;

import java.util.TreeSet;

public class Reducer implements Runnable{

	private TreeSet<TagRender> threads;
	private String renderedText;
	
	public Reducer(TreeSet<TagRender> threads) {
		this.threads = threads;
	}
	
	public String getResult() {
		return renderedText;
	}
	
	public void run() {
		for(TagRender tg:threads) {
			renderedText+=tg.getRenderedLine().strip()+"\n";
		}
		System.out.println(renderedText);
	}
}
