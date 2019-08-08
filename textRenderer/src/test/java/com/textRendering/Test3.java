package com.textRendering;

import java.io.File;

public class Test3 {

	public static void main(String[] args) {
		PageDownloader pd = new PageDownloader(new File(args[0]));
//		PageDownloader pd = new PageDownloader("https://www.topristorazione.com/frigoriferi-vino/3544-espositore-verticale-vino-vetri-fume-3-porte-verticali-temperatura-520c-modello-vinity3v.html");
//		PageDownloader pd = new PageDownloader("https://www.tuttoandroid.net/");
		Renderer rend = new Renderer(pd);
		rend.rendering();
	}
}
