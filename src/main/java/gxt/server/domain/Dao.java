package gxt.server.domain;

import gxt.client.domain.Factory.RepRequest.LogPagingLoadResultProxy;
import gxt.server.srvlt.SessionCounter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.eclipse.persistence.config.QueryHints;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import jpa.atm.AtMstationStat;
import jpa.other.RsysLog;
import jpa.other.Sms_archive;
import jpa.rep.Arm_rep_param;
import jpa.rep.Arm_rep_paramDd;
import jpa.rep.Arm_rep_rep;
import jpa.rep.Arm_rep_repPar;
import jpa.rep.Arm_rep_repParPK;
import jpa.rep.Arm_rep_repRole;
import jpa.rep.Arm_rep_repRolePK;
import jpa.user.Arm_user;
import jpa.user.Arm_users_grantsDict;

import anytools.JsonLog;
import anytools.JsonSend;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.Request;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;

import ui.UserInfo;
import gxt.server.srvlt.SessionCounter;

public class Dao {
//    private static final String SERVLET_DD = "tDd";
//    private static final String SERVLET_RR = "rr";
    private static final String SESSION_FM = "fm";
    private static String PATH_OUT_REPS = System.getProperty("rsys.out.path", "/tmp/jasp");
    private static String JASP_PATH = System.getProperty("rsys.jasper", "/WORK/exe/jaspers/");
    private static String JASP_SVN_PATH =  System.getProperty("rsys.svn.url", "http://hibara/svn/jasper");
    private static String JASP_SVN_NAME = System.getProperty("rsys.svn.name", "CORP/Syshedshdl");
    private static String JASP_SVN_PASS = System.getProperty("rsys.svn.password", "itlekkth2");
    public static final String PROJECT = "gxt.rep";
    public static final String MESSTYPE = "reprun";    
    private static final String LOGURL = "/rsys-adm/log";
    private String urlServlet;
    public String getUrlServlet() {
		return urlServlet;
	}
	//    private String url = RequestFactoryServlet.getThreadLocalRequest().getRequestURL().toString();
    private final int MAX_THREADS = 3;
    private static ArrayList<Worker> ThrdPool = new ArrayList<Worker>();
    private static final EntityManagerFactory emfUser = Persistence.createEntityManagerFactory("jpaUser");
//    private static final Map<String, List<listData>> DD = new HashMap<String, List<listData>>();
    public static EntityManager emUser() {
	   EntityManager em = emfUser.createEntityManager();
	   em.setFlushMode(FlushModeType.COMMIT);
	   return em;
	   }
//    UserInfo uf = new UserInfo(RequestFactoryServlet.getThreadLocalRequest()){
    UserInfo uf;

    public Dao(){
    	String url = RequestFactoryServlet.getThreadLocalRequest().getRequestURL().toString();
    	urlServlet = url.substring(0, url.indexOf("/", url.indexOf("/", url.indexOf("/") + 1) + 1)) + LOGURL;
    	uf = new UserInfo(urlServlet, PROJECT){
    	    @Override
    	    protected String TransRole(String TabRole){
    		if (TabRole.equals("REPS_ADM")) return "A";
    		else return "X";
    	    }
        }; 
    }
  //============================= SVN
  	String[] Files = null;

  	public List<listData> getNewSvnJaspers(){
  	    int n = -1;
  	    Collection entries = null;
  	    SVNRepository repository = null;
  	    boolean isRenewed = false;
            ArrayList<listData> ret = new ArrayList<listData>();   
      try {
      	DAVRepositoryFactory.setup();
      	repository = SVNRepositoryFactory.create( SVNURL.parseURIDecoded( JASP_SVN_PATH ) );
      	ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( JASP_SVN_NAME , JASP_SVN_PASS );
      	repository.setAuthenticationManager( authManager );

      	SVNNodeKind nodeKind = repository.checkPath( "" ,  -1 );
      	if ( nodeKind != SVNNodeKind.DIR ) throw new Exception("нет пути " + JASP_SVN_PATH);
      	   entries = repository.getDir( "", -1 , null , (Collection) null );
      	   FillFiles();
//      	   itaNewJasp.setValue("");
      	   Iterator iterator = entries.iterator();
      	   while ( iterator.hasNext( ) ) {
      	      SVNDirEntry entry = ( SVNDirEntry ) iterator.next();
      	      if (( entry.getKind() == SVNNodeKind.DIR )||(!entry.getName().toUpperCase().matches("(.+)\\.JRXML")) )continue;
  	          n = Arrays.binarySearch(Files, entry.getName());
      	      if ( n >= 0){
                File f = new File(JASP_PATH + Files[n]);
                java.util.Date d = new java.util.Date();
                d.setTime(f.lastModified());
                if (entry.getDate().getTime() > f.lastModified() ){
//                  getNewFileAndComp( repository, entry.getName());
//                  itaNewJasp.setValue(itaNewJasp.getValue() + entry.getName() + "\n");
//                  ret.add(entry.getName());
                    ret.add(new listData( entry.getName(), getNewFileAndComp( repository, entry.getName()) ));
                  isRenewed = true;
                 }
               //new files 
      		 }else {
//      		   	getNewFileAndComp( repository, entry.getName());
//      	    	itaNewJasp.setValue(itaNewJasp.getValue() + entry.getName() + "\n");
//                ret.add(entry.getName());
                ret.add(new listData( entry.getName(), getNewFileAndComp( repository, entry.getName()) ));
      		  	isRenewed = true;
      		   }
      		 } 
//      	  if (!isRenewed) ret.add("обновлений нет");//itaNewJasp.setValue("обновления нет");
          } catch ( Exception e ) {
            System.out.println("getNewJaspers = " + e.getMessage());
            e.printStackTrace();
          }finally{
            if (entries != null) {entries.clear(); entries=null;}
            if (repository != null) repository.closeSession();
            Files = null;
          }
      return ret;
  	}

