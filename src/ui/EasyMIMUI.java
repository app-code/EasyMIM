package ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import DataStructures.EasyMIMConfig;
import Server.EasyMIMServer;

public class EasyMIMUI {
	
	
	private static EasyMIMServer server;
	/**
	 * 
	 */
	private static final long serialVersionUID = -6517723169863451705L;
	/** The default width of the Window Frame */
	private static final int WIDTH = 400;
	/** The default height of the Window Frame */
	private static final int HEIGHT = 300;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createFrame();
			}
		});
		
	}
	
	public static void createFrame() {				
		
		final JFrame frame = new JFrame("Provide information");
		
		frame.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				try {
					server.terminateServer();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}});
		
		JPanel pane = new JPanel(new BorderLayout());
		
		
		URL imageURL = EasyMIMUI.class.getResource("/image/images.jpeg");
		JLabel icon = new JLabel();
		icon.setIcon(new ImageIcon(imageURL));
		JPanel logo = new JPanel();
		JLabel prompt = new JLabel("EasyMIM: HACK ME NOW");
		logo.add(prompt);
		logo.add(icon);
		
		JLabel targetURL = new JLabel("Target URL");
		final JTextField targetURLField = new JTextField();
		targetURLField.setToolTipText("Enter the site you want to attack");

		JLabel swapImageLabel = new JLabel("Swap image");
		//JButton chooseImageBttn = new JButton("Choose Image");
		final JTextField urlImageTextField = new JTextField();
		urlImageTextField.setToolTipText("Enter the image url");
		urlImageTextField.setPreferredSize(urlImageTextField.getPreferredSize());
		
		JLabel videoLabel = new JLabel("Force YouTube Video to Play");
		//JButton chooseImageBttn = new JButton("Choose Image");
		final JTextField videoTextField = new JTextField();
		videoTextField.setToolTipText("Enter the the youtube video embedded url");
		videoTextField.setPreferredSize(videoTextField.getPreferredSize());
		
		JLabel popUpLabel = new JLabel("Popup message");
		final JTextField popUpInput = new JTextField();
		popUpInput.setToolTipText("Enter website pop up message");
		
		JLabel enableKeyLogger = new JLabel("Key logger");
		final JCheckBox isKeyLogging = new JCheckBox();
		
		JLabel enableCredentialHavest = new JLabel("Save credential");
		final JCheckBox isSaveCredential = new JCheckBox();
		
		JButton submit = new JButton("Submit");
		
		submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(server!=null){
					try {
						server.terminateServer();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				EasyMIMConfig config = new EasyMIMConfig();
				config.targetURL = new ArrayList<String>();
				config.targetURL.add(targetURLField.getText());
				config.imageURL = urlImageTextField.getText();
				config.keylogger = isKeyLogging.isEnabled();
				config.saveCred = isSaveCredential.isEnabled();
				config.popUpMessage = popUpInput.getText();
				config.youtubeURL = videoTextField.getText();
				server = new EasyMIMServer(config);
				Thread t = new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							server.startServer();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}});
				t.start();
				
			}});

		
		pane.add(logo, BorderLayout.PAGE_START);
		
		JPanel MITMOption = new JPanel();
		GroupLayout MITMLayout = new GroupLayout(MITMOption);
		MITMOption.setLayout(MITMLayout);
		MITMLayout.setAutoCreateGaps(true);

		MITMLayout.setAutoCreateContainerGaps(true);

		// Create a sequential group for the horizontal axis.

		GroupLayout.SequentialGroup hGroup = MITMLayout
				.createSequentialGroup();
		hGroup.addGroup(MITMLayout.createParallelGroup()
				.addComponent(targetURL).addComponent(swapImageLabel)
				.addComponent(videoLabel).
				addComponent(popUpLabel).addComponent(enableKeyLogger).
				addComponent(enableCredentialHavest));
		
		hGroup.addGroup(MITMLayout.createParallelGroup()
				.addComponent(targetURLField)
				.addGroup(MITMLayout.createSequentialGroup()/*.addComponent(chooseImageBttn)*/.addComponent(urlImageTextField))
				.addComponent(videoTextField)
				.addComponent(popUpInput)
				.addComponent(isKeyLogging)
				.addComponent(isSaveCredential));
		MITMLayout.setHorizontalGroup(hGroup);

		// Create a sequential group for the vertical axis.
		GroupLayout.SequentialGroup vGroup = MITMLayout
				.createSequentialGroup();
		vGroup.addGroup(MITMLayout.createParallelGroup(Alignment.BASELINE)
				.addComponent(targetURL).addComponent(targetURLField));
		vGroup.addGroup(MITMLayout.createParallelGroup(Alignment.BASELINE)
				.addComponent(swapImageLabel)
				/*.addComponent(chooseImageBttn)*/.addComponent(urlImageTextField));
		vGroup.addGroup(MITMLayout.createParallelGroup(Alignment.BASELINE).addComponent(videoLabel).addComponent(videoTextField));
		vGroup.addGroup(MITMLayout.createParallelGroup(Alignment.BASELINE)
				.addComponent(popUpLabel).addComponent(popUpInput));
		vGroup.addGroup(MITMLayout.createParallelGroup(Alignment.BASELINE)
				.addComponent(enableKeyLogger).addComponent(isKeyLogging));
		vGroup.addGroup(MITMLayout.createParallelGroup(Alignment.BASELINE)
				.addComponent(enableCredentialHavest).addComponent(isSaveCredential));
		MITMLayout.setVerticalGroup(vGroup);
		MITMOption.setPreferredSize(new Dimension(600, 300));
		pane.add(MITMOption, BorderLayout.CENTER);

		JPanel clickPane = new JPanel();
		clickPane.add(submit);
		pane.add(clickPane, BorderLayout.PAGE_END);
		frame.setContentPane(pane);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

	}
}
