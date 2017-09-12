package serves.homepage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serves.tools.IsLogin;
import serves.tools.GlobalVar.GlobalParameter;
import sql.AttentionPage;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class homepage
 */
@WebServlet("/homepage")
public class homepage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public homepage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 关注页按时间先后推送 关注人的quest和answer
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//		//登录检测
//		String request_uid_string =IsLogin.isLogin(request);
//		if(request_uid_string==null){
//			//TODO 这里有异常需要捕获
//			response.setStatus(500);
//			Response(response.getWriter(), false, null);
//			return;
//		}
		
		
		int request_uid = -1;
		int request_page = -1;
		boolean isOK = true;
		PrintWriter out = null;
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");

		// 查看请求
		try {
			request_uid = Integer.parseInt(request.getParameter("uid"));
			request_page = Integer.parseInt(request.getParameter("page"));
			out = response.getWriter();
		} catch (IOException e) {
			System.out.println("io异常");
			// TODO:如何处理该异常
			try {
				response.sendError(404, "您要查找的资源不存在");
			} catch (IOException e1) {
				System.out.println("IO异常");
			}
		} catch (NumberFormatException e) {
			System.out.println("所填数据异常");
			isOK = false;
			if (out != null){
				response.setStatus(500);
				Response(out, false, null);
			}
			return;
		}

		if (request_uid < 0) {
			response.setStatus(500);
			Response(out, false, null);
			return;
		}
		
		try {
			AttentionPage ap = new AttentionPage(GlobalParameter.uri,
					GlobalParameter.sql_user, GlobalParameter.sql_password);
			ResultSet rs = ap.selectPageByAttention(request_uid, request_page, 10);

			// 对结果集进行JSON解析
			if (rs.next() == false) {
				// 没有找到数据
				response.setStatus(501);
				Response(out, false, null);
			} else {
				rs.beforeFirst();
				Response(out, true, rs);
			}

		} catch (ClassNotFoundException | SQLException e) {
			response.setStatus(500);
			Response(out, false, null);
			return;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private boolean Response(PrintWriter out, boolean isOK, ResultSet rs) {
		JsonObject jObject = new JsonObject();
		// 如果不OK
		if (!isOK) {
			jObject.addProperty("isOK", isOK);
			//TODO: 修改状态码
			
			out.print(jObject.toString());
			out.flush();
			out.close();
			return false;
		}
		// 如果OK
		JsonArray jarray = new JsonArray();
		try {
			while (rs.next()) {
				
				int id = rs.getInt("id");
				int uid = rs.getInt("uid");
				String title = rs.getString("title");
				String detail= rs.getString("detail");
				int qtype=rs.getInt("qtype");
				String post_time = rs.getString("post_time");
				int lower_level_sum =rs.getInt("lower_level_sum");
				int agree_sum = rs.getInt("agree_sum");

				JsonObject jo1 = new JsonObject();
				jo1.addProperty("id", id);
				jo1.addProperty("uid", uid);
				jo1.addProperty("title", title);
				jo1.addProperty("detail", detail);
				jo1.addProperty("qtype", qtype);
				jo1.addProperty("post_time", post_time);
				jo1.addProperty("lower_level_sum", lower_level_sum);
				jo1.addProperty("agree_sum", agree_sum);

				jarray.add(jo1);
			}
		} catch (SQLException e) {
			isOK = false;
			jObject.addProperty("isOK", isOK);
			out.print(jObject.toString());
			out.flush();
			out.close();
			return false;
		}

		jObject.addProperty("isOK", isOK);
		jObject.add("answers", jarray);
		out.print(jObject.toString());
		out.flush();
		out.close();
		return true;
	}

}
