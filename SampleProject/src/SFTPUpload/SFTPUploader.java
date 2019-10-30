package SFTPUpload;

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

public class SFTPUploader {
	String host, id, pw;
	Integer port;
	JSch jsch;
	Session session;
	Channel channel;
	ChannelSftp sftpChannel;
	
	public SFTPUploader(String host, Integer port, String id, String pw) {
		this.host = host;
		this.port = port;
		this.id = id;
		this.pw = pw;
	}
	
	//접속부분
	public void connect() throws JSchException {
		System.out.println("connecting..." + host);
		jsch = new JSch();
		session = jsch.getSession(id, host, port);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(pw);
		session.connect();
		
		channel = session.openChannel("sftp");
		channel.connect();
		sftpChannel = (ChannelSftp) channel;
	}
	
	//접속 종료
	public void disconnect() {
		if(session.isConnected()) {
			System.out.println("disconnecting...");
			sftpChannel.disconnect();
			channel.disconnect();
			session.disconnect();
		}
	}
	
	//파일 업로드 부분
	public void upload(String fileName, String remoteDir) throws Exception {
		FileInputStream fis = null;
		connect();
		try {
			sftpChannel.cd(remoteDir);
			File file = new File(fileName);
			fis = new FileInputStream(file);
			sftpChannel.put(fis, file.getName());
			fis.close();
			System.out.println("File uploaded successfully - " + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();
	}
	
	//파일 다운로드 부분
	public void download(String fileName, String localDir) throws Exception {
		byte[] buffer = new byte[1024];
		BufferedInputStream bis;
		connect();
		try {
			String cdDir = fileName.substring(0, fileName.lastIndexOf("/") + 1);
			sftpChannel.cd(cdDir);
			File file = new File(fileName);
			bis = new BufferedInputStream(sftpChannel.get(file.getName()));
			File newFile = new File(localDir + "/" + file.getName());
			OutputStream os = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int readCount;
			while((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			bis.close();
			bos.close();
			System.out.println("File downloaded successfully - " + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();
	}
	
	public static void main(String[] args) {
		String localPath = "c:/MySNS/";
		String remotePathUp = "/home/pi/MySNS/UploadedFiles/Images";
		String remotePathDown = "/home/pi/MySNS/UploadedFiles/Images/";
		
//		String host = "192.168.219.14";
		String host = "125.185.21.163";
		Integer port = 2222;
		String id = "pi";
		String pw = "yaahq90";
		
		System.out.println("서버 접속 중");
		try {
			SFTPUploader sftpUploader = new SFTPUploader(host, port, id, pw);
			sftpUploader.upload(localPath + "미니어처.png", remotePathUp);
			sftpUploader.download(remotePathDown + "미니어처.png", localPath);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
