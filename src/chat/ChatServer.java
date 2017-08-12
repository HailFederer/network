package chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChatServer {
	
	private static final int PORT = 5000;
	private Vector<ChatHandler> handlers;
	private ServerSocket serverSocket;
	
	public ChatServer(int port){
		
		try {
			handlers = new Vector<ChatHandler>();
			serverSocket = new ServerSocket();
			
			// set option SO_REUSEADDR ( 종료 후 빨리 바인딩)
			serverSocket.setReuseAddress(true);
			
			String localhost = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhost, PORT));
			System.out.println("ChatServer is ready.");
			
			while(true){
				Socket clientSocket = serverSocket.accept();
				ChatHandler chatHandler = new ChatHandler(this, clientSocket);
				register(chatHandler);
				chatHandler.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ChatHandler getHandler(int index){
		return handlers.elementAt(index);
	}
	
	public void register(ChatHandler c){
		handlers.addElement(c);
	}
	
	public void unregister(ChatHandler c){
		handlers.removeElement(c);
	}
	
	public void broadcast(String message){
		
		String joinedClient = null;
		String chattingClient = null;
		String msg = null;
		StringTokenizer toWhispersTokens = null;
		ArrayList<String> toWhispersList = null;
		
		StringTokenizer tokens = new StringTokenizer(message, ":");
		String protocol = tokens.nextToken();
		
		if("JOIN".equals(protocol)) {
			joinedClient = tokens.nextToken();
		} else if("MSG".equals(protocol)) {
			chattingClient = tokens.nextToken();
			msg = tokens.nextToken();
		} else if("WHISPER".equals(protocol)) {
			chattingClient = tokens.nextToken();
			String toWhispers = tokens.nextToken();
			msg = tokens.nextToken();
			toWhispersTokens = new StringTokenizer(toWhispers, ",");
			
			toWhispersList = new ArrayList<String>();
			while(toWhispersTokens.hasMoreTokens()) {
				toWhispersList.add(toWhispersTokens.nextToken());
			};
		}
		
		synchronized (handlers) {
			
			for(int i=0; i<handlers.size(); i++){
				
				ChatHandler ChatHandler = handlers.elementAt(i);
				if(joinedClient != null && joinedClient.equals(ChatHandler.userName)) {
					ChatHandler.println(joinedClient + "님 환영합니다!");
					joinedClient = null;
					continue;
				}
				try {
					if("MSG".equals(protocol)) {
						ChatHandler.println(chattingClient +" : "+ msg);
					} else if("WHISPER".equals(protocol)){
												
						if(toWhispersList.contains(ChatHandler.userName)) {
							ChatHandler.println(chattingClient +" : "+ msg);
						}
					} else if("JOIN".equals(protocol)){
						ChatHandler.println(joinedClient + "님이 입장하셨습니다.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		
		ChatServer server = new ChatServer(5001);
	}
}






