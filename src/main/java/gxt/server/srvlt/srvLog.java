package gxt.server.srvlt;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import anytools.JsonLog;

//@WebServlet("/log")
public class srvLog extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private ArrQueue queue = new ArrQueue();  
	static final int MAX_LEN_ARRAY = 20;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		ServletInputStream sis = req.getInputStream();
		InputStreamReader isr = new InputStreamReader(req.getInputStream()/*,"UTF-8"*/);
//		System.out.println("isr = " + isr);
		int retCode = JsonLog.RET_NOFORMAT;
		if (isr != null){
		JsonLog json = new JsonLog();
try{
	    if (json.json2obj(isr) == JsonLog.RET_SUCCESS)  {
// 		  System.out.println("json = " + json.getClassMet());
	      retCode = queue.addRec(json);
//	      System.out.println("srvLog = " + json.getMessage());
	    }
}catch(Exception e){
	    System.out.println("doPost error " + e.getMessage());
	    e.printStackTrace();
}
		}
		resp.setStatus(HttpServletResponse.SC_CREATED);
	    resp.setContentType("text/html");
	    resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(retCode);
        resp.flushBuffer();
	}

	public void init(){
        queue.start();
		System.out.println("servlet queue init");
	}
	public void destroy(){
		queue.stopRun();
//		if (queue.isAlive()) 
		queue.interrupt();
		System.out.println("servlet queue destroy");
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	public srvLog() {super();}

}
