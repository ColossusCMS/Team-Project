package FTPUpload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPDownloader {
	public boolean receiveFTPServer(String ip, int port, String id, String password, String sourceFilePath, String targetFilePath) throws Exception {
		boolean result = false;
		FTPClient ftp = null;
		int reply = 0;
		FileOutputStream fos = null;
		try {
			ftp = new FTPClient();
			ftp.connect(ip, port);
			reply = ftp.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			if(!ftp.login(id, password)) {
				ftp.logout();
				return result;
			}
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			fos = new FileOutputStream(new File(targetFilePath));
			result = ftp.retrieveFile(sourceFilePath, fos);
			if(result) {
				System.out.println("Success");
			}
			else {
				System.out.println("Fail");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(ftp != null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e2) {}
			}
			if(fos != null) {
				fos.close();
			}
			ftp.logout();
		}
		return result;
	}
}
