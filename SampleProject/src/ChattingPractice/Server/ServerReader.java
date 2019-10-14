package ChattingPractice.Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javafx.scene.control.TextField;

public class ServerReader extends Thread {
	Socket serverSocket = null;
	List<Joiner> list;
	TextField recMsg;
	
	public ServerReader(Socket serverSocket, List<Joiner> list, TextField recMsg) {
		this.serverSocket = serverSocket;
		this.list = list;
		this.recMsg = recMsg;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				InputStream inputStream = serverSocket.getInputStream();
				byte[] byteArray = new byte[256];
				int size = inputStream.read(byteArray);
//				if(size == -1) {		//if : logout
//					for(int i = 0; i < list.size();) {
//						if(serverSocket == list.get(i).UserInfo_socket) {
//							list.remove(i);
//						}
//						else {
//							i++;
//						}
//					}
//					break;
//				}
				String readMsg = new String(byteArray, 0, size, "UTF-8");
				recMsg.setText(readMsg);
				try {
					String sendMessage = readMsg;
					byte[] byteArray1 = sendMessage.getBytes("UTF-8");
					for(int i = 0; i < list.size(); i++) {
						OutputStream outputStream = list.get(i).UserInfo_socket.getOutputStream();
						outputStream.write(byteArray1);
					}
				} catch (Exception e) {	}
			}
		} catch (Exception e) {	}
	}
}
