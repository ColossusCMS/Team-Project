package ChatClientModule;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import InitializePackage.DataProperties;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ChatClientModule
패키지 버전 : 1.0.0
클래스 이름 : ChatController
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 채팅 클라이언트 컨트롤러

패키지 버전 변경 사항
 */
public class ChatController implements Initializable {
	@FXML
	Button btnSend;
	@FXML
	TextArea txtArea;
	@FXML
	TextField txtFieldInput;
	@FXML
	Label lblRoomTitle;

	Socket socket;
	String ip = DataProperties.getIpAddress(); // 채팅 서버 IP주소를 담아두는 변수
	public static String name;
	public static String dept;
	int port = DataProperties.getPortNumber("Client");
	String deptKor; // 부서명을 한글로 변환하기 위한 변수

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		startClient(ip, port, name);
		Platform.runLater(() -> txtArea.appendText("[채팅방 접속]\n"));

		// 서버로부터 부서명을 받아왔을 때 영문으로 된 이름을 한글로 바꾸는 과정
		switch (dept) {
		case "all":
			deptKor = "전체";
			break;
		case "dev":
			deptKor = "개발";
			break;
		case "opt":
			deptKor = "경영";
			break;
		case "hr":
			deptKor = "인사";
			break;
		case "sales":
			deptKor = "영업";
			break;
		case "design":
			deptKor = "디자인";
			break;
		case "plan":
			deptKor = "기획";
			break;
		}
		lblRoomTitle.setText(deptKor); // 현재 접속중인 채팅방 이름(부서명)

		btnSend.setOnAction(event -> {
			send(name + ": " + txtFieldInput.getText() + "\n");
			txtFieldInput.setText("");
			txtFieldInput.requestFocus();
		});

		txtFieldInput.setOnAction(event -> {
			send(name + ": " + txtFieldInput.getText() + "\n");
			txtFieldInput.setText("");
			txtFieldInput.requestFocus();
		});
	}

	// 클라이언트 실행
	public void startClient(String ip, int port, String name) {
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(ip, port);
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					dos.writeUTF(name); // 사용자 이름을 서버로 전송
					dos.writeUTF(dept); // 사용자가 선택한 채팅방이름(부서명)을 서버로 전송
					receive();
				} catch (Exception e) {
					if (!socket.isClosed()) {
						stopClient();
						txtArea.appendText("[서버 접속 실패]\n");
						Platform.exit();
					}
				}
			}
		};
		thread.start();
	}

	// 접속 종료
	public void stopClient() {
		try {
			if (!socket.isClosed() && socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 서버로부터 메시지를 받아오는 메서드
	public void receive() {
		while (true) {
			try {
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				String msg = dis.readUTF();
				Platform.runLater(() -> txtArea.appendText(msg));
			} catch (Exception e) {
				stopClient();
				break;
			}
		}
	}

	// 서버로 메시지를 보내는 메서드
	public void send(String msg) {
		Thread thread = new Thread() {
			public void run() {
				try {
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					dos.writeUTF(msg);
					dos.flush();
				} catch (Exception e) {
					stopClient();
				}
			}
		};
		thread.start();
	}
}