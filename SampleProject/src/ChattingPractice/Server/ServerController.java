package ChattingPractice.Server;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class ServerController implements Initializable {
	@FXML private Button openServer, closeServer;
	@FXML private Label serverState;
	@FXML private TextField sendMsg, recMsg;
	
	List<Joiner> list = new ArrayList<Joiner>();
	ServerSocket mainServerSocket = null;
	
	public ServerController() {
		System.out.println("서버 컨트롤러 생성자 체크");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		recMsg.setEditable(false);
		sendMsg.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER) {
				try {
					String sendMessage = sendMsg.getText();
					byte[] byteArray = sendMessage.getBytes("UTF-8");
					for(int i = 0; i < list.size(); i++) {
						OutputStream outputStream = list.get(i).UserInfo_socket.getOutputStream();
						outputStream.write(byteArray);
					}
				} catch (Exception e) {	}
				sendMsg.clear();
			}
		});
		openServer.setOnAction(event -> open());
		closeServer.setOnAction(event -> close());
		closeServer.setDisable(true);
	}
	
	public void open() {
		try {
			mainServerSocket = new ServerSocket();
			mainServerSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 9991));
			serverState.setText("서버 오픈!");
			closeServer.setDisable(false);
			openServer.setDisable(true);
			
			ConnectThread connectThread = new ConnectThread(mainServerSocket, recMsg, list);
			connectThread.start();
		} catch (Exception e) {	}
	}
	
	public void close() {
		try {
			sendMsg.clear();
			recMsg.clear();
			serverState.setText("서버 종료");
			closeServer.setDisable(true);
			openServer.setDisable(false);
			
			String send = "FFFF";
			byte[] byteArray = send.getBytes("UTF-8");
			for(int i = 0; i < list.size(); i++) {
				OutputStream outputStream = list.get(i).UserInfo_socket.getOutputStream();
				outputStream.write(byteArray);
			}
			list.clear();
			mainServerSocket.close();
		} catch (Exception e) {	}
	}
}
