package ChattingPractice.Client;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

public class ClientController implements Initializable {
	@FXML private Button login, logout, sendFile, send;
	@FXML private Label serverState;
	@FXML private TextArea sendMsg, recMsg;
	
	Scanner input = new Scanner(System.in);
	Socket clientSocket = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		recMsg.setEditable(false);
		login.setOnAction(event -> login());
		logout.setOnAction(event -> logout());
		sendFile.setOnAction(event -> sendFile());
		send.setOnAction(event -> send());
		logout.setDisable(true);
		sendMsg.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.ENTER) {
				send();
			}
		});
	}
	
	public void sendFile() {
		
	}
	
	public void login() {
		try {
			clientSocket = new Socket();
			clientSocket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 9991));
			serverState.setText("로그인 성공!");
			login.setDisable(true);
			logout.setDisable(false);
			
			ClientReader clientReader = new ClientReader(clientSocket, sendMsg, recMsg, login, logout);
			clientReader.start();
		} catch (Exception e) {	}
	}
	
	public void logout() {
		try {
			serverState.setText("로그아웃!");
			login.setDisable(false);
			logout.setDisable(true);
			clientSocket.close();
		} catch (Exception e) {	}
	}
	
	public void send() {
		try {
			String sendMessage = sendMsg.getText();
			byte[] byteArray = sendMessage.getBytes("UTF-8");
			OutputStream outputStream = clientSocket.getOutputStream();
			outputStream.write(byteArray);
		} catch (Exception e) {	}
		sendMsg.clear();
	}
}
