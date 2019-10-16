package EncryptionDecryption;

import java.io.UnsupportedEncodingException;

public class PasswordEncryption {
	//비밀번호를 암호화하는 메서드
	public static String pwEncryption(String plainPw) {
		String cipherPw = new String();
		byte[] encByte;
		try {
			encByte = plainPw.getBytes("UTF-8");
			for(int i = 0; i < encByte.length / 2; i++) {
				byte tmp = 0;
				tmp = encByte[i];
				encByte[i] = encByte[encByte.length - (i + 1)];
				encByte[encByte.length - (i + 1)] = tmp;
				for(int j = 0; j < encByte.length; j++) {
					encByte[i] += 1;
				}
			}
			cipherPw = new String(encByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return cipherPw;
	}
	
	//비밀번호를 복호화하는 메서드
	public static String pwDecryption(String cipherPw) {
		String plainPw = new String();
		byte[] decByte;
		try {
			decByte = cipherPw.getBytes("UTF-8");
			for(int i = 0; i < decByte.length / 2; i++) {
				for(int j = 0; j < decByte.length; j++) {
					decByte[i] -= 1;
				}
				byte tmp = 0;
				tmp = decByte[i];
				decByte[i] = decByte[decByte.length - (i + 1)];
				decByte[decByte.length - (i + 1)] = tmp;
			}
			plainPw = new String(decByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return plainPw;
	}
}
