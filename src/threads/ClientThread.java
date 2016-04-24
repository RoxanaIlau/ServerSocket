package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

import client.MainServerSocket;

public class ClientThread extends Thread{

	private Socket socket;
	private BufferedReader br;
	private PrintStream ps = null;
	private boolean flag = true;
	
	public ClientThread(Socket socket) throws IOException {
		this.socket = socket;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		ps = new PrintStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		
		String message;
		while(flag){
			try {
				message = br.readLine();
				if ("quit".equals(message)){
					flag = false;
					closeSocketAndKillThread();
					
				}
				
				System.out.println( Thread.currentThread().getName() + ": " + message );
				ps.println("Bravo!!");
			} catch (SocketException se){
				flag = false;
				closeSocketAndKillThread();
				synchronized(MainServerSocket.class){
					MainServerSocket.getArrayList().remove(this);
				}
				
				
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeSocketAndKillThread(){
		
		System.out.println("Inchidem socketul...");
		
		ps.close();
		try {
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		flag = false;
	}
	
}