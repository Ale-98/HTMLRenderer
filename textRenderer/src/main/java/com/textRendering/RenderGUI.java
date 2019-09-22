package com.textRendering;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class RenderGUI extends JFrame{

	private static final long serialVersionUID = 1L;

	private static final int BUTTON_VERTICAL_SIZE = 30;
	private static final int BUTTON_HORIZONTAL_SIZE = 150;
	private static final int TEXT_FIELD_HORIZONTAL_SIZE = 300;
	private static final int TEXT_FIELD_VERTICAL_SIZE = 30;
	
	private final String separator = "==========================================";

	private JLabel whereIsTheFile, whereToBoundFile;
	private JTextField fileField1, fileField2;
	private JButton start, browse1, browse2;

	public RenderGUI() {
		super("HTML Renderer");

		Container con = getContentPane();
		con.setLayout(new FlowLayout());

		whereIsTheFile = new JLabel("Paste URL or browse the file to render");
		con.add(whereIsTheFile);

		Dimension textFieldDimension = new Dimension(TEXT_FIELD_HORIZONTAL_SIZE, TEXT_FIELD_VERTICAL_SIZE);
		fileField1 = new JTextField();
		fileField1.setPreferredSize(textFieldDimension);
		con.add(fileField1);

		browse1 = new JButton("Browse");
		browse1.setPreferredSize(textFieldDimension);
		browse1.addActionListener(new BrowseButtonHandler());
		con.add(browse1);
		
		con.add(new JLabel(separator));
		
		whereToBoundFile = new JLabel("Choose where to save the file");
		con.add(whereToBoundFile);

		fileField2 = new JTextField();
		fileField2.setPreferredSize(textFieldDimension);
		con.add(fileField2);

		browse2 = new JButton("Browse");
		browse2.setPreferredSize(textFieldDimension);
		browse2.addActionListener(new BrowseButtonHandler());
		con.add(browse2);
		
		con.add(new JLabel(separator));
		
		Dimension buttonDimension = new Dimension(BUTTON_HORIZONTAL_SIZE, BUTTON_VERTICAL_SIZE);
		start = new JButton("Start");
		start.setPreferredSize(buttonDimension);
		start.addActionListener(new StartButtonHandler());
		start.setAlignmentX(LEFT_ALIGNMENT);
		con.add(start);

		setVisible(true);
		setSize(30*11, 30*10);
	}

	// Handlers----------------------------------------------------------
	private class StartButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			PageDownloader pd = null;
			File f = new File(fileField1.getText());
			if(f.isFile()) {
				try {
					pd = new PageDownloader(f);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "File not found");
					e1.printStackTrace();
				}
			} else {
				try {
					pd = new PageDownloader(fileField1.getText());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "URL doesn't exists"); 
					e1.printStackTrace();
				}
			}
			Renderer theRenderer = new Renderer(pd, fileField2.getText());
			theRenderer.rendering();
			JOptionPane.showMessageDialog(null, "Done! The result is here:"+fileField2.getText()); 
		}
	}
	
	private class BrowseButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT Files", "txt");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       if(e.getSource().equals(browse1)) {
		    	   fileField1.setText(chooser.getSelectedFile().getAbsolutePath());
		       }else {
		    	   fileField2.setText(chooser.getSelectedFile().getAbsolutePath());
		       }
		    }
		}
		
	}

	public static void main(String[] args) {
		RenderGUI application = new RenderGUI();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}


