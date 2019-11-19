package ClassPackage;

/*
프로젝트 주제 : 사내 SNS
프로그램 버전 : 1.0.0
패키지 이름 : ClassPackage
패키지 버전 : 1.0.0
클래스 이름 : Dept
해당 클래스 작성 : 최문석

해당 클래스 주요 기능
- 부서 정보와 목록을 가져오는데 사용하는 클래스

패키지 버전 변경 사항
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
