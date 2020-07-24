package org.openjfx.DirecT.Connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.openjfx.DirecT.Commands.WindowsCommands;

public class Connection {

	public static ServerSocket serverSocket;// will be used by the sender in the laptop to laptop commincation
	public static Socket socket;

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
		// is=socket.getInputStream();
		is = new DataInputStream(socket.getInputStream());

	}

	public static void connectToServer() throws Exception {

		String ip = WindowsCommands.hotspotIP();
		System.out.println(ip + "hello");
		boolean connected = true;
		/*
		 * while(connected) { try{ socket=new Socket(ip,4444); connected=false;
		 * 
		 * }catch(Exception e) { try{ socket.close(); }catch(Exception e2) {
		 * 
		 * } System.out.println("Inside connectTorServer"); } }
		 */
		
		socket=new Socket();   
		socket.connect(new InetSocketAddress(ip,4444),1200000); 
		socket.setSoTimeout(120000);
	//	socket = new Socket(ip, 4444);
		
		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
		bos = new DataOutputStream(socket.getOutputStream());
		// is=socket.getInputStream();
		is = new DataInputStream(socket.getInputStream());

	}

}