  	private String getNewFileAndComp(SVNRepository repository, String FN) throws Exception {
  	    FileOutputStream fos = null;
  	    String JN = FN.substring(0, FN.indexOf("."));
  	    ByteArrayOutputStream baos = new ByteArrayOutputStream( );
  	 try{   
  		repository.getFile( FN, -1 , null , baos );
          fos = new FileOutputStream(new File(JASP_PATH + FN));
          baos.writeTo(fos);
          fos.flush();
          JasperCompileManager.compileReportToFile( JASP_PATH + FN);
          JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new File(JASP_PATH + JN + ".jasper"));
          CheckRep( jasperReport.getPropertiesMap(), JN);
          CheckParam( jasperReport.getParameters(), JN);
          return "T";
  	 } catch ( Exception e ) {
         System.out.println("getNewFileAndComp() = "+FN+" " + e.getMessage());
         e.printStackTrace();
  	 }finally{        
        if (baos != null) { baos.close(); baos = null;}
        if (fos != null)  { fos.close(); fos = null;}
  	 }
  	   return "F";
  	}

  	final static String REPS_BY_NAME = "select r from Arm_rep_rep r where r.rname =?1";
  	private void CheckRep( JRPropertiesMap props, String rep) throws Exception{
  		String title = props.getProperty("REC_TITLE");
  		if (title==null || title.isEmpty()) throw new Exception("REC_TITLE not found");
  		String alias = props.getProperty("REC_ALIAS");
  		if (alias==null || alias.isEmpty()) throw new Exception("REC_ALIAS not found");
  		String format = props.getProperty("REC_FORMAT");
  		if (format==null || format.isEmpty()) format = "XLS";
  		
        EntityManager em = emUser();
        Arm_rep_rep r;
        try{        
           r = em.createQuery(REPS_BY_NAME, Arm_rep_rep.class).setParameter(1, rep).getSingleResult();
        }catch(NoResultException e){
   	       r = new Arm_rep_rep();
   	       r.setRname(rep);
        }
        if (!alias.equals(r.getAlias())) r.setAlias(alias);
        if (!title.equals(r.getRlabel())) r.setRlabel(title);
        if (!format.equals(r.getRformat())) r.setRformat(format);
        try{
          em.getTransaction().begin();
	      if (r.toString().equals("0")) em.persist(r);
	      else em.merge(r);
	      em.flush();
	      em.getTransaction().commit();
        }catch(Exception e){
        	em.getTransaction().rollback();
        	throw new Exception(e); 
        }
  	}
  			
  	final static String PARS_BY_REP = "select p from Arm_rep_repPar p where p.arm_rep_rep.rname =?1";
  	private void CheckParam(JRParameter[] pars, String rep) throws Exception{
  		EntityManager em = emUser();
  	    List<Arm_rep_repPar> tabPars = em.createQuery(PARS_BY_REP).setParameter(1, rep).getResultList();
//  	    ArrayList<String> insPar = new ArrayList<String>();
  	    HashMap<String,String> insPar = new HashMap<String,String>();
  	    Arm_rep_rep r = null;
  	    Arm_rep_param p = null;
  	try{
        em.getTransaction().begin();
        for (JRParameter jit: pars){
          String jPar = jit.getName();
          if (!jPar.startsWith("P_")) continue;
          String order = jit.getPropertiesMap().getProperty("PR_ORDER");
          if (order == null) throw new Exception("Missing PR_ORDER for "+jPar);
          boolean found = false;
          int i = 0;
          for(i = 0; i < tabPars.size(); i++){
        	if (jPar.equals(tabPars.get(i).getArm_rep_param().getPname())&&
        		order.equals(tabPars.get(i).getLabelOrder())) {
        		tabPars.remove(i); found = true; break;} 
          }
          if (!found) insPar.put(jPar, order);
//          else tabPars.remove(i); 
        }
        em.flush();
//        em.getTransaction().commit();
//        em.getTransaction().begin();
        try{
          if (!insPar.isEmpty()) r = em.createQuery("select r from Arm_rep_rep r where r.rname =?1", Arm_rep_rep.class).setParameter(1, rep).getSingleResult();
          for(Arm_rep_repPar it: tabPars){
            em.remove(em.find(Arm_rep_repPar.class, it.getId()));
          }
          em.flush();
          for(Entry<String, String> it: insPar.entrySet()){
        	p = em.createQuery("select r from Arm_rep_param r where r.pname =?1", Arm_rep_param.class).setParameter(1, it.getKey()).getSingleResult();
        	Arm_rep_repPar rp = new Arm_rep_repPar( r, p);
        	rp.setLabelOrder(it.getValue());
        	em.persist(rp);  
          }
	      em.flush();
	      em.getTransaction().commit();
        }catch(Exception e){
        	em.getTransaction().rollback();
        	throw new Exception(e); 
        }
  	}finally{
  		tabPars.clear(); insPar.clear();
  	}
  	}
  	 
  	private void FillFiles() throws Exception{
  		FilenameFilter FileFilter = new FilenameFilter(){
  		         public boolean accept(File dir, String name){
  		           if ((new File(dir, name)).isDirectory())  return false;
  		           else return(name.toUpperCase().matches("(.+)\\.JRXML"));        	   
  		         }
  		     };
  		  File Dir = new File(JASP_PATH);
  	      Files = Dir.list(FileFilter);
  		  Arrays.sort(Files);
  		}

