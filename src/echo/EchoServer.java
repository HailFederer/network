package echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	private static final int SERVER_PORT = 6000;

	public static void main(String[] args) {

		ServerSocket serverSocket = null;
		
		try {
			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();
			
			// 2. 바인딩(Binding)
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress();
			
			serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));
			consoleLog(" binding "+localhostAddress+" : "+SERVER_PORT);
			
			// 3. 연결요청 기다림(accept)
			while(true) {
				Socket socket = serverSocket.accept();	// blocking
				new EchoServerReceiveThread(socket).start();
			}
			
		} catch (IOException e) {	// 서버소켓과 관련된 예외
			e.printStackTrace();
		} finally {
			{
				try {
					if(serverSocket != null && serverSocket.isClosed() == false) {
						serverSocket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void consoleLog(String log) {
		System.out.println("[Server : "+Thread.currentThread().getId()+" ] "+log);
	}
}
