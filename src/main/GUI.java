package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class GUI extends JFrame {
	
	JLabel lblServerStatusDisplay;
	Server MinecraftServer;
	BufferedReader serverFeedback;
	
	private JPanel contentPane;
	private JTextField ramMaxInputField;
	private JButton btnServerStop;
	private JButton btnServerStart;
	private JTextArea outputField;
	private JLabel ramErrorField;

	/**
	 * Create the frame.
	 */
	public GUI(Server passThrough) {
		setResizable(false);
		this.MinecraftServer = passThrough;
		
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setTitle("Server Tool");
		setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\Documents\\GitHub\\Eclipse-Workspace\\workspace\\MinecraftServerWatchdogTool\\app-icon.ico"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(2, 0, 431, 261);
		contentPane.add(tabbedPane);
		
		JPanel ActionMenu = new JPanel();
		tabbedPane.addTab("Actions", null, ActionMenu, "This menu shows actions directly related to the server.");
		ActionMenu.setLayout(null);
		
		JLabel lblServerStatus = new JLabel("Server Status:");
		lblServerStatus.setBounds(10, 9, 101, 29);
		ActionMenu.add(lblServerStatus);
		
		lblServerStatusDisplay = new JLabel("Not Started");
		lblServerStatusDisplay.setBounds(100, 9, 316, 29);
		ActionMenu.add(lblServerStatusDisplay);
		
		btnServerStart = new JButton("Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!ramMaxInputField.getText().equals("")) {
					btnServerStop.setEnabled(true);
					btnServerStart.setEnabled(false);
					// ToDo: make so program doesnt auto close without saving
					setServerStatusDisplay("Please wait... Starting");
					StartMinecraftServer();
				} else { 
					ramErrorField.setForeground(new Color(255, 0, 0));
					ramErrorField.setText("Enter max ram as Ex. 2000M, 2G");
				}

			}
		});
		btnServerStart.setForeground(new Color(0, 128, 0));
		btnServerStart.setBounds(10, 190, 150, 32);
		ActionMenu.add(btnServerStart);
		
		btnServerStop = new JButton("Stop");
		btnServerStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MinecraftServer.sendInput("Stop");
			}
		});
		btnServerStop.setEnabled(false);
		btnServerStop.setForeground(new Color(255, 0, 0));
		btnServerStop.setBounds(266, 190, 150, 32);
		ActionMenu.add(btnServerStop);
		
		JLabel lblSetRamMax = new JLabel("Set Ram Max:");
		lblSetRamMax.setBounds(10, 49, 101, 29);
		ActionMenu.add(lblSetRamMax);
		
		ramMaxInputField = new JTextField("");
		ramMaxInputField.setBounds(100, 49, 101, 29);
		ActionMenu.add(ramMaxInputField);
		ramMaxInputField.setColumns(10);
		
		ramErrorField = new JLabel("");
		ramErrorField.setBounds(211, 49, 205, 29);
		ActionMenu.add(ramErrorField);
		
		JScrollPane LogOutput = new JScrollPane();
		tabbedPane.addTab("Log", null, LogOutput, "Shows direct server log/console");
		
		outputField = new JTextArea();
		outputField.setEditable(false);
		LogOutput.setViewportView(outputField);
	}
	
	private void StartMinecraftServer() {
		try {
			MinecraftServer.setRam(ramMaxInputField.getText());
			MinecraftServer.setFileLocation("C:/Users/Aaron/Desktop/Minecraft_Servers/minecraft_1.14.1/server.jar");
			MinecraftServer.setWorkingDirectory("C:/Users/Aaron/Desktop/Minecraft_Servers/minecraft_1.14.1/");
			new Thread(() -> {
				serverFeedback = MinecraftServer.Start();
				startLogBuffer();
			}).start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
	}
	
	private void startLogBuffer() {
		try {
			String line;
			while((line = serverFeedback.readLine()) != null) {
				getServerStatus(line);
				sendToLogTextField(line + "\n");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void sendToLogTextField(String writeText) {
		outputField.append(writeText);
	}
	
	private void setServerStatusDisplay(String displayText) {
		lblServerStatusDisplay.setText(displayText);
	}
	
	private void getServerStatus(String linetext) {
		if(linetext.contains("Done")) {
			setServerStatusDisplay("Server Running");
		}
	}
}
