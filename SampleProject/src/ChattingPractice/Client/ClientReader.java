package ChattingPractice.Client;

import java.io.InputStream;
import java.net.Socket;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ClientReader extends Thread {
	Socket clientSocket = null;
	TextArea sendMsg, recMsg;
	Button login, logout;
	
	public ClientReader(Socket clientSocket, TextArea sendMsg, TextArea recMsg, Button login, Button logout) {
		this.clientSocket = clientSocket;
		this.sendMsg = sendMsg;
		this.recMsg = recMsg;
		this.login = login;
		this.logout = logout;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				InputStream inputStream = clientSocket.getInputStream();
				byte[] byteArray = new byte[256];
				int size = inputStream.read(byteArray);
				String readMessage = new String(byteArray, 0, size, "UTF-8");
				if(readMessage.equals("FFFF")) {
					sendMsg.clear();
					recMsg.clear();
					readMessage = "서버가 종료되었습니다.";
					login.setDisable(false);
					logout.setDisable(true);
				}
				recMsg.appendText(readMessage + "\n");
			}
		} catch (Exception e) {	}
	}
}
