package sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class test {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		String uri = "jdbc:mysql://127.0.0.1:3306/zhihu";
		String sql_user="root";
		String sql_password="qinyangyang147";
		
		UserLabelMapTable UserLabelMapTable = new UserLabelMapTable(uri,sql_user, sql_password);
		if(UserLabelMapTable.setULMT(100, 20))
			System.out.println("插入成功");
		else
			System.out.println("插入失败");
		
		ResultSet rs=UserLabelMapTable.getULMT(100);
		if(!rs.next())
			System.out.println("空的");
		else {
			System.out.println("查找成功");
		}
	}

}
