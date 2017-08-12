package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ChatHandler extends Thread{
	
	String userName = "";
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	private ChatServer server;
	
	public ChatHandler(ChatServer server, Socket socket) throws IOException{
		
		this.socket = socket;
		this.server = server;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
	}

	@Override
	public void run() {
		
		try {
			String joinMSG = br.readLine();
			
			StringTokenizer StringTokenizer = new StringTokenizer(joinMSG, ":");
			
			String protocol = StringTokenizer.nextToken();
			userName = StringTokenizer.nextToken();

			broadcast(protocol +":"+ userName);
			
			while(true){
				StringTokenizer tokens;
				
				String msg = br.readLine();
				tokens = new StringTokenizer(msg, ":");
				if(tokens.countTokens() == 2) {
					broadcast("WHISPER:" + userName + ":" + msg);
				} else {
					broadcast("MSG:" + userName + ":" + msg);
				}
			}
		} catch (IOException e) {
			System.out.println(userName + "님이 나가셨습니다.");
			e.printStackTrace();
		}
		server.unregister(this);
		broadcast(userName + "님이 나가셨습니다.");
		try {
			br.close();
			pw.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void println(String message){
		pw.println(message);
	}
	
	public void broadcast(String message){
		server.broadcast(message);
	}
}

