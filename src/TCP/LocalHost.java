package TCP;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {

		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostName = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			byte[] addresses = inetAddress.getAddress();
			
			System.out.println(hostName);			
			System.out.println(hostAddress);
			for(byte b : addresses) {
				System.out.print((b & 0x000000ff)+".");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
