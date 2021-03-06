package serves.answer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serves.tools.GlobalVar.GlobalParameter;
import sql.AnswerTable;
import sql.MySQLInformation;
import sql.UserTable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SelectAmountAnswerByQid
 */
@WebServlet("/SelectAmountAnswerByQid")
public class SelectAmountAnswerByQid extends HttpServlet {
	int a=0;
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
	 * 閺嶈宓侀悽銊﹀煕閻ㄥ埇d閼惧嘲褰囬幐鍥х暰閺佹壆娲伴惃鍕礀缁涳拷
	 * qid閵嗕垢age
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int request_qid=-1;
		int request_page=-1;
		boolean isOK = true;
		PrintWriter out=null;
		response.setContentType("text/html;charset=utf-8");
	    response.setCharacterEncoding("utf-8");
		//閺屻儳婀呯拠閿嬬湴
		try {
			request_qid = Integer.parseInt( request.getParameter("qid"));
			request_page= Integer.parseInt( request.getParameter("page"));
		}catch (NumberFormatException  e) {
			System.out.println("閹碉拷婵夘偅鏆熼幑顔肩磽鐢拷");
			isOK = false;
			if(out!=null)
				Response(response, false,null);
			return;
		} 
		
		if(request_qid<0){
			Response(response, false,null);
			return;
		}
		
		try {
			AnswerTable at=new AnswerTable(GlobalParameter.uri, GlobalParameter.sql_user, GlobalParameter.sql_password);
			ResultSet rs =at.selectByQid(request_qid+"", request_page, 10);
			
			
			//鐎靛湱绮ㄩ弸婊堟肠鏉╂稖顢慗SON鐟欙絾鐎�
			if(rs.next()==false){
				//濞屸剝婀侀幍鎯у煂閺佺増宓�
				response.setStatus(501);
				Response(response, false,rs,true);
			}
			else{
				rs.beforeFirst();
				Response(response, true,rs);
			}
			
			at.CloseConnection();
			
		} catch (ClassNotFoundException | SQLException e) {
			Response(response, false,null);
			return;
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private boolean Response(HttpServletResponse response, boolean isOK,ResultSet rs){
		return Response(response,isOK,rs,false);
	}

	private boolean Response(HttpServletResponse response, boolean isOK,ResultSet rs,boolean isEmpty){
			PrintWriter out;
			try {
				out = ((ServletResponse) response).getWriter();
			} catch (IOException e) {
				System.out.println("io瀵倸鐖�");
				//TODO:婵″倷缍嶆径鍕倞鐠囥儱绱撶敮锟�
				try {
					response.sendError(404, "閹劏顩﹂弻銉﹀閻ㄥ嫯绁┃鎰瑝鐎涙ê婀�");
				} catch (IOException e1) {
					System.out.println("IO瀵倸鐖�");
				}
				return false;
			}
	
		JsonObject jObject = new JsonObject();
		//婵″倹鐏夋稉宄匥
		if(!isOK){
			//失败的时候加一个错误码
			if(isEmpty)
				response.setStatus(501);
			else
				response.setStatus(500);
			jObject.addProperty("isOK", isOK);
			out.print(jObject.toString());
			out.flush();
			out.close();
			return false;
		}
		//婵″倹鐏塐K
		System.out.println("isOK:OK");
		JsonArray jarray  = new JsonArray();
		try {
			UserTable ut=new UserTable(MySQLInformation.uri, MySQLInformation.account, MySQLInformation.password);
			while(rs.next()){
				int aid = rs.getInt("id");
				String detail= rs.getString("detail");
				int uid = rs.getInt("uid");
				String post_time =rs.getString("post_time");
				int agree_sum = rs.getInt("agree_sum");
				int comment_sum = rs.getInt("comment_sum");				
				
				JsonObject jo1 = new JsonObject();
				jo1.addProperty("aid",aid);
				jo1.addProperty("detail",detail);
				jo1.addProperty("qid", "0"); //空值
				jo1.addProperty("qtitle", "");//暂时为空
				jo1.addProperty("uid", uid);
				jo1.addProperty("post_time", post_time);
				jo1.addProperty("agree_sum", agree_sum);
				jo1.addProperty("comment_sum", comment_sum);
				ResultSet urs=ut.selectById(uid+""); //从用户表拿数据
				if(urs.next()){
					jo1.addProperty("name", urs.getString("name"));
					jo1.addProperty("photo", urs.getString("photo"));
				}else{
					jo1.addProperty("name", "unknow");
					jo1.addProperty("photo", "0");
				}
				//加到数组中
				jarray.add(jo1);
			}		
		} catch (SQLException e) {
			isOK=false;
			jObject.addProperty("isOK", isOK);
			out.print(jObject.toString());
			out.flush();
			out.close();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jObject.addProperty("isOK", isOK);
		jObject.add("answers",jarray);
		out.print(jObject.toString());
		out.flush();
		out.close();
		return true;
	}

}
