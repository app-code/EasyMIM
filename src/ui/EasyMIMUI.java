package ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

public class EasyMIMUI {
	
	
	
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
		
		JPanel pane = new JPanel(new BorderLayout());
		
		
		URL imageURL = EasyMIMUI.class.getResource("/image/images.jpeg");
		JLabel icon = new JLabel();
		icon.setIcon(new ImageIcon(imageURL));
		JPanel logo = new JPanel();
		JLabel prompt = new JLabel("EasyMIM: HACK ME NOW");
		logo.add(prompt);
		logo.add(icon);
		
		JLabel targetURL = new JLabel("Target URL");
		JTextField targetURLField = new JTextField();
		targetURLField.setToolTipText("Enter the site you want to attack");

		JLabel swapImageLabel = new JLabel("Swap image");
		//JButton chooseImageBttn = new JButton("Choose Image");
		JTextField urlImageTextField = new JTextField();
		urlImageTextField.setToolTipText("Enter the image url");
		urlImageTextField.setPreferredSize(urlImageTextField.getPreferredSize());
		
		JLabel popUpLabel = new JLabel("Popup message");
		JTextField popUpInput = new JTextField();
		popUpInput.setToolTipText("Enter website pop up message");
		
		JLabel enableKeyLogger = new JLabel("Key logger");
		JCheckBox isKeyLogging = new JCheckBox();
		
		JLabel enableCredentialHavest = new JLabel("Save credential");
		JCheckBox isSaveCredential = new JCheckBox();
		
		JButton submit = new JButton("Submit");
		
		//submit.addActionListener(arg0)

		
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
				.addComponent(targetURL).addComponent(swapImageLabel).
				addComponent(popUpLabel).addComponent(enableKeyLogger).
				addComponent(enableCredentialHavest));
		
		hGroup.addGroup(MITMLayout.createParallelGroup()
				.addComponent(targetURLField)
				.addGroup(MITMLayout.createSequentialGroup()/*.addComponent(chooseImageBttn)*/.addComponent(urlImageTextField))
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