//============================ Context =================================================    
    public List<String> getSessionUsers(){
    	SessionCounter counter = SessionCounter.getInstance(RequestFactoryServlet.getThreadLocalRequest().getServletContext());
    	ArrayList<String> ret = new ArrayList<String>();
    	Iterator it = counter.getSessions().entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
          ret.add((String) pair.getValue());
        }
      return ret;	
    }

    public String getUserInfo(){
	return uf.getUserInfo(RequestFactoryServlet.getThreadLocalRequest(), emUser());
    }
    
    public String getFIs(){
	return uf.getFIs(RequestFactoryServlet.getThreadLocalRequest(), emUser());
    }

    public String getRole(){
	return uf.getRole(RequestFactoryServlet.getThreadLocalRequest(), emUser());
    }
    public String getLogin(){
//    	uf.WriteLog( "создание сессии", this.getClass().getName()+".getLogin", JsonLog.LEVEL_M, "login", PROJECT, "");
	return uf.getLogin(RequestFactoryServlet.getThreadLocalRequest(), emUser()).toUpperCase();
    }
    
    public ArrayList<Worker> getRunReps() throws Exception{
//	ArrayList<Worker> rr = (ArrayList<Worker>)RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getAttribute(SERVLET_RR);
//       if (rr == null){
//	 System.out.println("getRunReps() NULL");
//	 rr = new ArrayList<Worker>();
//	 rr.add(new Worker("repName", "10", "repAlias", "repFormat", new HashMap<String,String>(), ""));
//	 RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().setAttribute(SERVLET_RR, rr);
//       }
	synchronized(ThrdPool){
       return ThrdPool;
	}
    }
    public ArrayList<Worker> addRunReps(String repName, String repId, String repAlias, String repFormat, String param) throws Exception{
//	ArrayList<Worker> rr = (ArrayList<Worker>)RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().getAttribute(SERVLET_RR);
//        ServletContext context = RequestFactoryServlet.getThreadLocalServletContext();
//        synchronized(RequestFactoryServlet.getThreadLocalServletContext()){
	synchronized(ThrdPool){
        if (!ThrdPool.isEmpty()){
           if (ThrdPool.size() > 2) throw new Exception("Уже 3 отчета");
           for(Worker wi: ThrdPool){
               if (wi.fId.equals(repId)) throw new Exception("Отчет уже запущен");
           }
        }
//        else if (rr == null){
//	  System.out.println("addRunReps NULL");
//    	  rr = new ArrayList<Worker>();
//	}
//        StringBuilder Mess = new StringBuilder(pRepName).append(" start\n");
        System.out.println("param = "+param);
        HashMap jasp_params = new HashMap();
        if (param != null && !param.isEmpty()){
            String[] s = param.split(";");
          for(String it: s){
              String[] ss = it.split("=");
              if (ss[0].equals("P_LDM")){
        	  ss[1] = getLDM(ss[1]);
              }
              System.out.println(ss[0] +"=" +ss[1]);
              jasp_params.put(ss[0], ss[1]);
           }
        }
//        JsonLog json = new JsonLog();
//		json.setClassMet(this.getClass().getName()+".addRunReps");
//		json.setLevelLog(JsonLog.LEVEL_M);
//		json.setMessage(Mess.toString());
//		json.setMessType(MESSTYPE);
//		json.setProject(PROJECT);
//		json.setTrace("");
//		json.setUser(1);
//		JsonSend js = new JsonSend(json);//, HOSTURL + LOGURL);
//		System.out.println("js.Send = " + js.Send(false));
        HashMap<String,String> m = (HashMap<String,String>) RequestFactoryServlet.getThreadLocalRequest().getSession().getAttribute(UserInfo.SESSION_KEYS);
        Worker w = new Worker(repName, repId, repAlias, repFormat, jasp_params, "", this, Long.parseLong(m.get(UserInfo.KEY_UI)));
        ThrdPool.add(w);
//            System.out.println("w.start()");
	    w.start();
//	rr.add();
//	RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().setAttribute(SERVLET_RR, rr);
	return ThrdPool;
        }
    }
    private String getLDM(String PutVal){
	 Calendar cal = Calendar.getInstance();
	    cal.set(Integer.parseInt(PutVal.substring(3,7)), Integer.parseInt(PutVal.substring(0,2))-1, 1);
	    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
	    return (new SimpleDateFormat("dd-MM-yyyy")).format(cal.getTime());	}
    
    public void delThread(String repId){
try{
//	synchronized(RequestFactoryServlet.getThreadLocalServletContext()){
	synchronized(ThrdPool){
	   int i = 0; 
 	   boolean isFound = false;
// 	   System.out.println("delThread repId = |" + repId + "|");
	   for( i = 0; i < ThrdPool.size(); i++){
	       if ( ((Worker)ThrdPool.get(i)).getFId().equals(repId) ){
//		   System.out.println("delThread getFId() = |" + ((Worker)ThrdPool.get(i)).getFId() + "|");
                   isFound = true;
                   break;
                 }
	    }
           if (isFound){
//      	     System.out.println("delThread ThrdPool = " + ThrdPool.size());
    	     ThrdPool.remove(i);
//    	     System.out.println("delThread ThrdPool = " + ThrdPool.size());
          }else System.out.println("=====>Pack for stop not found " + repId);
        }
}catch(Exception ee){
    System.out.println("delThread Exception = " + ee.getMessage());
	}
    }
    
    
