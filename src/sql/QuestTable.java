package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuestTable {

	private Connection conn;
	/**
	 * ����quest_table
	 * @param uri ���ݿ�uri���硱jdbc:mysql://localhost:3306/database"
	 * @param account ���ݿ��˺�
	 * @param password ���ݿ�����
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public QuestTable(String uri,String account,String password) throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(uri, account, password);
	}
	/**
	 * ��������
	 * @param uid
	 * @param title
	 * @param detail
	 * @param qtype
	 * @return
	 * @throws SQLException
	 */
	public int insert(String uid,String title,String detail,String qtype) throws SQLException{
		String sql="insert into quest_table(uid,title,detail,qtype) values('"+uid+"','"+title+"','"+detail+"','"+qtype+"');";
		Statement stmt = conn.createStatement();
		return stmt.executeUpdate(sql);
	}
	/**
	 * ���һҳ������quest
	 * @param page ҳ�룬��0��ʼ
	 * @param amountPerPage ÿҳ��quest��
	 * @param qtype quest����
	 * @return
	 * @throws SQLException 
	 */
	public ResultSet selectOnePage(int page,int amountPerPage,int qtype) throws SQLException{
		String sql="select * from quest_table where qtype="+qtype+" order by post_time desc limit "+page*amountPerPage+","+amountPerPage+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ͨ��id����quest
	 * @param qid quest��id
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectById(String qid) throws SQLException{
		String sql="select * from quest_table where id="+qid+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ��ѯĳ�û�������quest
	 * @param uid
	 * @param page
	 * @param amountPerPage
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectByUid(String uid,int page,int amountPerPage) throws SQLException{
		String sql="select * from quest_table where uid="+uid+" order by post_time desc limit "+page*amountPerPage+","+amountPerPage+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ����ĳid��ע���˷�����quest
	 * @param id �û�id
	 * @param page ҳ��
	 * @param amountPerPage ÿҳ��quest��
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectByAttention(String id,int page,int amountPerPage) throws SQLException{
		String sql="select * from quest_table where uid in (select att_id from attention_table where id="+id+") order by post_time desc limit "+page*amountPerPage+","+amountPerPage+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ����ĳid��ע��label��ص�quest
	 * @param id �û�id
	 * @param page ҳ��
	 * @param amountPerPage ÿҳ��quest��
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectByUserLabel(String id,int page,int amountPerPage) throws SQLException{
		String sql="select * from quest_table where id in (select distinct qid from questlabelmap_table where lid in (select lid from userlabelmap_table where uid="+id+")) order by post_time desc limit "+page*amountPerPage+","+amountPerPage+";";
		Statement stmt=conn.createStatement();
		return stmt.executeQuery(sql);
	}
}
