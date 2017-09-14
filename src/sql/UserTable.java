package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserTable {

	private Connection conn;
	/**
	 * ����user_table
	 * @param uri ���ݿ�uri���硱jdbc:mysql://localhost:3306/database"
	 * @param account ���ݿ��˺�
	 * @param password ���ݿ�����
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public UserTable(String uri,String account,String password) throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(uri, account, password);
	}
	/**
	 * ����û�id
	 * @param account �˺�
	 * @param password ����
	 * @return �û�id
	 * @throws SQLException
	 */
	public ResultSet selectByAccountPassword(String account,String password) throws SQLException{
		String sql="select id from user_table where account='"+account+"' and password='"+password+"';";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ��ö�Ӧ�û�id��������Ϣ
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectById(String id) throws SQLException{
		String sql="select * from user_table where id='"+id+"';";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ������û�
	 * @param account
	 * @param password
	 * @param type
	 * @param name
	 * @return �ɹ�����0���˺��Ѵ��ڷ���1�������Ѵ��ڷ���2
	 * @throws SQLException
	 */
	public int insert(String account,String password,String type,String name,String photo) throws SQLException{
		String sql="select * from user_table where account='"+account+"';";
		Statement stmt = conn.createStatement();
		if(stmt.executeQuery(sql).next()){
			//�˺��Ѵ���
			return 1;
		}
		sql="select * from user_table where name='"+name+"';";
		if(stmt.executeQuery(sql).next()){
			//�����Ѵ���
			return 2;
		}
		sql="insert into user_table(account,password,type,name,photo) values('"+account+"','"+password+"','"+type+"','"+name+"','"+photo+"');";
		stmt.executeUpdate(sql);
		return 0;
	}
	
	/**
	 * 关闭连接
	 */
	public void CloseConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
