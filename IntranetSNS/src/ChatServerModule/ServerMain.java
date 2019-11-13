package ChatServerModule;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import InitializePackage.DataProperties;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ChatServerModule
패키지 버전 : 1.0.0
클래스 이름 : ServerMain
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 채팅 서버 클래스

패키지 버전 변경 사항
 */
public class ServerMain extends Application {
	public static ExecutorService threadPool;	//여러 쓰레드를 동시에 처리하기 위한 쓰레드풀 선언
	HashMap<String, HashMap<String, Client>> hashMapDept;
	TextArea textAreaLog = null;

	ServerSocket serverSocket = null;
	Socket socket = null;

	public ServerMain() {
		//각각의 채팅방을 하나로 담아두는 큰 방을 만듦
		hashMapDept = new HashMap<String, HashMap<String, Client>>();
		Collections.synchronizedMap(hashMapDept);
		
		//각 부서에 맞는 채팅방을 하나씩 생성
		HashMap<String, Client> dept1 = new HashMap<String, Client>();
		Collections.synchronizedMap(dept1);	//해시맵 동기화처리
		HashMap<String, Client> dept2 = new HashMap<String, Client>();
		Collections.synchronizedMap(dept2);
		HashMap<String, Client> dept3 = new HashMap<String, Client>();
		Collections.synchronizedMap(dept3);
		HashMap<String, Client> dept4 = new HashMap<String, Client>();
		Collections.synchronizedMap(dept4);
		HashMap<String, Client> dept5 = new HashMap<String, Client>();
		Collections.synchronizedMap(dept5);
		HashMap<String, Client> dept6 = new HashMap<String, Client>();
		Collections.synchronizedMap(dept6);
		HashMap<String, Client> dept7 = new HashMap<String, Client>();
		Collections.synchronizedMap(dept7);
		
		//각 부서의 이름과 생성하나 해시맵을 큰 방에 순서대로 넣음
		//영어로 사용하는 이유는 서버로 구동하는 라즈베리파이에서
		//한글 문자셋이 깨지는 현상이 발생해서 라즈베리파이에서 처리하기 위해선
		//부득이하게 영문 이름으로 변환하는 과정이 필요해짐
		hashMapDept.put("all", dept1);
		hashMapDept.put("dev", dept2);
		hashMapDept.put("opt", dept3);
		hashMapDept.put("hr", dept4);
		hashMapDept.put("sales", dept5);
		hashMapDept.put("design", dept6);
		hashMapDept.put("plan", dept7);
	}
	
	//서버 시작
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket(port);	//서버용 소켓 생성
		} catch (Exception e) {
			System.out.println("Exception in open server");
			e.printStackTrace();
			if(!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}
		//서버에서 쓰레드를 실행
		//서버로 접속하는 클라이언트들을 받아오는 쓰레드
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						socket = serverSocket.accept();		//클라이언트가 접속했을 때 accept를 하고 클라이언트의 소켓 정보를 받아옴
						Thread msr = new Client(socket);	//클라이언트 처리용 쓰레드 실행
						threadPool.submit(msr);				//쓰레드풀에 등록
						textAreaLog.appendText(socket.getInetAddress()+":"+socket.getPort() + "\n");//클라이언트 정보 (ip, 포트) 출력
					} catch (Exception e) {
						if(!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);	//현재 쓰레드 쓰레드풀에 등록
	}
	
	//서버 종료
	public void stopServer() {
		try {
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//서버 창 디자인
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		
		textAreaLog = new TextArea();
		textAreaLog.setEditable(false);
		textAreaLog.setFont(new Font(13));
		root.setCenter(textAreaLog);
		root.getCenter().prefWidth(300);
		
		Button btnServerOpen = new Button("Start");
		btnServerOpen.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(btnServerOpen, new Insets(1, 0, 0, 0));
		root.setBottom(btnServerOpen);
		
		String IP = "127.0.0.1";
		int port = DataProperties.getPortNumber("Server");
		
		btnServerOpen.setOnAction(event -> {
			if(btnServerOpen.getText().equals("Start")) {
				startServer(IP, port);
				Platform.runLater(() -> {
					String message = String.format("[Start Server]\n", IP, port);
					textAreaLog.appendText(message);
					btnServerOpen.setText("Stop");
				});
			}
			else {
				stopServer();
				Platform.runLater(() -> {
					String message = String.format("[Stop Server]\n", IP, port);
					textAreaLog.appendText(message);
					btnServerOpen.setText("Start");
				});
			}
		});
		Scene scene = new Scene(root, 200, 500);
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Chat Server");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
		new ServerMain();	//해시맵을 생성하는 인스턴스
	}
	
	//클라이언트를 처리하는 쓰레드
	class Client extends Thread {
		Socket socket;		//클라이언트 소켓을 담아두는 변수
		DataInputStream in;	//클라이언트로부터 받아오는 스트림
		DataOutputStream out;	//클라이언트로 보내는 스트림
		String name = "";			//클라이언트의 이름
		String dept = "";			//클라이언트의 부서
		
		public Client(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			HashMap<String, Client> clientMap = null;	//현재의 클라이언트가 저장되어 있는 해시맵
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				name = in.readUTF();	//클라이언트의 이름을 받아옴
				dept = in.readUTF();	//클라이언트의 부서를 받아옴
				clientMap = hashMapDept.get(dept);	//받아온 부서를 가지고 해당하는 해시맵으로 생성
				clientMap.put(name, this);				//클라이언트의 이름과 소켓 정보를 해시맵에 저장
				
				//받아온 메시지를 처리하는 부분
				while(in != null) {
					String msg = in.readUTF();
					sendMsg(dept, msg);
				}
			} catch (Exception e) {
				System.out.println("--> " + e);
			} finally {
				if(clientMap != null) {
					clientMap.remove(name);
				}
			}
		}
		
		//해당 그룹 안의 사용자들에게 메시지를 보냄
		public void sendMsg(String dept, String msg) {
			//해당 부서의 값인 해시맵을 가져옴(유저들이 저장되어 있음)
			HashMap<String, Client> clientMap = hashMapDept.get(dept);
			//반복자에 해당 부서의 값인 해시맵의 키(유저이름)를 저장
			Iterator<String> user = hashMapDept.get(dept).keySet().iterator();
			//해당 부서에 접속한 유저들을 하나씩 가져와서
			while(user.hasNext()) {
				try {
					//새로운 클래스에 하나씩 가져옴.
					Client client = clientMap.get(user.next());
					client.out.writeUTF(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
