package DBConnectionPractice;

import java.util.List;

public class sampleDB_Connection {
	
	public static void main(String[] args) {
	
		LoginDao dao = new LoginDao();
        //전체출력
        List<User> list = dao.slectAll();
        for(int i=0;i<list.size();i++){
        	User u = list.get(i);
        	System.out.println(u.toString());
        }
	}
}
