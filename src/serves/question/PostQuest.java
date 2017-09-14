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

import serves.tools.IsLogin;
import sql.MySQLInformation;
import sql.QuestTable;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class PostQuest
 */
@WebServlet("/PostQuest")
public class PostQuest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	QuestTable qt;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostQuest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		try {
			qt=new QuestTable(MySQLInformation.uri,MySQLInformation.account,MySQLInformation.password);
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
		request.setCharacterEncoding("utf-8");
		String id=IsLogin.isLogin(request);
		PrintWriter out=response.getWriter();
		JsonObject jo=new JsonObject();
		if(id==null){
			jo.addProperty("isOK", false);
			jo.addProperty("information", "please login");
			response.setStatus(500);
			out.println(jo.toString());
			return;
		}
		String title,detail,type;
		title=request.getParameter("title");
		detail=request.getParameter("detail");
		type=request.getParameter("type");
		try {
			ResultSet rs=qt.insert(id, title, detail, type);
			if(rs.next()){
				jo.addProperty("isOK", true);
				jo.addProperty("qid", rs.getString(1));
			}else{
				jo.addProperty("isOK", false);
			}
			out.println(jo.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jo.addProperty("isOK", false);
			response.setStatus(500);
			out.println(jo.toString());
		}
	}

}
