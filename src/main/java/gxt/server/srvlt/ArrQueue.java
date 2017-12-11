package gxt.server.srvlt;

import gxt.server.domain.DaoLog;
import java.util.ArrayList;
import java.util.List;
import anytools.JsonLog;


public class ArrQueue extends Thread{
    List<JsonLog> log = new ArrayList<JsonLog>(); 
    boolean isRun = true;
    DaoLog dao = new DaoLog();
    
    public void stopRun(){
    	isRun = false;
    }

	public ArrQueue(){
		dao.InitDb();
	}

	public int addRec(JsonLog rec) {
		if (!isRun) return JsonLog.RET_NORUN;
		synchronized (log) {
		  if (log.size() > srvLog.MAX_LEN_ARRAY) return JsonLog.RET_ARRAYFILLED;
		  if (!rec.getLevelLog().matches("["+JsonLog.LEVEL_E+"|"+JsonLog.LEVEL_M+"]")) return JsonLog.RET_ERLEVEL; 
		  log.add(rec);
		  log.notify();
		}
 	    return JsonLog.RET_SUCCESS;
	}
	
	public void run() {
		JsonLog procRec = null;
	    while(isRun || !log.isEmpty()) {
		  synchronized (log) {
		  while (log.isEmpty()) {
            try {
//              System.out.println("befor wait " + isRun);
		      if (isRun) log.wait();
//              System.out.println("after wait" + isRun);
	        } catch (InterruptedException ie) { 
//	        	System.out.println(ie.getMessage());
	        	break;
	        }
		  }
		  if (!log.isEmpty()) procRec = log.remove(0);
		 }
//		 System.out.println("write " + procRec.getMessage());
		 dao.WriteLog(procRec);
		}
	  System.out.println("ArrQueue run finished, log.size() = " + log.size());
   }
}