//========================= SELECTs ======================================================    
//    public static String REP_ROLE = "select d from Arm_rep_repRole o, Arm_users_grantsDict d "+
//                                    "where o.arm_rep_rep=?1 and o.arm_users_grantsDict=d";
//    public List<Arm_rep_repRole> getChildren(Arm_rep_rep rep){
//    	EntityManager em = emUser();
//    	 try {
//   	       return em.createQuery(REP_ROLE).setParameter(1, rep).getResultList();
//   	      }catch (RuntimeException re) {
//   	          re.printStackTrace();
//   	          throw re;
//   	      }
//    }
    
    public static String MAX_DATE_STATISTICS = "with x as (Select max(settl_date) m from DOC_STATISTICS) "+
                                               "select to_char(x.m,'dd.mm.yy') from x";
    public String getMaxDateStat(){
	EntityManager em = emUser();
	  try {
//		  log("pageTab failed for " + Sql, "E");
	       return em.createNativeQuery(MAX_DATE_STATISTICS).getSingleResult().toString();
	      }catch (RuntimeException re) {
	          re.printStackTrace();
	          throw re;
	      }
  }
    
  public static String USER_REPS = "select r from Arm_rep_rep as r, " +
                                              " Arm_user as u, Arm_users_grant as g," +
                                              " Arm_rep_repRole as rr"+
                                              " where UPPER(u.certId)=?1 and g.arm_user=u " +
                                              " and rr.arm_users_grantsDict=g.arm_users_grantsDict "+
                                              " and rr.arm_rep_rep=r"+
                                              " order by r.id";

   public List<Arm_rep_rep> getUserReps(String login) throws Exception {
       EntityManager em = emUser();
       try {
//	   List<Arm_rep_rep> l = em.createQuery(USER_REPS).setParameter(1, login).getResultList();
//	   System.out.println("l = "+l);
	  return em.createQuery(USER_REPS).setParameter(1, login).getResultList();
       }catch (Exception re) {
	  re.printStackTrace();
	  throw re;
       }
//       return Arrays.asList(new String[]{"1", "2"});
    }

   public static String PAGE_PARAMDD = "select d from Arm_rep_paramDd as d order by d.id";
   private static Map<String, List<listData>> dd = null; 
   private final static int FLAG_DD_EMPTY = 0;
   private final static int FLAG_DD_FILLED = 1;
   private static int flagDD = FLAG_DD_EMPTY;
   public List<listData> getParamDd(Arm_rep_paramDd ddId) throws Exception {
//       ServletContext context = RequestFactoryServlet.getThreadLocalServletContext();
//System.out.println("getParamDd = "+flagDD+"; dd = "+(dd==null));
	   
	   if (flagDD == FLAG_DD_FILLED){
		   return dd.get(ddId.toString());
	   }
	   
       synchronized(RequestFactoryServlet.getThreadLocalServletContext()){
//System.out.println("synchronized");
       if (dd == null){
//       if (dd.isEmpty()){
//System.out.println("dd = "+(dd==null));
	       dd = new HashMap<String, List<listData>>();
           EntityManager em = emUser();
           List<Arm_rep_paramDd> t;
           Connection db_conn = null;
 	       Statement st = null;
 	       ResultSet result = null;
       try {
         Context initCtx = new InitialContext();
	     t = (List<Arm_rep_paramDd>)em.createQuery(PAGE_PARAMDD).getResultList();
         for(Arm_rep_paramDd it: t){
           ArrayList<listData> NV = new ArrayList<listData>();
           if (it.getDdType().equals("D")){
             DataSource ds = (DataSource) initCtx.lookup(it.getAlias());
	         db_conn = ds.getConnection(); 
             st = db_conn.createStatement();
	         result = st.executeQuery(it.getSqlBody());
	         while (result.next())
		     NV.add(new listData(result.getString("name"),result.getString("value")));
 	         if (result != null) result.close();
             if (st != null) st.close();
	         if (db_conn != null) db_conn.close();
           }else {
             String[] s1 = it.getSqlBody().split(";");
	         for(String I: s1){
	           String[] s2 = I.split(",");
	           NV.add(new listData(s2[0].trim(), s2[1].trim()));
//		       new String[]{s2[0].trim(), s2[1].trim()});
	         }
//	     dd.addItem(it.getId(), NV);
           }
	       dd.put(String.valueOf(it.getId()), NV); 
          }
          flagDD = FLAG_DD_FILLED;
         }catch (Exception re) {
	       re.printStackTrace();
	       throw re;
         }finally {
  	       try { if (result != null) result.close();
	         if (st != null) st.close();
	         if (db_conn != null) db_conn.close(); } catch (Exception e){ }
         }
//         RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext().setAttribute(SERVLET_DD, dd);
       }
//       System.out.println("dd.get(ddId.toString()).size = " + dd.get(ddId.toString()).size());
       }
//System.out.println("return flagDD = "+flagDD);

       return dd.get(ddId.toString());
   }
   
