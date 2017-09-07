package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CommentTable {

	private Connection conn;
	/**
	 * ����comment_table
	 * @param uri ���ݿ�uri���硱jdbc:mysql://localhost:3306/database"
	 * @param account ���ݿ��˺�
	 * @param password ���ݿ�����
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public CommentTable(String uri,String account,String password) throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(uri, account, password);
	}
	/**
	 * ��������
	 * @param aid
	 * @param uid
	 * @param detail
	 * @return
	 * @throws SQLException
	 */
	public int insert(String aid,String uid,String detail) throws SQLException{
		String sql="insert into comment_table(aid,uid,detail) values("+aid+","+uid+",'"+detail+"');";
		Statement stmt=conn.createStatement();
		int result = stmt.executeUpdate(sql);
		if(result!=0){
			sql="update answer_table set comment_sum=comment_sum+1 where id="+aid+";";
			stmt.executeUpdate(sql);
		}
		return result;
	}
	/**
	 * ��ѯĳanswer�µ�comment
	 * @param aid answer��id
	 * @param page
	 * @param amountPerPage
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectByAid(String aid,int page,int amountPerPage) throws SQLException{
		String sql="select * from comment_table where aid="+aid+" order by post_time desc limit "+page*amountPerPage+","+amountPerPage+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ��ѯĳ�û�������comment
	 * @param uid user��id
	 * @param page
	 * @param amountPerPage
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectByUid(String uid,int page,int amountPerPage) throws SQLException{
		String sql="select * from comment_table where uid="+uid+" order by post_time desc limit "+page*amountPerPage+","+amountPerPage+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
}
