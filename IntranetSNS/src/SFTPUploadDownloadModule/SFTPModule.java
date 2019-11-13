package SFTPUploadDownloadModule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : SFTPUploadDownloadModule
패키지 버전 : 1.0.0
클래스 이름 : SFTPModule
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- SFTP서버에 업로드와 다운로드를 관리

사용한 외부 라이브러리
- jsch-0.1.55.jar (SFTP서버를 사용할 수 있는 라이브러리를 제공)

패키지 버전 변경 사항
 */

/*
Jsch를 사용한 이유
현재 사용하고 있는 SFTP서버는 라즈베리파이의 SSH를 이용해 구현함.
그렇기에 일반 FTP가 아닌 SFTP를 사용하게 되었고 이를 가장 잘 지원하는 것이 Jsch이다.
 */
public class SFTPModule {
	String host, id, pw;
	int port;
	JSch jsch;
	Session session;
	Channel channel;
	ChannelSftp sftpChannel;
	
	//인스턴스 생성시에 서버의 주소, 포트, 접속 시 필요한 계정을 받아옴
	public SFTPModule(String host, int port, String id, String pw) {
		this.host = host;
		this.port = port;
		this.id = id;
		this.pw = pw;
	}
	
	//서버 연결 부분
	public void connect() throws JSchException {
		System.out.println("Connecting...." + this.host);
		jsch = new JSch();
		session = jsch.getSession(this.id, this.host, this.port);
		
		//서버의 호스트키가 변경되었거나 서버를 알 수 없는 경우 수행할 작업을 선택
		//yes : 연결 거부, ask : 키를 추가할지 변경할지 사용자가 선택, no : 항상 새 키를 삽입.
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(this.pw);
		session.connect();
		channel = session.openChannel("sftp");
		channel.connect();
		sftpChannel = (ChannelSftp) channel;
	}
	
	//서버 접속 종료
	public void disconnect() {
		if(session.isConnected()) {
			System.out.println("Disconnecting");
			sftpChannel.disconnect();
			channel.disconnect();
			session.disconnect();
		}
	}
	
	//파일 업로드
	//매개변수 : 파일 전체 경로 및 파일명, 업로드할 최하위 폴더
	//리턴하는건 파일명.
	//리턴된 값은 데이터베이스에 저장하는데 사용됨
	public String upload(String fileName, String remote) throws Exception {
		FileInputStream fis = null;
		File file = null;
		connect();	//SFTP서버 접속
		try {
			String remoteDir = new String();	//서버측의 경로를 저장하는 변수
			switch(remote) {
			case "images":	//업로드 타입이 이미지면(프로필 사진이면)
				remoteDir = "/home/pi/MySNS/www/images/";
				break;
			case "files":		//로드 타입이 파일이면(첨부 파일이면)
				remoteDir = "/home/pi/MySNS/www/files/";
				break;
			}
			sftpChannel.cd(remoteDir);	//서버 측의 채널위치를 지정한 경로로 변경
			file = new File(fileName);		//파일 전송에 쓰일 파일 객체 생성
			fis = new FileInputStream(file);
			sftpChannel.put(fis, file.getName());	//해당 채널에 파일을 전송하면서 해당 파일명으로 저장
			fis.close();
			System.out.println("파일 업로드 완료 - " + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();	//완료했으니 서버 접속 종료
		return file.getName();
	}
	
	//파일 다운로드
	//매개변수 : 서버에 저장된 파일명, 다운로드받을 전체 경로
	public void download(String fileName, String saveFile) throws Exception {
		byte[] buffer = new byte[1024];
		BufferedInputStream bis;	//파일을 받아올 변수
		connect();	//서버 접속
		try {
			String cdDir = "/home/pi/MySNS/www/files/";	//서버 측의 경로
			sftpChannel.cd(cdDir);
			File file = new File(cdDir + fileName);	//서버측 경로와 파일명을 조합해 파일 객체 생성
			bis = new BufferedInputStream(sftpChannel.get(file.getName()));	//파일을 받아옴
			File newFile = new File(saveFile);	//이 객체는 사용자 PC에 저장될 파일 객체
			OutputStream out = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(out);
			//이 부분은 파일을 가져와서 저장하는 부분
			int readCnt;
			while((readCnt = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCnt);
			}
			bis.close();
			bos.flush();
			bos.close();
			System.out.println("파일 다운로드 완료 - " + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();	//완료했으니 서버 접속 종료
	}
}