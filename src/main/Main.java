package main;

import java.awt.EventQueue;

public class Main {

	public static void main(String[] args) {
		Server MinecraftServer = new Server();
		GUI frame = new GUI(MinecraftServer);
		frame.setVisible(true);

	}

}
