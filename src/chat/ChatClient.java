package chat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ChatClient implements ActionListener{
	
	private String userName;
	private BufferedReader br;
	private PrintWriter pw;
	private Socket clientSocket;
	private String server;
	
	private Frame frame;	// 전체 윈도우
	private Panel pannel;	// 판떼기
	private Button buttonSend;	// send 버튼
	private TextField textField;
	private TextArea textArea;
	
	public ChatClient(String server, String userName) {
		
		this.server = server;
		this.userName = userName;

		frame = new Frame(userName);
		pannel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);
		textArea.setEditable(false);
	}

	public static void main(String[] args) {

		String server = null;
		try {
			server = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		System.out.print("채팅 아이디를 입력하세요 : ");
		Scanner scanner = new Scanner(System.in);
		String userName = scanner.nextLine();
		
		ChatClient chatClient = new ChatClient(server, userName);
		
		ChatClientReceiveThread cc = chatClient.new ChatClientReceiveThread();
		cc.start();
		
		chatClient.show();
	}
	
	public class ChatClientReceiveThread extends Thread{

		@Override
		public void run() {
			
			try {
				clientSocket = new Socket(server, 5000);
				br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
				pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
				
				pw.println("JOIN:"+userName);
				
				while(true){
					String line = br.readLine();
					textArea.append(line + "\n");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		sendMessage();
	}
	
	private void sendMessage() {

		pw.println(textField.getText());
		textField.setText("");
		textField.requestFocus();		
	}
	
	public void show() {
		
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent actionEvent ) {
				sendMessage();
			}
		});
		

		// Textfield
		textField.setColumns(80);
		textField.addKeyListener( new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if (keyCode == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});

		// Pannel
		pannel.setBackground(Color.LIGHT_GRAY);
		pannel.add(textField);
		pannel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, pannel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.pack();
	}
}
