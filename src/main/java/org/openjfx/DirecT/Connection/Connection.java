package org.openjfx.DirecT.Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.openjfx.DirecT.Commands.WindowsCommands;
import org.openjfx.DirecT.Controller.FileSelection;

public class Connection {

	public static ServerSocket serverSocket;// will be used by the sender in the laptop to laptop commincation
	public static Socket socket;
	public static boolean receiverConnected = false;
	public static boolean senderConnected = false;

	public static DataOutputStream dos;
	public static DataInputStream dis;
	public static DataOutputStream bos;
	public static DataInputStream is;

	public static void openServer() throws Exception {

		serverSocket = new ServerSocket(4444);
		socket = serverSocket.accept();

		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
		bos = new DataOutputStream(socket.getOutputStream());
		is = new DataInputStream(socket.getInputStream());

		senderConnected = true;
		receiverConnected = true;

	}

	public static void connectToServer() throws Exception {

		boolean connected = true;

		socket = new Socket();
		socket.connect(new InetSocketAddress("192.168.137.1", 4444), 1200000);
		socket.setSoTimeout(120000);

		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
		bos = new DataOutputStream(socket.getOutputStream());
		is = new DataInputStream(socket.getInputStream());
		receiverConnected = true;
		FileSelection.connectionEstablished = true;

	}

}
