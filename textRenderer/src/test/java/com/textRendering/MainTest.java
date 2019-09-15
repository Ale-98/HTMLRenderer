package com.textRendering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

public class MainTest {

	private List<String> righe = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {

		String urlStr="https://www.tuttoandroid.net/";
		
		//try {
			//PageDownloader pd = new PageDownloader(urlStr);
			File from = new File(args[0]);
			File to = new File(args[1]);
//			FileWriter fw = new FileWriter(to);
//			fw.write(Jsoup.parse(txtToString(from)).toString());
//			System.out.println(Jsoup.parse(txtToString(from)).toString());
			Renderer theRenderer = new Renderer(Jsoup.parse(txtToString(from)).toString(), null);
			theRenderer.rendering();
//			theRenderer.tagRender();
//			theRenderer.printLines();
		/*} catch (IOException e) {
			System.err.println("Error writing html file");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			System.err.println("Given URL does not exists");
			e.printStackTrace();
		}*/
	}
	
	public static String txtToString(File f) {
		String htmlText = "";
		String line = "";
		BufferedReader fr = null;
		try {
			fr = new BufferedReader(new FileReader(f));
			while((line = fr.readLine())!=null) {
				htmlText+=line;
			}
		} catch (FileNotFoundException e) {
			System.err.println("===File not found===");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("===I/O exception reading file===");
			e.printStackTrace();
		}
		finally {
			try {
				fr.close();
			} catch (IOException e) {
				System.err.println("Reader not closed");
				e.printStackTrace();
			}
		}
		return htmlText;
	}
	
	public void tagRender(String fileToRender) {
		int c, i = 0, tagCounter = 0;
		String line = "", tag = "", endLineTag = "";
		boolean toWrite = true;
		while(i<fileToRender.length()) {
			c=fileToRender.charAt(i++);
			if(c=='<') {toWrite = false;}
			if(toWrite) {line+=Character.toString(c); System.out.print(Character.toString(c));
//				if(i%100==0) System.out.println();
			}
			if(i==2000)break;
			if(!toWrite) {tag+=Character.toString(c);}
			if(c=='>') {
				toWrite = true; tagCounter++; 
				if(tagCounter == 1) {endLineTag = tag;}
				else if(tag.endsWith(endLineTag.substring(1))) {
//					new TagRender(line);
					righe.add(line);
					tagCounter = 0;
					line = "";
				}
				tag = "";
			}
		}
	}
	
	public void printLines() {
		for(String s:righe) {
			System.out.println(s);
		}
	}
	
}
