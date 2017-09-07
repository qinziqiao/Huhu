package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AttentionPage {

	private Connection conn;
	/**
	 * 访问attention_table
	 * @param uri 数据库uri，如”jdbc:mysql://localhost:3306/database"
	 * @param account 数据库账号
	 * @param password 数据库密码
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public AttentionPage(String uri,String account,String password) throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(uri, account, password);
	}
	
	
	public ResultSet selectPageByAttention(int uid,int page ,int amount) throws SQLException{
		//id,uid,title,detail,qtype,post_time, lower_level_sum,agree_sum
		String sql ="select id,uid,title,detail,qtype,post_time,answer_sum as lower_level_sum,'-1'as agree_sum from quest_table where uid in(select att_id from attention_table where id="
				+uid+ ")union all select id,uid,'-1' as title,detail,'-1'as qtype,post_time,comment_sum as lower_level_sum,agree_sum from answer_table where uid in(select att_id from attention_table where id="
				+uid+ ")order by post_time desc limit "+page*amount+","+amount+";";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	
}
