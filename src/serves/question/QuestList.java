package serves.question;

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

import sql.MySQLInformation;
import sql.QuestTable;
import sql.UserTable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 * Servlet implementation class QuestList
 */
@WebServlet("/QuestList")
public class QuestList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	QuestTable qt;
	UserTable ut;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
			qt=new QuestTable(MySQLInformation.uri, MySQLInformation.account, MySQLInformation.password);
			ut=new UserTable(MySQLInformation.uri, MySQLInformation.account, MySQLInformation.password);
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
//		String id=IsLogin.isLogin(request);
//		if(id==null){
//			JsonObject jo=new JsonObject();
//			jo.addProperty("isOK", false);
//			jo.addProperty("information", "please login");
//			out.println(jo.toString());
//			return;
//		}
		int type,page,amount=10;
		try {
			type=Integer.parseInt(request.getParameter("type"));
			page=Integer.parseInt(request.getParameter("page"));
			ResultSet rs=qt.selectOnePage(page, amount, type);
			int n=0;
			JsonObject jo;
			JsonArray ja=new JsonArray();
			while(rs.next()){
				jo=new JsonObject();
				jo.addProperty("id", rs.getString("id"));
				jo.addProperty("uid", rs.getString("uid"));
				jo.addProperty("title", rs.getString("title"));
				jo.addProperty("detail", rs.getString("detail"));
				jo.addProperty("post_time", rs.getString("post_time"));
				jo.addProperty("answer_sum", rs.getString("answer_sum"));
				ResultSet trs=ut.selectById(rs.getString("uid"));
				if(trs.next()){
					jo.addProperty("name", trs.getString("name"));
					jo.addProperty("photo", trs.getString("photo"));
				}else{
					jo.addProperty("name", "unknow");
					jo.addProperty("photo", "0");
				}
				ja.add(jo);
			}
			JsonObject rjo=new JsonObject();
			rjo.addProperty("isOK", true);
			rjo.add("quests",ja);
			out.println(rjo.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JsonObject rjo=new JsonObject();
			rjo.addProperty("isOK", false);
			response.setStatus(500);
			out.println(rjo.toString());
			//e.printStackTrace();
		}
	}

}
