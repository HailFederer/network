package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		String hostAddress = null;
		
		while(true) {
			
			System.out.print("> ");
			hostAddress = sc.nextLine();
			if("exit".equals(hostAddress)) {
				break;
			} else {
				try {
					InetAddress[] inetAddresses = InetAddress.getAllByName(hostAddress);// 호스트 이름을 여러 아이피에 바인딩..
																						// 목적 : 네임 서버에 들어오는 요청을 분산
					
					for(InetAddress inetAddress : inetAddresses) {
						System.out.println(hostAddress+" : "+inetAddress.getHostAddress());
					}
					
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}	
			}
		}
		
		
	}

}
