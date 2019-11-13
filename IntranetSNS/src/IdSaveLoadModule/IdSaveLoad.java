package IdSaveLoadModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import EncryptionDecryption.PasswordEncryption;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : IdSaveLoadModule
패키지 버전 : 1.0.0
클래스 이름 : IdSaveLoad
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 현재 로그인한 사용자의 정보를 항상 가지고 있기 위해
- 설정한 경로에 사용자 번호를 텍스트 파일로 저장함.
- 저장할 때에는 사용자 번호를 암호화해서 저장하고 프로그램에서 가져올 때는 복호화해서 가져옴
- 프로그램이 완전히 종료되면 저장했던 사용자 번호를 지움.
 */

//로그인된 계정을 프로그램이 실행되는 동안에는 계속 가지고 있기 위해서
//사원번호를 txt파일로 지정한 폴더에 저장해둠.
//(로그아웃하거나) 프로그램을 완전히 종료하면 txt파일의 내용을 전부 지움.
public class IdSaveLoad {
	//로그인을 하고 나면 로그인된 사용자번호를 계속해서 가지고 있기 위해 저장함.
	public static void saveUserId(String id) {
//		String path = System.getProperty("user.home") + "/Documents/MySNS/id.txt";
//		String path = "e:/MySNS/id.txt";
//		File filePath = new File("e:/MySNS/");
//		File fileName = new File("e:/MySNS/id.txt");
		File filePath = new File("c:/MySNS/");
		File fileName = new File("c:/MySNS/id.txt");
		try {
			if(!filePath.exists()) {
				filePath.mkdirs();
			}
			if(!fileName.exists()) {
				fileName.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(fileName);
			Writer writer = new OutputStreamWriter(fos);
			//텍스트파일에 저장할 때 암호화해서 저장한다.
			id = PasswordEncryption.pwEncryption(id);
			writer.write(id);
			writer.flush();
			writer.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//사용자번호가 필요한 모듈이나 메서드에서 이 메서드를 호출해서 현재 로그인 중인 사용자의 번호를
	//언제든지 가져올 수 있음.
	public static String loadUserId() {
//		String path = System.getProperty("user.home") + "/Documents/MySNS/id.txt";
		String path = "c:/MySNS/id.txt";
//		String path = "e:/MySNS/id.txt";
		String id = new String();
		FileReader fr = null;
		BufferedReader br = null;
		StringWriter sw = null;
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			sw = new StringWriter();
			int ch = 0;
			while((ch = br.read()) != -1) {
				sw.write(ch);
			}
			br.close();
			//복호화해서 id를 가져옴
			id = PasswordEncryption.pwDecryption(sw.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	//로그아웃이나 프로그램을 완전히 종료할 경우 저장했던 사용자번호를 지우고 txt파일을 초기화함
	public static void resetUserId() {
//		String path = System.getProperty("user.home") + "/My Documents/MySNS/id.txt";
		String path = "c:/MySNS/id.txt";
		File file = new File(path);
		try {
			FileWriter fw = new FileWriter(file, false);
			fw.write("");
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
