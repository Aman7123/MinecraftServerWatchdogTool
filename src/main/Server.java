package main;

import java.io.*;

public class Server {
	private String ramMax = null;
	private String fileLocation = null;
	private String workingDirectory = null;
	private BufferedReader log = null;
	
	private Process serverProcess;
	private BufferedWriter out;
	
	

	public Server() {
		System.out.println("Initialized Server Class");
	}
	
	public void setRam(String max) {
		System.out.println("Set ram: " + max);
		this.ramMax = max;
	}
	
	public void setFileLocation(String location) {
		System.out.println("Set file location: " + location);
		this.fileLocation = location;
	}
	
	public void setWorkingDirectory(String directory) {
		System.out.println("Set working directory: " + directory);
		this.workingDirectory = directory;
	}
	
	public BufferedReader Start() {
		if((ramMax != null) && (fileLocation != null) && (workingDirectory != null)) {
			try {
				serverProcess = new ProcessBuilder("java", "-Xmx"+ramMax, "-jar", fileLocation, "-nogui").directory(new File(workingDirectory)).start();
				log = new BufferedReader(new InputStreamReader(serverProcess.getInputStream())); 
				out = new BufferedWriter(new OutputStreamWriter(serverProcess.getOutputStream()));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		} else {
			System.out.println("Server variables not initialized");
			System.out.println("ram= " + ramMax + ", " + 
								"fileLocation= " + fileLocation + ", " +
								"workingDirectory= " + workingDirectory);
		}
		return log;
	}
	
	public void Stop() throws IOException {
		serverProcess.destroy();
	}
	
	
	//Cannot get this to work
	public void sendInput(String command) {
		try {
			out.write(command + "\n");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	

}
 