package gxt.server.srvlt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class srvFile
 */
//@WebServlet("/srvFile")
public class srvFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String PATH_OUT_REPS = System.getProperty("rsys.out.path", "/WORK/exe/");
    public srvFile() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    doPost(req, resp);
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		byte[] buf = new byte[10240];
		InputStream f = null;
		boolean isFile = true;
		Map m = req.getParameterMap();
		java.util.Iterator it = m.keySet().iterator();
		String RepFile = (String)it.next();
		String rest = RepFile.substring(RepFile.indexOf(".")+1);
		if (rest.toUpperCase().equals("XLS")){
		    resp.setContentType("MIME_XLS");    
		} 
		else isFile = false;
		
	if (isFile){	
	    resp.setHeader( "Content-disposition", "attachment;filename=" + RepFile);  
	    resp.setCharacterEncoding("Cp1251");
    try {
	    ServletOutputStream out = resp.getOutputStream();
	    f = new FileInputStream(PATH_OUT_REPS + RepFile);
//	    	Log.log(Priority.ERROR, "FileInputStream");
   	    int read = 0;
	    while ((read = f.read(buf)) != -1) {
	    		out.write(buf, 0, read);
	    }
	    out.flush();
	    out.close();
      } catch (IOException e) {
//			Log.log(Priority.ERROR, "getXls() = "+e.getMessage());
	e.printStackTrace();
      }finally{	if (f != null) f.close();
		buf = null;
      }
	}
	
	}

}
