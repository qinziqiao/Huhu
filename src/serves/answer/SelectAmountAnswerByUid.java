package serves.answer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serves.tools.GlobalVar.GlobalParameter;
import sql.AnswerTable;
import sql.MySQLInformation;
import sql.QuestTable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SelectAmountAnswerByUid
 */
@WebServlet("/SelectAmountAnswerByUid")
public class SelectAmountAnswerByUid extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SelectAmountAnswerByUid() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) 根据用户的id获取指定数目的回答
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
			AnswerTable at = new AnswerTable(GlobalParameter.uri,
					GlobalParameter.sql_user, GlobalParameter.sql_password);
			ResultSet rs = at.selectByUid(request_uid + "", request_page, 10);
			
			// 对结果集进行JSON解析
			if (rs.next() == false) {
				// 没有找到数据
				response.setStatus(501);
				Response(out, false, null);
			} else {
				rs.beforeFirst();
				Response(out, true, rs);
			}
			
			at.CloseConnection();
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
			out.print(jObject.toString());
			out.flush();
			out.close();
			return false;
		}
		// 如果OK
		JsonArray jarray = new JsonArray();
		try {
			QuestTable qt=new QuestTable(MySQLInformation.uri, MySQLInformation.account, MySQLInformation.password);

			while (rs.next()) {
				int aid = rs.getInt("id");
				String detail = rs.getString("detail");
				int qid = rs.getInt("qid");
				int uid = rs.getInt("uid");
				String post_time = rs.getString("post_time");
				int agree_sum = rs.getInt("agree_sum");
				int comment_sum = rs.getInt("comment_sum");
				
				JsonObject jo1 = new JsonObject();
				jo1.addProperty("aid",aid);
				jo1.addProperty("detail", detail);
				jo1.addProperty("qid", qid);
				//从quest表里面找数据
				ResultSet qrs = qt.selectById(qid+"");
				if(qrs.next()){
					jo1.addProperty("qtitle",qrs.getString("title"));
				}
				else{
					jo1.addProperty("qtitle",qrs.getString(""));
				}
				//按顺序
				jo1.addProperty("uid",uid);
				jo1.addProperty("post_time", post_time);
				jo1.addProperty("agree_sum", agree_sum);
				jo1.addProperty("comment_sum", comment_sum);
				jo1.addProperty("name", "");
				jo1.addProperty("photo", "0");
				
				jarray.add(jo1);
			}
			qt.CloseConnection();
		} catch (SQLException | ClassNotFoundException e) {
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
