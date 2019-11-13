package SendMail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import InitializePackage.DataProperties;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : SendMail
패키지 버전 : 1.0.0
클래스 이름 : SendMail
해당 클래스 작성 : 최문석, 심대훈

해당 클래스 주요 기능
- 계정 찾기를 원하는 사용자에게 안내 메일을 보내기 위해 smtp서버에 접속
- 데이터베이스에서 해당 사용자의 정보를 받아와서 사용자의 메일로 해당 정보를 송신

사용한 외부 라이브러리
- mail-1.4.7.jar

패키지 버전 변경 사항
 */

/*
mail.jar을 사용한 이유
자바에서 smtp서버에 접속해 메일을 발송하는 기능을 구현하기 위해
 */
public class SendMail {
	private final String id = DataProperties.getIdProfile("MailServer");
	private final String pw = DataProperties.getPassword("MailServer");
	String to;
	String name;
	String userNo;
	String userPw;
	Properties props = null;
	
	public SendMail(String to, String name, String userNo, String userPw) {	//보낼 메일 주소와 사용자 이름
		this.to = to;
		this.name = name;
		this.userNo = userNo;
		this.userPw = userPw;
		
		//구글smtp에 접속하는 코드
		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", 465);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		this.send();
	}
	
	public void send() {
		//해당 smtp서버에서 인증을 받아오는 부분
		//이 부분에서 인증을 받아오지 못하면 메일 발송을 할 수 없다.
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(id, pw);
			}
		});
		
		try {
			//메일로 전송할 메시지를 작성하는 부분
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.id));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.to));
			
			//제목부분
			message.setSubject("아이디와 비밀번호 안내입니다.");
			
			//내용부분
			String bodyMsg = "안녕하세요. " + name + "님!\nSNS 관리자입니다.\n문의하신 사원번호와 비밀번호 안내입니다.\n\n"
					+ "사원번호 : " + userNo + "\n비밀번호 : " + userPw + "\n\n오늘도 좋은 하루 되세요.\n감사합니다.";
			message.setText(bodyMsg);
			
			Transport.send(message);	//메일 전송
			System.out.println("메일을 성공적으로 보냈습니다.");
		} catch (MessagingException e) {
			System.out.println("실패");
			e.printStackTrace();
		}
	}
}
