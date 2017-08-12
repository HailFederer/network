package TCP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {
	private static final String SERVER_IP = "192.168.1.28";
	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) {
		
		Socket socket = null;
		try {
			// 1. 소켓 생성
			socket = new Socket();
			
			// 1-1. Socket Buffer Size 확인
			int receiveBufferSize = socket.getReceiveBufferSize();
			int sendBufferSize = socket.getSendBufferSize();
			System.out.println("receiveBufferSize : " + receiveBufferSize);
			System.out.println("sendBufferSize : " + sendBufferSize);
			
			// 1-2. Socket Buffer Size 변경
			socket.setReceiveBufferSize(10 * 1024);
			socket.setSendBufferSize(10 * 1024);
			receiveBufferSize = socket.getReceiveBufferSize();
			sendBufferSize = socket.getSendBufferSize();
			System.out.println("receiveBufferSize : " + receiveBufferSize);
			System.out.println("sendBufferSize : " + sendBufferSize);
			
			// 1-3 SO_NODELAY (Nagle Algorithm off)
			socket.setTcpNoDelay(true);
			
			// 1-4 SO_TIMEOUT
			socket.setSoTimeout(10);
			
			// 2. 서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			
			// 3. I/O 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			// 4. 쓰기/읽기
			String data = "hello";
			os.write(data.getBytes("UTF-8"));
			
			byte[] buffer = new byte[256];
			int readByteCount = is.read(buffer);
			if(readByteCount <= -1) {
				System.out.println("[Client] disconnection by server");
				return;
			}
			
			data = new String(buffer, 0, readByteCount, "UTF-8");
			System.out.println("[Client] received : "+data);
			
		} catch (ConnectException e) {		// 서버가 떠있지 않은 상황의 예외
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
