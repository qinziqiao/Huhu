package sql;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class UserLabelMapTable {
	private Connection conn;
	public UserLabelMapTable(String uri,String sqlUser,String Password) throws SQLException, ClassNotFoundException {
		//连接数据库
		Class.forName("com.mysql.jdbc.Driver");
		conn = (Connection) DriverManager.getConnection(uri, sqlUser, Password);
	}
	
	/**
	 * 设置标签
	 * @param uid
	 * @param lid
	 * @return 
	 * @throws SQLException
	 */
	public boolean setULMT(int uid,int lid) throws SQLException{
		if(uid<0||lid<0)
			return false;
		//0.首先看是否已存在
		String judgeSQL=String.format("select * FROM zhihu.userlabelmap_table where (uid=%d and lid=%d)", uid,lid);
		PreparedStatement sqlStatement0  = conn.prepareStatement(judgeSQL);
		ResultSet rs= sqlStatement0.executeQuery();
		//如果为空
		if(!rs.next()){
			//1.插入数据库
			String insertSQL ="insert into zhihu.userlabelmap_table(uid,lid) values(?,?)";
			PreparedStatement sqlStatement  = conn.prepareStatement(insertSQL);
			sqlStatement.setInt(1, uid);
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
	 * @param uid
	 * @return 获得用户关注标签
	 * @throws SQLException
	 */
	public ResultSet getULMT(int uid) throws SQLException{
		
		ResultSet rs;
		String selectSQL=String.format("select * from userlabelmap_table where uid=%d", uid);
		PreparedStatement sqlStatement = conn.prepareStatement(selectSQL);
		rs = sqlStatement.executeQuery();
			
		return  rs;
	}
	
	/**
	 * 
	 * @param lid
	 * @param page
	 * @return 根据标签获得指定数目的用户
	 * @throws SQLException
	 */
	public ResultSet getLimitAmountUserByLabel(int lid , int page) throws SQLException{
		ResultSet rs;
		int amount=10;
		String selectSQL=String.format("select * from userlabelmap_table where lid=%d limit %d,%d", lid,page*amount,amount);
		System.out.println(selectSQL);
		PreparedStatement sqlStatement = conn.prepareStatement(selectSQL);
		rs = sqlStatement.executeQuery();
		
		return  rs;
	}
	
	/**
	 * 
	 * @param lid
	 * @return 根据标签获得所有用户
	 * @throws SQLException
	 */
	public ResultSet getUserByLabel(int lid) throws SQLException{
		
		ResultSet rs;
		String selectSQL=String.format("select * from userlabelmap_table where lid=%d", lid);
		System.out.println(selectSQL);
		PreparedStatement sqlStatement = conn.prepareStatement(selectSQL);
		rs = sqlStatement.executeQuery();
		
		return  rs;
	}
	
		
	public boolean deleteULMT(int uid ,int lid) throws SQLException{
		PreparedStatement sqlStatement;
		String safeSQL= "set SQL_SAFE_UPDATES=0";
		sqlStatement= conn.prepareStatement(safeSQL);
		sqlStatement.executeUpdate();
		
		
		String deleteSQL = String.format("delete from userlabelmap_table  where uid=%d and lid=%d",uid,lid);
	    sqlStatement  = conn.prepareStatement(deleteSQL);
		int status = sqlStatement.executeUpdate();
		
		if(status!=0 )
			return true;
		else
			return false;
	}
}
