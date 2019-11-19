package ClassPackage;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ClassPackage
패키지 버전 : 1.0.0
클래스 이름 : DayOff
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 휴가 신청 창에서 데이터 베이스로 정보를 전달하기 위해 사용하는 클래스

패키지 버전 변경 사항
 */
public class DayOff {
	private String doUserNo;
	private String doStart;
	private String doEnd;
	private String doContent;

	public DayOff(String doUserNo, String doStart, String doEnd, String doContent) {
		this.doUserNo = doUserNo;
		this.doStart = doStart;
		this.doEnd = doEnd;
		this.doContent = doContent;
	}

	public String getDoUserNo() {
		return doUserNo;
	}

	public void setDoUserNo(String doUserNo) {
		this.doUserNo = doUserNo;
	}

	public String getDoStart() {
		return doStart;
	}

	public void setDoStart(String doStart) {
		this.doStart = doStart;
	}

	public String getDoEnd() {
		return doEnd;
	}

	public void setDoEnd(String doEnd) {
		this.doEnd = doEnd;
	}

	public String getDoContent() {
		return doContent;
	}

	public void setDoContent(String doContent) {
		this.doContent = doContent;
	}
}
