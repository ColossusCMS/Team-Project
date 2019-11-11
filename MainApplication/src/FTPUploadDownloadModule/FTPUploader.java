package FTPUploadDownloadModule;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import InitializePackage.DataProperties;

public class FTPUploader {
	//FTP서버로 파일을 업로드하는 메서드
	public static boolean uploadFTPServer(String ftpPath, String localPath, String file) {
		String ip = DataProperties.ipAddress("FTPServer");
		int port = DataProperties.portNumber("FTPServer");
		String id = DataProperties.idProfile("FTPServer");
		String pw = DataProperties.password("FTPServer");
		
		boolean isSuccess = false;
		FTPClient ftp = null;
		int reply;
		try {
			ftp = new FTPClient();
			ftp.setControlEncoding("UTF-8");
			ftp.connect(ip, port);
			reply = ftp.getReplyCode();
			
			if(!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.exit(1);
			}
			if(!ftp.login(id, pw)) {
				ftp.logout();
				throw new Exception("FTP서버 로그인 실패");
			}
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			
			//여기서 경로가 갈림
			ftp.changeWorkingDirectory(ftpPath);
			
			String sourceFile = localPath + file;
			File uploadFile = new File(sourceFile);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(uploadFile);
				isSuccess = ftp.storeFile(file, fis);
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = false;
			} finally {
				if(fis != null) {
					try {
						fis.close();
					} catch (Exception e2) {}
				}
			}
			ftp.logout();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ftp != null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (Exception e2) {}
			}
		}
		return isSuccess;
	}
	
	//매개변수로 받는거
	//전송할 파일이 image인지, file인지 받음
	//파일을 선택하고 생성된 File 객체
	public static String uploadFile(String type, File localFile) {
		String ftpFilesPath = new String();
		if(type.equals("image")) {
			ftpFilesPath = "uploadedfiles/images/";
		}
		else if(type.equals("file")) {
			ftpFilesPath = "uploadedfiles/files/";
		}
		String localPath = localFile.getParent();	//파일명을 제외한 경로 + /필요
		String fileName = localFile.getName();		//파일명만 가져옴
		uploadFTPServer("html/" + ftpFilesPath, localPath + "/", fileName);
		
		ftpFilesPath += blankReplace(fileName);
		return ftpFilesPath;//변환된 주소를 반환
	}
	
	//글자 공백 변환하는거
	public static String blankReplace(String str) {
		return str.replaceAll(" ", "%20");
	}
}
