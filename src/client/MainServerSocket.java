package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import threads.ClientThread;

public class MainServerSocket {

	private static ServerSocket serverSocket;

	private static List<ClientThread> arrayList = new ArrayList<ClientThread>();
	private static boolean mainFlag = true;
	
	static {
		try {
			serverSocket = new ServerSocket(1025);
			System.out.println("Server running...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<ClientThread> getArrayList() {
		return arrayList;
	}
	
	
	public static void main(String[] args) {
		
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {	
				
				System.out.println("Main board: Write 'exit' to close all threads and sockets.");
				
				Scanner scanner = new Scanner(System.in);
				String input;
				
				do {
					input = scanner.nextLine();
					
				} while( !"exit".equals(input) );
				
				synchronized(MainServerSocket.class){
					for (ClientThread thread : arrayList) {
						thread.closeSocketAndKillThread();
					}
					
				}
				
				try {
					if (serverSocket != null && !serverSocket.isClosed()) {
						serverSocket.close();
						
						// interrupting accept
						// SocketException
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}			
		}).start();
		
		while(mainFlag){
			
			try {
				
				System.out.println("Waiting for socket clients...");
				Socket socket = serverSocket.accept();
				System.out.println("client conectat...");
				ClientThread client = new ClientThread(socket);
				
				arrayList.add(client);
				
				client.start();
				
			} catch (SocketException se){
				System.out.println("Server has closed...");
				mainFlag = false;
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} 
			
		}
			
		
		
		
	}
}