//======================================================================================
   public static String SQL_LOG = " from "+ RsysLog.class.getSimpleName() + " m,"+ Arm_user.class.getSimpleName()+
		                          " u where m.userId=u ";
   public LogPagingLoadResultBean getListLog(int start, int limit, List<SortInfoBean> sortInfo, List<FilterConfigBean> filterConfig){
	   EntityManager em = emUser();
	   int total = 0;
//	   System.out.println("getListLog = "+start);
		StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":" order by");
		String filtr = "", orderIt = "", filterIt = "";
	      try {
	           for(SortInfo it:sortInfo){
	              if (it.getSortField().equals("strEventdate")) orderIt = " m.eventdate";
	              else  orderIt = " m." + it.getSortField();
	              order = order.append(orderIt).append(" ").append(it.getSortDir()).append(",");
	           }
	           order.setCharAt( order.length()-1, ' ');

	           for(FilterConfigBean it: filterConfig){
	              if (it.getField().equals("userString")) {
	        	    filterIt = " u.secondName like '"+it.getValue().trim() +"'";
	              }
	              else filterIt = "m."+it.getField()+ " like '" + it.getValue().trim() +"'";
	              filtr = filtr + " and "+filterIt;
	           }
		 List<RsysLog> page = em.createQuery("select m" + SQL_LOG  + filtr + order.toString(), RsysLog.class).setFirstResult(start).setMaxResults(limit).getResultList();
//		 System.out.println("page = "+page.size());
		 total = ((Long) em.createQuery("select count(m)" + SQL_LOG + filtr).getSingleResult()).intValue();
		 return new LogPagingLoadResultBean(page, total, start);
	       } catch (Exception ex) {
		      ex.printStackTrace();
		      throw new RuntimeException(ex);
	       }
   }
   
   public static String LIST_ROLE = "select o from "+ Arm_rep_repRole.class.getSimpleName()+" o where o.arm_rep_rep=?1";
   public RoleLoadResultBean getListRole(List<SortInfoBean> sortInfo, Arm_rep_rep rep){
   	EntityManager em = emUser();
  	 try {
 	       return new RoleLoadResultBean(em.createQuery(LIST_ROLE).setParameter(1, rep).getResultList());
 	      }catch (RuntimeException re) {
 	          re.printStackTrace();
 	          throw re;
 	      }
   }
   
   public static String LIST_REP = "select r from "+ Arm_rep_rep.class.getSimpleName()+" r order by r.id";
   public RepLoadResultBean getListReps(List<SortInfoBean> sortInfo){
   	EntityManager em = emUser();
  	 try {
 	       return new RepLoadResultBean(em.createQuery(LIST_REP).getResultList());
 	      }catch (RuntimeException re) {
 	          re.printStackTrace();
 	          throw re;
 	      }
   }

