package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
	private static final int PORT = 5000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {

		DatagramSocket socket = null;
		
		try {
			socket = new DatagramSocket(PORT);
			
			while(true) {
				
				DatagramPacket receivePacket =
						new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				
				socket.receive(receivePacket);
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
				String data = simpleDateFormat.format(new Date());
				
				byte[] sendData = data.getBytes(StandardCharsets.UTF_8);
				DatagramPacket sendPacket = 
						new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
				
				socket.send(sendPacket);
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("연결 종료");
	}
}
