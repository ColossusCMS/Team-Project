package ChattingPractice.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javafx.scene.control.TextField;

public class ConnectThread extends Thread {
	ServerSocket mainServerSocket;
	TextField recMsg;
	List<Joiner> list;
	
	public ConnectThread(ServerSocket mainServerSocket, TextField recMsg, List<Joiner> list) {
		this.mainServerSocket = mainServerSocket;
		this.recMsg = recMsg;
		this.list = list;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				Socket serverSocket = mainServerSocket.accept();
				recMsg.setText("사용자 접속!");
				list.add(new Joiner(serverSocket));
				ServerReader serverReader = new ServerReader(serverSocket, list, recMsg);
				serverReader.start();
			}
		} catch (Exception e) {	}
	}
}
