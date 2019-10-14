package ChattingPractice.Server;

import java.net.Socket;

public class Joiner {
	Socket UserInfo_socket;
	
	public Joiner(Socket socketNumber) {
		UserInfo_socket = socketNumber;
	}
}
