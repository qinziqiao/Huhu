package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AttentionTable {

	private Connection conn;
	/**
	 * ����attention_table
	 * @param uri ���ݿ�uri���硱jdbc:mysql://localhost:3306/database"
	 * @param account ���ݿ��˺�
	 * @param password ���ݿ�����
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public AttentionTable(String uri,String account,String password) throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(uri, account, password);
	}
	/**
	 * ��ѯĳ���û���ע�˵���
	 * @param id
	 * @return id��ע�˵�att_id��
	 * @throws SQLException
	 */
	public ResultSet selectById(String id) throws SQLException{
		String sql="select distinct att_id from attention_table where id='"+id+";";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * 返回偶像信息
	 * @param id
	 * @return
	 */
	public ResultSet selectInformById(String id)throws SQLException{
		String sql="select id as att_id,name,photo from user_table"
				+ " where id in(select distinct att_id from attention_table "
				+ "where id="+id+");";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	/**
	 * ��ѯĳ�û���ע���˵�����
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public int attetionAmount(String id) throws SQLException{
		String sql="select count(distinct att_id)  from attention_table where id='"+id+"';";
		Statement stmt = conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		if(rs.next()){
			return rs.getInt(1);
		}else{
			return 0;
		}
	}
	/**
	 * ��ѯ��ע��ĳ���û�����
	 * @param att_id
	 * @return ��ע��att_id��id��
	 * @throws SQLException
	 */
	public ResultSet selectByAtt_id(String att_id) throws SQLException{
		String sql="select distinct id from attention_table where att_id='"+att_id+"';";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	
	/**
	 * 通过att_id 获得粉丝的信息
	 * @param att_id
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectInformByAtt_id(String att_id) throws SQLException{
		String sql="select id,name,photo from user_table"
				+ " where id in(select distinct id from attention_table "
				+ "where att_id="+att_id+");";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	
	/**
	 * ��ѯ��ע��ĳ�û�������
	 * @param att_id
	 * @return
	 * @throws SQLException
	 */
	public int beAttAmount(String att_id) throws SQLException{
		String sql="select count(distinct id) from attention_table where att_id='"+att_id+"';";
		Statement stmt = conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		if(rs.next()){
			return rs.getInt(1);
		}else{
			return 0;
		}
	}
	/**
	 * ��ѯid�Ƿ��ע��att_id
	 * @param id
	 * @param att_id
	 * @return ��ע����Ϊtrue
	 * @throws SQLException
	 */
	public boolean idAttAtt_id(String id,String att_id) throws SQLException{
		String sql="select * from attention_table where att_id='"+att_id+"' and id='"+id+"';";
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql).next();
	}
	/**
	 * ��������
	 * @param id
	 * @param att_id
	 * @return �Ѵ���ʱ����0
	 * @throws SQLException
	 */
	public int insert(String id,String att_id) throws SQLException{
		if(idAttAtt_id(id, att_id)){
			return -1;
		}
		String sql="insert into attention_table values("+id+","+att_id+");";
		Statement stmt = conn.createStatement();
		return stmt.executeUpdate(sql);
	}
	/**
	 * ɾ������
	 * @param id
	 * @param att_id
	 * @return
	 * @throws SQLException
	 */
	public int delete(String id,String att_id) throws SQLException{
		String sql="SET SQL_SAFE_UPDATES = 0;";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		sql="delete from attention_table where id="+id+" and att_id="+att_id+";";
		return stmt.executeUpdate(sql);
	}
}
