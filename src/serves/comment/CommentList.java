package serves.comment;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql.CommentTable;
import sql.MySQLInformation;
import sql.UserTable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class CommentList
 */
@WebServlet("/CommentList")
public class CommentList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommentTable ct;
	private UserTable ut;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
			ct=new CommentTable(MySQLInformation.uri,MySQLInformation.account,MySQLInformation.password);
			ut=new UserTable(MySQLInformation.uri,MySQLInformation.account,MySQLInformation.password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		String aid=request.getParameter("aid");
		int page,amount=10;
		page=Integer.parseInt(request.getParameter("page"));
		try {
			ResultSet rs=ct.selectByAid(aid, page, amount);
			int n=0;
			JsonObject jo;
			JsonArray ja=new JsonArray();
			while(rs.next()){
				jo=new JsonObject();
				jo.addProperty("id", rs.getString("id"));
				jo.addProperty("uid", rs.getString("uid"));
				jo.addProperty("detail", rs.getString("detail"));
				jo.addProperty("post_time", rs.getString("post_time"));
				ResultSet trs=ut.selectById(rs.getString("uid"));
				if(trs.next()){
					jo.addProperty("name", trs.getString("name"));
				}else{
					jo.addProperty("name", "unknow");
				}
				ja.add(jo);
			}
			JsonObject rjo=new JsonObject();
			rjo.addProperty("isOK", true);
			rjo.add("comments",ja);
			out.println(rjo.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JsonObject rjo=new JsonObject();
			rjo.addProperty("isOK", false);
			response.setStatus(500);
			out.println(rjo.toString());
			e.printStackTrace();
		}
	}

}
