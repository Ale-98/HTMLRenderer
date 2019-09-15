package com.textRendering;

import java.io.File;
import java.io.IOException;

public class Test3 {

	public static void main(String[] args) throws IOException {
		PageDownloader pd = new PageDownloader(new File(args[0]));
//		PageDownloader pd = new PageDownloader("https://www.topristorazione.com/frigoriferi-vino/3544-espositore-verticale-vino-vetri-fume-3-porte-verticali-temperatura-520c-modello-vinity3v.html");
//		PageDownloader pd = new PageDownloader("https://www.tuttoandroid.net/");
		Renderer rend = new Renderer(pd, "C:/Users/Ale/eclipse-workspace/textRenderer/src/main/resources/prova.txt");
		rend.rendering();
	}
}
