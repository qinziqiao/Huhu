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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SelectAmountAnswerByQid
 */
@WebServlet("/SelectAmountAnswerByQid")
public class SelectAmountAnswerByQid extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int b=1;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectAmountAnswerByQid() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * 鏍规嵁鐢ㄦ埛鐨刬d鑾峰彇鎸囧畾鏁扮洰鐨勫洖绛�
	 * qid銆乸age
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int request_qid=-1;
		int request_page=-1;
		boolean isOK = true;
		PrintWriter out=null;
		response.setContentType("text/html;charset=utf-8");
	    response.setCharacterEncoding("utf-8");
		//鏌ョ湅璇锋眰
		try {
			request_qid = Integer.parseInt( request.getParameter("qid"));
			request_page= Integer.parseInt( request.getParameter("page"));
			out= response.getWriter();
		}catch (IOException e) {
			System.out.println("io寮傚父");
			//TODO:濡備綍澶勭悊璇ュ紓甯�
			try {
				response.sendError(404, "鎮ㄨ鏌ユ壘鐨勮祫婧愪笉瀛樺湪");
			} catch (IOException e1) {
				System.out.println("IO寮傚父");
			}
		}catch (NumberFormatException  e) {
			System.out.println("鎵�濉暟鎹紓甯�");
			isOK = false;
			if(out!=null)
				Response(out, false,null);
			return;
		} 
		
		if(request_qid<0){
			Response(out, false,null);
			return;
		}
		
		try {
			AnswerTable at=new AnswerTable(GlobalParameter.uri, GlobalParameter.sql_user, GlobalParameter.sql_password);
			ResultSet rs =at.selectByQid(request_qid+"", request_page, 10);
			
			//瀵圭粨鏋滈泦杩涜JSON瑙ｆ瀽
			if(rs.next()==false){
				//娌℃湁鎵惧埌鏁版嵁
				Response(out, false,null);
			}
			else{
				rs.beforeFirst();
				Response(out, true,rs);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			Response(out, false,null);
			return;
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private boolean Response(PrintWriter out, boolean isOK,ResultSet rs){
		JsonObject jObject = new JsonObject();
		//濡傛灉涓峅K
		if(!isOK){
			jObject.addProperty("isOK", isOK);
			out.print(jObject.toString());
			out.flush();
			out.close();
			return false;
		}
		//濡傛灉OK
		JsonArray jarray  = new JsonArray();
		try {
			while(rs.next()){
				
				String detail= rs.getString("detail");
				int uid = rs.getInt("uid");
				String post_time =rs.getString("post_time");
				int agree_sum = rs.getInt("agree_sum");
				int comment_sum = rs.getInt("comment_sum");
				
//				JsonObject jo[5] = new JsonObject[5];
//				for (int i = 0; i < jo.length; i++) {
//					jo[i]=new JsonObject();
//				}
				JsonObject jo1 = new JsonObject();
				jo1.addProperty("detail",detail);
				jo1.addProperty("uid", uid);
				jo1.addProperty("post_time", post_time);
				jo1.addProperty("agree_sum", agree_sum);
				jo1.addProperty("comment_sum", comment_sum);
			
				jarray.add(jo1);
			}		
		} catch (SQLException e) {
			isOK=false;
			jObject.addProperty("isOK", isOK);
			out.print(jObject.toString());
			out.flush();
			out.close();
			return false;
		}
		
		jObject.addProperty("isOK", isOK);
		jObject.add("answers",jarray);
		out.print(jObject.toString());
		out.flush();
		out.close();
		return true;
	}

}
