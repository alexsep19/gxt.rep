package gxt.server.domain;

import j2f.runme;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

import anytools.JsonLog;
import anytools.JsonSend;

public class Worker extends Thread{
    String fRep, fFormat, fId, fConn, fFileName = "XXX", fStartTime = "dd.mm.yy", fRepFileName, feMail;
    Dao dao;
    long uId;

    private static String REP_PATH;
    private static String REP_PATH_LABEL;

    Map fPar;
    Connection fRepConn;
	static{
	   REP_PATH = System.getProperty("rsys.out.path", "/mnt/p/OWSWork/Prod/Data/Reports/Jasp/");
	   REP_PATH_LABEL = System.getProperty("rsys.out.label.path", "p:/OWSWork/Prod/Data/Reports/Jasp/");
	}

    Worker(String pJaspName, String pId, String pConn, String pFormat, 
    	   Map jasp_params, String eMail, Dao Dao, long UId)  throws Exception {
           this.fRep = pJaspName;
           fFormat = pFormat;
           fPar = jasp_params;
           fConn = pConn;
           Context initCtx = new InitialContext();
           fRepConn = ((DataSource)initCtx.lookup(fConn)).getConnection();
           fId = pId;
           java.util.Date d = new java.util.Date();
           fStartTime = String.format("%1$td.%1$tm.%1$tY %1$tH:%1$tM:%1$tS", d);
           fFileName = REP_PATH_LABEL + pJaspName + String.format("_%1$tY%1$tm%1$td_%1$tH%1$tM%1$tS", d) + "." + pFormat;
           fRepFileName = REP_PATH + pJaspName + String.format("_%1$tY%1$tm%1$td_%1$tH%1$tM%1$tS", d);
           feMail = eMail;
           dao = Dao;
           uId = UId;
       }

//    private static final String PROJECT = "gxt.rep";
//    private static final String MESSTYPE = "reprun";    
//    private static final String LOGURL = "/rsys-adm/log";
    private String url = RequestFactoryServlet.getThreadLocalRequest().getRequestURL().toString();
    private String HOSTURL = url.substring(0, url.indexOf("/", url.indexOf("/", url.indexOf("/") + 1) + 1));
  public void run() {
        int i = 0;	
        boolean isFound = false;
       try{	
    	System.out.println("=====>Pack Start " + fRep);
    	StringBuilder Mess = new StringBuilder(fRep).append(" start\n");
    	for(Object key: fPar.keySet()){
        	Mess.append(String.format("%1s = %2s\n", (String)key, fPar.get(key).toString()));
        }
    	
//		dao.uf.WriteLog( Mess.toString(), this.getClass().getName()+".run", JsonLog.LEVEL_M, "reprun", Dao.PROJECT, "");
		JsonLog json = new JsonLog();
		json.setClassMet(this.getClass().getName()+".run");
		json.setLevelLog(JsonLog.LEVEL_M);
		json.setMessage(Mess.toString());
		json.setMessType("reprun");
		json.setProject(Dao.PROJECT);
//		json.setTrace("");
		json.setUser(uId);
		json.WriteLog(dao.getUrlServlet());

        runme j = new runme( fRepFileName, fRep, (HashMap)fPar, fRepConn, fFormat);
        String ret = j.MakeFile();
        System.out.println("=====>Pack Stop " + fRep + " "+ ret);
//		dao.uf.WriteLog( "Стоп " + fRep + " "+ ret, this.getClass().getName()+".run", JsonLog.LEVEL_M, "reprun", Dao.PROJECT, "");
		JsonLog json2 = new JsonLog();
		json2.setClassMet(this.getClass().getName()+".run");
		json2.setLevelLog(JsonLog.LEVEL_M);
		json2.setMessage(fRep + " stop "+ ret);
		json2.setMessType("reprun");
		json2.setProject(Dao.PROJECT);
//		json.setTrace("");
		json2.setUser(uId);
		json2.WriteLog(dao.getUrlServlet());

//        if (!isInterrupted()) 
//            dao.delThread(fId); 
//            synchronized(ThrdPool){
//   		for( i = 0; i < ThrdPool.size(); i++){
//               if ( ((Worker)ThrdPool.get(i)).getRep().equals(this.fRep) ){
//                isFound = true;
//                break;
//               }
//   		}
//         if (isFound){
//        	 ThrdPool.remove(i);
//         }else Dao.log("=====>Pack for stop not found " + fRep, "I");
//         }
//        sendMail(feMail, "������ ����� " + fRepFileName);
       }catch(Exception e){
	    System.out.println("=====>Pack run stop Error "+ fRep +" " +e.getMessage());
// 	    dao.uf.WriteLog( "Pack run stop "+ fRep +" Error = " +e.getMessage(), this.getClass().getName()+".run", JsonLog.LEVEL_M, "reprun", Dao.PROJECT, JsonLog.stackTraceToString(e));
		JsonLog json3 = new JsonLog();
		json3.setClassMet(this.getClass().getName()+".run");
		json3.setLevelLog(JsonLog.LEVEL_M);
		json3.setMessage("Pack run stop "+ fRep +" Error = " +e.getMessage());
		json3.setMessType("reprun");
		json3.setProject(Dao.PROJECT);
		json3.setTrace(JsonLog.stackTraceToString(e));
		json3.setUser(uId);
		json3.WriteLog(dao.getUrlServlet());

       }finally{
           dao.delThread(fId); 
    	try{if (fRepConn != null) fRepConn.close();fRepConn = null;}catch(Exception ee){}
    	     System.out.println("=====>Pack stopo() "+fRep);
        }}
    
    public Integer getVersion(){return 1;}
    
    public String getFRep() {
        return fRep;
    }

    public void setFRep(String fRep) {
        this.fRep = fRep;
    }

    public String getFId() {
        return fId;
    }

    public void setFId(String fId) {
        this.fId = fId;
    }

    public String getFFileName() {
        return fFileName;
    }

    public void setFFileName(String fFileName) {
        this.fFileName = fFileName;
    }

    public String getFStartTime() {
        return fStartTime;
    }

    public void setFStartTime(String fStartTime) {
        this.fStartTime = fStartTime;
    }

}