//   private static String LIST_PARS = "select p from " + Arm_rep_param.class.getSimpleName()+" p,"+Arm_rep_paramDd.class.getSimpleName()+
//           " t where l.arm_rep_paramDd=t";
   private static String LIST_PARS = "select p from " + Arm_rep_param.class.getSimpleName()+" p";
   public ParsLoadResultBean getListPars(List<SortInfoBean> sortInfo){
       EntityManager em = emUser();
       StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":" order by");
//       String orderIt = "";
       try {
           for(SortInfo it:sortInfo){
//               if (it.getSortField().equals("atPart")) orderIt = " p.name";
//               else if (it.getSortField().equals("finName")) orderIt = " finName";
//               else orderIt = " t." + it.getSortField();
//               order = order.append(orderIt).append(" ").append(it.getSortDir()).append(",");
             order = order.append(" p.").append(it.getSortField()).append(" ").append(it.getSortDir()).append(",");
            }
            order.setCharAt( order.length()-1, ' ');
//            List r = em.createQuery(LIST_PARS + order).getResultList();
//            System.out.println("r.size() = " + r.size());
         return new ParsLoadResultBean(em.createQuery(LIST_PARS + order).getResultList());
       }catch (RuntimeException re) {
         re.printStackTrace();
       throw re;
       }
   }
   
   private static String LIST_DD = "select p from " + Arm_rep_paramDd.class.getSimpleName()+" p";
   public DdLoadResultBean getListDd(List<SortInfoBean> sortInfo){
       EntityManager em = emUser();
       StringBuilder order = new StringBuilder(sortInfo.isEmpty()?" ":" order by");
//       String orderIt = "";
       try {
           for(SortInfo it:sortInfo){
             order = order.append(" p.").append(it.getSortField()).append(" ").append(it.getSortDir()).append(",");
            }
            order.setCharAt( order.length()-1, ' ');
//            List r = em.createQuery(LIST_PARS + order).getResultList();
//            System.out.println("r.size() = " + r.size());
         return new DdLoadResultBean(em.createQuery(LIST_DD + order).getResultList());
       }catch (RuntimeException re) {
         re.printStackTrace();
       throw re;
       }
   }
   
//   private final static String LIST_SMS = "select s from " + Sms_archive.class.getSimpleName()+
//   private final static String LIST_SMS = "select * from proc.sms#archive s where s.phone_number = ?1 and s.receive_time >= ?2 and s.receive_time < ?3";
//   private final static String LIST_SMS = "select * from proc.sms#archive s where s.normalizednumber = ?1 and s.receive_time >= to_date(?2,'dd-mm-yyyy') and s.receive_time < to_date(?3,'dd-mm-yyyy')";
   private final static String LIST_SMS = "select * from proc.sms#archive s where s.normalizednumber = ?1 and s.receive_time >= ?2 and s.receive_time < ?3";
   final static String DATE_FORMAT = "dd-MM-yyyy";
   public SmsLoadResultBean getListSms(List<SortInfoBean> sortInfo, String ph, Date from, Date to){
	   String orderIt = "";
//	   String f = new SimpleDateFormat(DATE_FORMAT).format(from); 
//	   String t = new SimpleDateFormat(DATE_FORMAT).format(to);
//	   System.out.println("ph = "+ph);
//	   System.out.println("from = "+from);
//	   System.out.println("to = "+to);
	   if (ph == null) {
		   return new SmsLoadResultBean(new ArrayList<Sms_archive>());
	   }
       EntityManager em = emUser();
       StringBuilder order = new StringBuilder(sortInfo.isEmpty()? " ": " order by");
       Calendar cF = Calendar.getInstance();
       cF.setTime(from);
       cF.set(Calendar.HOUR_OF_DAY, 0);
       cF.set(Calendar.MINUTE, 0);
       cF.set(Calendar.SECOND, 0);
       cF.set(Calendar.MILLISECOND, 0);
//	   cF.clear(Calendar.MINUTE);
//	   cF.clear(Calendar.SECOND);
//	   cF.clear(Calendar.MILLISECOND);
       Calendar cT = Calendar.getInstance();
       cT.setTime(to);
       cT.set(Calendar.HOUR_OF_DAY, 0);
       cT.set(Calendar.MINUTE, 0);
       cT.set(Calendar.SECOND, 0);
       cT.set(Calendar.MILLISECOND, 0);
//       cT.clear(Calendar.MINUTE);
//       cT.clear(Calendar.SECOND);
//       cT.clear(Calendar.MILLISECOND);
       cT.add(Calendar.DAY_OF_MONTH, 1);
//       ph = "+79213984833";// + //ph;
//       System.out.println("ph = "+ph);
//       System.out.println("cT = "+cT);
//       String orderIt = "";

//       System.out.println("from = "+f);
//	   System.out.println("to = "+t);
	   List<Sms_archive> r = null;
       try {
           for(SortInfo it:sortInfo){
        	  if (it.getSortField().equals("strReceiveTime")) orderIt = " s.RECEIVE_TIME";
        	  else if (it.getSortField().equals("respText")) orderIt = " s.RESP_TEXT";
        	  else if (it.getSortField().equals("phoneNumber")) orderIt = " s.PHONE_NUMBER";
        	  else if (it.getSortField().equals("respText")) orderIt = " s.RESP_TEXT";
        	  else  orderIt = " s." + it.getSortField();    	
        	  order = order.append(orderIt).append(" ").append(it.getSortDir()).append(",");
//             order = order.append(" s.").append(it.getSortField()).append(" ").append(it.getSortDir()).append(",");
            }
            order.setCharAt( order.length()-1, ' ');
         return new SmsLoadResultBean(em.createNativeQuery(LIST_SMS + order, Sms_archive.class).setHint(QueryHints.PARAMETER_DELIMITER, "&").
        		    setParameter(1, "+7" + ph).
            		setParameter(2, new java.sql.Date(cF.getTimeInMillis())).
            		setParameter(3, new java.sql.Date(cT.getTimeInMillis())).
            		getResultList());
       }catch (RuntimeException re) {
         re.printStackTrace();
         throw re;
       }
   }

