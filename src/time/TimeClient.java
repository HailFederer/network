package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TimeClient {
	private static String SERVER_IP = null;
	private static final int SERVER_PORT = 5000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		
		DatagramSocket socket = null;
		Scanner scanner = null;

		try {
			// 0. 키보드 연결
			scanner = new Scanner(System.in);
			
			SERVER_IP = InetAddress.getLocalHost().getHostAddress();
			
			socket = new DatagramSocket();
			
			while(true) {
				
				// 1. 사용자 입력
				System.out.print(">>");
				String message = scanner.nextLine();
				if("quit".equals(message)) {
					break;
				}
				
				// 2. 전송 패킷 생성
				byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
				
				DatagramPacket sendPacket = 
						new DatagramPacket(sendData, sendData.length, new InetSocketAddress(SERVER_IP, SERVER_PORT));
				
				// 3. 전송
				socket.send(sendPacket);
				
				// 4. 메세지 수신
				DatagramPacket receivePacket = 
						new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket); 	// block
				
				message = 
						new String(receivePacket.getData(), 0, 
								receivePacket.getLength(), StandardCharsets.UTF_8);
				System.out.println("<<" + message);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			{
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}
		}
	}

}
