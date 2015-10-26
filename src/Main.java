import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;

import com.jogamp.newt.Display;

public class Main {
	public static void main(String[] args){
		new Main();
	}
	
	CellApplet app;
	JEditorPane textArea;
	
	public Main(){
		JFrame bg = new JFrame();
		final JButton jb = new JButton("X");
		jb.setBackground(Color.BLACK);
		jb.setForeground(Color.WHITE);
		jb.setOpaque(true);
		jb.setBorderPainted(false);
		jb.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}}
		);
		jb.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		        jb.setBackground(Color.RED);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		        jb.setBackground(Color.BLACK);
		    }
		});
		bg.setUndecorated(true);
		bg.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.black);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(jb);
		bg.add(buttonPanel,BorderLayout.NORTH);
		bg.getContentPane().setBackground(Color.black);
		bg.setVisible(true);
		//APPLET FRAME
		JFrame appf = new JFrame();
		app = new CellApplet();
		app.init();
		appf.add(app);
		appf.setAlwaysOnTop(true);
		appf.setTitle("Cell Model");
		appf.setVisible(true);
		appf.setSize(960+10, 540+30);
		appf.setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth()/2-(970/2), GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight()/2-(570/2));
		//INFO FRAME
		JFrame infof = new JFrame();
		infof.setAlwaysOnTop(true);
		textArea = new JEditorPane();
		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		infof.setTitle("More information");
		infof.setSize(550, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
		infof.add(scroll);
		infof.setResizable(true);
		textArea.setContentType("text/html");
		textArea.setEditable(false);
		textArea.addHyperlinkListener(new CustomHyperlinkListener());
		infof.setVisible(true);
		//SHOWING EVERYTHING
		bg.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		fileToWebView(app.dataPath("") + "/info.html");
	}
	
	void fileToWebView(String path) {
		try {
			StringBuilder paneBuilder = new StringBuilder();
			Scanner s = new Scanner(new File(path));
			while (s.hasNext()) {
				paneBuilder.append(s.nextLine() + "\n");
			}
			textArea.setText(paneBuilder.toString().replace("{src}", "file:/" + app.dataPath("").replace("\\", "/") + "/"));
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static String stackTraceToString(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	class CustomHyperlinkListener implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				fileToWebView(app.dataPath("") + "/" + e.getDescription());
			}
		}
	}
}