//========================= Files ======================================================   
   public FilePagingLoadResultBean getFiles(int start, int limit, List<SortInfoBean> sortInfo/*, List<FilterConfigBean> filterConfig*/) throws Exception {
       int total = 0;
       List<FileData> page = new ArrayList<FileData>();
       String mask = getFilesMask();
       if (!mask.isEmpty()){
         SimpleDateFormat fd = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US);
         File Dir = new File(PATH_OUT_REPS);
         File[] files = Dir.listFiles(MakeMatch(mask));
         total = files.length;
        	 
         if (sortInfo.size() > 0) {
             SortInfo sort = sortInfo.get(0);
             final String sortField = sort.getSortField();
//             SortDir sd = sort.getSortDir();
//             System.out.println("sortField = "+sortField+"; sd = "+sd);
             if (sortField != null) {
        	if (sort.getSortDir().equals(SortDir.ASC)){
        	   Arrays.sort(files, new Comparator<File>(){
        	       public int compare(File f1, File f2){
        		if (sortField.equals("getCreated")) return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
        		else if (sortField.equals("getName")) return f1.getName().compareTo(f2.getName());
        		else if (sortField.equals("getSize")) return f1.length() > f2.length()? 1 : (f1.length() == f2.length()?0:-1);
        		else return 0;
        	       }
        	   });
                 }else{
          	   Arrays.sort(files, new Comparator<File>(){
        	       public int compare(File f1, File f2){
        		if (sortField.equals("getCreated")) return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
        		else if (sortField.equals("getName")) return f2.getName().compareTo(f1.getName());
        		else if (sortField.equals("getSize")) return f2.length() > f1.length()? 1 : (f2.length() == f1.length()?0:-1);
        		else return 0;
        	       }
        	   });
        	}
             }}

         int actualTotal = total;
         if (limit > 0) {
             actualTotal = Math.min(start + limit, actualTotal);
         }
         for (int i = start; i < actualTotal; i++) {
           page.add(new FileData(files[i].getName(), String.format("%,d", files[i].length()), fd.format(new Date(files[i].lastModified())) ));
         }
       }
       return new FilePagingLoadResultBean(page, total, start);
   }

   private String getFilesMask() throws Exception{
       String reps4Login = (String)RequestFactoryServlet.getThreadLocalRequest().getSession().getAttribute(SESSION_FM);
       if (reps4Login == null || reps4Login.isEmpty()){
	 reps4Login = "";
	 List<Arm_rep_rep> reps = getUserReps(getLogin());
         for(Arm_rep_rep r :reps){
      	   if (reps4Login.length() > 0) reps4Login = reps4Login + "|";
      	   reps4Login = reps4Login + "(" + r.getRname() + "[0-9_.A-Za-z]*)";
	 }
         reps.clear(); reps = null;
         RequestFactoryServlet.getThreadLocalRequest().getSession().setAttribute(SESSION_FM, reps4Login);
       }
      return reps4Login;
   }
   
   private FilenameFilter MakeMatch(final String mask) {
//       FilenameFilter FileFilter = null;
//       final String RepFiltr = reps4Login;
//       log("RepFiltr = " + RepFiltr, "E");
       return new FilenameFilter(){
           public boolean accept(File dir, String name){
             if ((new File(dir, name)).isDirectory())  return false;
             else return(name.matches(mask));
           }
       };
//    return FileFilter;    	
   }
