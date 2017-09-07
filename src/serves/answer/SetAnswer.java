package serves.answer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serves.tools.GlobalVar.GlobalParameter;
import sql.AnswerTable;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class SetAnswer
 */
@WebServlet("/SetAnswer")
public class SetAnswer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetAnswer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 编辑回答
	 * 需要参数qid,uid,detail
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int request_qid=-1;
		int request_uid=-1;
		String detail;
		
		boolean isOK=true;
		//查看请求
		try {
			//System.out.println(request.getParameter("qid") + "   "+request.getParameter("uid")+"   "+request.getParameter("detail") );
			request_qid = Integer.parseInt( request.getParameter("qid"));
		    request_uid = Integer.parseInt( request.getParameter("uid"));
		    detail=request.getParameter("detail");

		    
		} catch (NumberFormatException  e) {
			System.out.println("传输请求数据格式异常");
			isOK = false;
			Response(response, isOK);
			return;
		}	
		
		if(request_qid<0|| request_uid<0){
			Response(response, false);
			return;
		}
		
		//操作数据库
		try {
			AnswerTable at=new AnswerTable(GlobalParameter.uri, GlobalParameter.sql_user, GlobalParameter.sql_password);
			if(at.insert(request_qid+"", request_uid+"",detail) !=0){
				isOK=true;
			}
			else{
				isOK=false;
			}
			Response(response, isOK);
		
		} catch (ClassNotFoundException e) {
			System.out.println("找不到数据库类");
			isOK=false;
			Response(response, isOK);
		} catch (SQLException e) {
			System.out.println("无法连接到数据库或操作失败");
			System.out.println(e);
			isOK=false;
			Response(response, isOK);
		}	
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//response响应数据
	private boolean Response(HttpServletResponse response, boolean isOK){
			PrintWriter out;
			JsonObject jObject =new JsonObject();
			try {
				out = response.getWriter();
				String msg = null;
				//回复
				response.setContentType("text/html;charset=utf-8");
				response.setCharacterEncoding("utf-8");
					   
				jObject.addProperty("isOK", isOK);
				out.print(jObject.toString());
				out.flush();
				out.close();
						
				} catch (IOException e) {
						System.err.println(e);
				}
				return true;
		}
}
