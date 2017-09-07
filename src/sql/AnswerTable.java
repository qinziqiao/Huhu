package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AnswerTable {

	private Connection conn;
	/**
	 * ����answer_table
	 * @param uri ���ݿ�uri���硱jdbc:mysql://localhost:3306/database"
	 * @param account ���ݿ��˺�
	 * @param password ���ݿ�����
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public AnswerTable(String uri,String account,String password) throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(uri, account, password);
	}
	/**
	 * ��������
	 * @param qid quest��id
	 * @param uid user��id
	 * @param detail
	 * @return
	 * @throws SQLException
	 */
	public int insert(String qid,String uid,String detail) throws SQLException{
		// 没有检测重复回答  1.联合唯一索引 2.程序自己检测 3.修改数据库复合主键
		String sql="insert into answer_table(qid,uid,detail) values("+qid+","+uid+",'"+detail+"');";
		Statement stmt=conn.createStatement();
		int rt=stmt.executeUpdate(sql);
		if(rt!=0){
			String sql2="update quest_table set answer_sum= answer_sum +1 where id = "+qid;
			stmt=conn.createStatement();
			stmt.executeUpdate(sql2);
		}
		return rt;
	}
	/**
	 * ����id��ѯanswer
	 * @param aid answer��id
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectById(String aid) throws SQLException{
		String sql="select * from answer_table where id="+aid+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ��ѯĳ�û��Ļش�
	 * @param uid user��id
	 * @param page
	 * @param amountPerPage
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectByUid(String uid,int page,int amountPerPage) throws SQLException{
		String sql="select * from answer_table where uid="+uid+" order by post_time desc limit "+page*amountPerPage+","+amountPerPage+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ��ѯĳquest�µ�answer
	 * @param qid quest��id
	 * @param page
	 * @param amountPerPage
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectByQid(String qid,int page,int amountPerPage) throws SQLException{
		String sql="select * from answer_table where qid="+qid+" order by post_time desc limit "+page*amountPerPage+","+amountPerPage+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ��ѯĳ�û���ע��user��answer
	 * @param uid �û���id
	 * @param page
	 * @param amountPerPage
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectByAttention(String uid,int page,int amountPerPage) throws SQLException{
		String sql="select * from answer_table where uid in (select att_id from attention_table where id="+uid+") order by post_time desc limit "+page*amountPerPage+","+amountPerPage+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
}