//========================= find ======================================================   
//   public static Arm_rep_repParPK findArm_rep_repParPK(Long id) throws RuntimeException{
//       if (id == null) return null;
//       return (Arm_rep_repParPK) findObject(Arm_rep_repParPK.class, id);
//   }
   
   final static String SQL_ALLDD = "select c from " + Arm_rep_paramDd.class.getSimpleName()+" c order by c.id";
   public static String SQL_ALLROLE = "select d from " + Arm_users_grantsDict.class.getSimpleName()+" d where d.name LIKE 'REPS_%' order by d.name";
   public List getAllRec(String Sql){
	EntityManager em = emUser();
//	String sql = "";
	 try {
//	   if (Sql == Factory.SQL_ALLDD) sql = SQL_ALLDD;
//	   else if (Sql == 2 ) sql = SQL_ALLROLE; 
//	   else if (Sql == 3 ) sql = SQL_ALLPART; 
//	   else if (Sql == 4 ) sql = SQL_ALLOK;
          return (List)em.createQuery(Sql).getResultList();
	 }catch (RuntimeException re) {
	    	  re.printStackTrace();
	          throw re;
	 }  
   }
   
   public List<Arm_rep_paramDd> getAllDd(){return getAllRec(SQL_ALLDD);}
   public List<Arm_users_grantsDict> getAllRole(){return getAllRec(SQL_ALLROLE);}

   public void merg(Object rec){
	EntityManager em = emUser();
       try {
//    	   System.out.println("merg = "+ rec.toString());
	      em.getTransaction().begin();
	      if (rec.toString().equals("0")) em.persist(rec);
	      else em.merge(rec);
	      em.flush();
	      em.getTransaction().commit();
	    }catch(RuntimeException e){
	      if (em.getTransaction().isActive()) em.getTransaction().rollback();
  	      e.printStackTrace();
	      throw e;
	    }
   }
   public void mergRole(Arm_rep_repRole rec, Arm_rep_repRole oldRec, boolean isIns){
	EntityManager em = emUser();
       try {
//    	   System.out.println("rec = "+ rec.toString() + ";oldRec ="+oldRec.toString());
//    	   System.out.println("isIns = "+ isIns);
	      em.getTransaction().begin();
//	      if (isIns) {
////	    	  rec.setArm_rep_rep(oldRec.getArm_rep_rep());
//	    	  em.persist(rec);
//	      }else {
//	    	  em.remove(em.find(Arm_rep_repRole.class, oldRec.getId()));
//	      }
	      if (!isIns) em.remove(em.find(Arm_rep_repRole.class, oldRec.getId()));
    	  em.persist(rec);
	      em.flush();
	      em.getTransaction().commit();
	    }catch(RuntimeException e){
	      if (em.getTransaction().isActive()) em.getTransaction().rollback();
  	      e.printStackTrace();
	      throw e;
	    }
   }
   public void remov(Object rec, Class<?> cl, long id){
       EntityManager em = emUser();
       try {
	      em.getTransaction().begin();
	      em.remove(em.find(cl, id));
	      em.flush();
	      em.getTransaction().commit();
	    }catch(RuntimeException e){
	      if (em.getTransaction().isActive()) em.getTransaction().rollback();
   	      e.printStackTrace();
	      throw e;
	    } 
   }

   public void removParam(Arm_rep_param rec) throws RuntimeException{
	   remov(rec, Arm_rep_param.class, rec.getId());
   }
   public void removDd(Arm_rep_paramDd rec) throws RuntimeException{
	   remov(rec, Arm_rep_paramDd.class, rec.getId());
   }
   public void removRole(Arm_rep_repRole rec)throws RuntimeException{
	   EntityManager em = emUser();
       try {
	      em.getTransaction().begin();
	      em.remove(em.find(Arm_rep_repRole.class, rec.getId()));
	      em.flush();
	      em.getTransaction().commit();
	    }catch(RuntimeException e){
	      if (em.getTransaction().isActive()) em.getTransaction().rollback();
   	      e.printStackTrace();
	      throw e;
	    } 
   }

   public static Arm_user findArm_user(Long id) throws RuntimeException{
       if (id == null) return null; 
       return (Arm_user) findObject(Arm_user.class, id);
   }
   public static RsysLog findRsysLog(Long id) throws RuntimeException{
       if (id == null) return null; 
       return (RsysLog) findObject(RsysLog.class, id);
   }

   public static Sms_archive findSmsArchive(Long id) throws RuntimeException{
       if (id == null) return null; 
       return (Sms_archive) findObject(Sms_archive.class, id);
   }
   
   public static Arm_rep_repPar findArm_rep_repPar(Arm_rep_repParPK id) throws RuntimeException{
       if (id == null) return null; 
       EntityManager em = emUser();
       try {
	     return em.find(Arm_rep_repPar.class, id);
           }catch(RuntimeException e){
	     e.printStackTrace();
	     throw e;
           } 
//       return (Arm_rep_repPar) findObject(Arm_rep_repPar.class, id);
   }
   
   public static Arm_rep_paramDd findArm_rep_paramDd(Long id) throws RuntimeException{
       if (id == null) return null; 
       return (Arm_rep_paramDd) findObject(Arm_rep_paramDd.class, id);
   }
   
   public static Arm_rep_param findArm_rep_param(Long id) throws RuntimeException{
       if (id == null) return null; 
       return (Arm_rep_param) findObject(Arm_rep_param.class, id);
   }
   
   public static Arm_rep_rep findArm_rep_rep(Long id) throws RuntimeException{
       if (id == null) return null; 
       return (Arm_rep_rep) findObject(Arm_rep_rep.class, id);
   }

   public static Arm_users_grantsDict findArm_users_grantsDict(Long id) throws RuntimeException{
       if (id == null) return null; 
       return (Arm_users_grantsDict) findObject(Arm_users_grantsDict.class, id);
   }
   
   public static Arm_rep_repRole findArm_rep_repRole(Arm_rep_repRolePK id) throws RuntimeException{
       if (id == null) return null;
       EntityManager em = emUser();
       try {
	     return em.find(Arm_rep_repRole.class, id);
           }catch(RuntimeException e){
	     e.printStackTrace();
	     throw e;
           } 
//       return (Arm_rep_repRole) findObject(Arm_rep_repRole.class, id);
   }
   
   public static Object findObject(Class<?> cl, Long id) {
       EntityManager em = emUser();
       try {
	     return em.find(cl, id);
           }catch(RuntimeException e){
	     e.printStackTrace();
	     throw e;
           } 
   }
}
