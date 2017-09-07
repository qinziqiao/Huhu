package serves.comment;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serves.tools.IsLogin;
import sql.CommentTable;
import sql.MySQLInformation;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class PostComment
 */
@WebServlet("/PostComment")
public class PostComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommentTable ct;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostComment() {
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
		String id=IsLogin.isLogin(request);
		PrintWriter out=response.getWriter();
		JsonObject jo=new JsonObject();
		if(id==null){
			jo.addProperty("isOK", false);
			jo.addProperty("information", "please login");
			out.println(jo.toString());
			return;
		}

		String aid,detail;
		aid=request.getParameter("aid");
		detail=request.getParameter("detail");
		try {
			ct.insert(aid, id, detail);
			jo.addProperty("isOK", true);
			out.println(jo.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jo.addProperty("isOK", false);
			out.println(jo.toString());
		}
	}

}
