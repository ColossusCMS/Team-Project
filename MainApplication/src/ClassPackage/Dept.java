package ClassPackage;
/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 0.7.0
모듈 이름 : 클래스 패키지
모듈 버전 : 1.1.2
클래스 이름 : Dept
해당 클래스 작성 : 최문석

필요모듈 Java파일
- Dept.java

필요 import 사용자 정의 package
- 

해당 클래스 주요 기능
- 

모듈 버전 변경 사항
1.1.0
- DAO 인스턴스를 필요시에만 생성해 페이지 이동 간의 로딩 시간을 줄임.
- 사용자 등록창에서 이메일 중복체크 버튼 추가 및 이메일 중복체크 액션 추가
- LoginDao 클래스에 이메일 체크하는 메서드 추가
- 변수 및 메서드 이름 통일화

1.1.1
- Dao 인스턴스 통합 (데이터 베이스 초기화 클래스 생성)
- 콤보박스의 내용을 데이터베이스와 연동

1.1.2
- 사용자 직급, 상태 메시지용, 관리자확인 변수 추가
 */
public class Dept {
	private String deptNo;
	private String deptName;
	
	public Dept(String deptNo, String deptName) {
		this.deptNo = deptNo;
		this.deptName = deptName;
	}

	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
