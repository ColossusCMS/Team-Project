package EncryptionDecryption;

import java.io.UnsupportedEncodingException;
/*
프로젝트 주제 : 사내 SNS
모듈 이름 : 비밀번호 암호화, 복호화
클래스 이름 : PasswordEncryption
버전 : 1.0.0
해당 클래스 작성 : 최문석

필요 전체 Java파일
- PasswordEncryption.java (로그인 화면이 실행되는 메인 클래스)

해당 클래스 주요 기능
- 비밀번호를 암호화하거나 복호화하는 메서드를 포함
 */
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
