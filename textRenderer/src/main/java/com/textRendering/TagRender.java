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
	
	public int compareTo(TagRender o) {
		if(id>o.id) return 1;
		else if(id<o.id) return -1;
		else return 0;
	}

	public void run() {
		renderedLine = tagRender();
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

	public String tagRender() {
		int c, i = 0;
		String line = "", tag = "";
//		String[] splitted;
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
		}/*
		if(line.contains("Â&nbsp;")) {
			splitted = line.split("Â&nbsp;");
			line = "";
			for(int j = 0; j<splitted.length; j++) {
				if(!splitted[j].equals("Â&nbsp;")) {
					line+=splitted[j];
				}
			}
		} else if(line.contains("Ã&nbsp;")) {
			splitted = line.split("Ã&nbsp;");
			line = "";
			for(int j = 0; j<splitted.length; j++) {
				if(!splitted[j].equals("Ã&nbsp;")) {
					line+=splitted[j];
				}
			}
		}*/
		return line;
	}
}
