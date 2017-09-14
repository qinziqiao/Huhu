package sql;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class QuestLabelMapTable {
	private Connection conn;
	public QuestLabelMapTable(String uri,String sqlUser,String Password) throws SQLException, ClassNotFoundException {
		//连接数据库
		Class.forName("com.mysql.jdbc.Driver");
		conn = (Connection) DriverManager.getConnection(uri, sqlUser, Password);
	}
	/**
	 * 
	 * @param qid
	 * @param lid
	 * @return 是否插入map表成功
	 * @throws SQLException
	 */
	public boolean setQLMT(int qid,int lid) throws SQLException{
		if (qid <0||lid<0)
			return false;
		//0.首先看是否已存在
		String judgeSQL=String.format("select * FROM questlabelmap_table where (qid=%d and lid=%d)", qid,lid);
		PreparedStatement sqlStatement0  = conn.prepareStatement(judgeSQL);
		ResultSet rs= sqlStatement0.executeQuery();
		//如果为空
		if(!rs.next()){
			//1.插入数据库
			String insertSQL ="insert into questlabelmap_table(qid,lid) values(?,?)";
			PreparedStatement sqlStatement  = conn.prepareStatement(insertSQL);
			sqlStatement.setInt(1, qid);
			sqlStatement.setInt(2, lid);
		
			int status = sqlStatement.executeUpdate();
			if(status!=0 )
				return true;
			else
				return false;
		}
		else 
			return true;
		
	}
	
	/**
	 * 
	 * @param qid
	 * @return 查询问题返回的结果集
	 * @throws SQLException
	 */
	public ResultSet getQLMT(int qid) throws SQLException{

		
		ResultSet rs;
		String selectSQL=String.format("select * from questlabelmap_table where qid=%d", qid);
		System.out.println(selectSQL);
		PreparedStatement sqlStatement = conn.prepareStatement(selectSQL);
		rs = sqlStatement.executeQuery();
		
		return  rs;
	}
	
	/**
	 * 
	 * @param lid
	 * @param index
	 * @param amount
	 * @throws SQLException
	 * @return标签下指定数目的问题
	 */
	public ResultSet getLimitAmountQuestByLabel(int lid , int page) throws SQLException{
		ResultSet rs;
		int amount=10;
		String selectSQL=String.format("select * from questlabelmap_table where lid=%d limit %d,%d", lid,page*amount,amount);
		System.out.println(selectSQL);
		PreparedStatement sqlStatement = conn.prepareStatement(selectSQL);
		rs = sqlStatement.executeQuery();
		
		return  rs;
	}
	/**
	 * 
	 * @param lid
	 * @return 由问题得到所有标签
	 * @throws SQLException
	 */
	public ResultSet getQuestByLabel(int lid) throws SQLException{
		
		ResultSet rs;
		String selectSQL=String.format("select * from questlabelmap_table where lid=%d", lid);
		System.out.println(selectSQL);
		PreparedStatement sqlStatement = conn.prepareStatement(selectSQL);
		rs = sqlStatement.executeQuery();
		
		return  rs;
	}
	
	public boolean deleteQLMT(int qid ,int lid) throws SQLException{
		PreparedStatement sqlStatement;
		String safeSQL= "set SQL_SAFE_UPDATES=0";
		sqlStatement= conn.prepareStatement(safeSQL);
		sqlStatement.executeUpdate();
		
		String deleteSQL = String.format("delete from questlabelmap_table  where qid=%d and lid=%d",qid,lid);
		 sqlStatement  = conn.prepareStatement(deleteSQL);
		int status = sqlStatement.executeUpdate();
		
		if(status!=0 )
			return true;
		else
			return false;
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
