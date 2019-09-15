package com.textRendering;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TagRender implements Runnable, Comparable<TagRender>{

	private static int counter = 0;
	private final int id = counter++;
	private String myLine; 
	private String renderedLine;
	private CyclicBarrier theBarrier;
	
	public TagRender(String l, CyclicBarrier cb) {
		myLine = l;
		theBarrier = cb;
	}
	
	public String getRenderedLine() {
		return renderedLine;
	}
	
	/**
	 * Set a way of sorting TagRenderers looking at their id.
	 */
	public int compareTo(TagRender o) {
		return id-o.id;
	}

	public void run() {
		renderedLine = tagRendering();
		try {
			theBarrier.await();
		} catch (InterruptedException e) {
			System.err.println("TagRender_"+id+" interrupted");
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			System.err.println("TagRender_"+id+": Barrier has been broken");
			e.printStackTrace();
		}
	}

	/**
	 * Elaborates a line of the file to be rendered. Optimized for bulleted lists.
	 * @return The rendered line.
	 */
	public String tagRendering() {
		int c, i = 0;
		String line = "", tag = "";
		boolean toWrite = true;
		while(i<myLine.length()) {
			c=myLine.charAt(i++);
			if(c=='<') {toWrite = false;}
			if(toWrite) {line+=Character.toString(c);}
			if(!toWrite) {tag+=Character.toString(c);}
			if(c=='>') {
				if(tag.equals("</ul>")) {line+="_____________________\r\n";}
				if(tag.equals("<li>")) {line+="-";}
				if(tag.equals("<strong>")) {line+="->";}
				if(tag.equals("<h3>")) {line+="-->";}
				toWrite = true; tag = "";
			}
		}
		return line;
	}
}
