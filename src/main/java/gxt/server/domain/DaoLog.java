package gxt.server.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jpa.other.RsysLog;
import jpa.user.Arm_user;

import anytools.JsonLog;

public class DaoLog {
	private static final EntityManagerFactory emfUser = Persistence.createEntityManagerFactory("jpaUser");
	private static final int MAX_REC_E = 100;
	private static final int MAX_REC_M = 1000;
	
	public static EntityManager emUser() {
		EntityManager em = emfUser.createEntityManager();
		em.setFlushMode(FlushModeType.COMMIT);
		return em;
    }
	public DaoLog() {}

	public void InitDb(){
		EntityManager em = emUser();
	try {
  	 List<Object[]> l = (List<Object[]>)em.createQuery("select count(l.levellog) num, l.levellog level from RsysLog l group by l.levellog").getResultList();
		long e = 0, m = 0;
		for(Object[] rec:l){
			if (((String)rec[1]).equals(JsonLog.LEVEL_E)) e = (Long)rec[0];
			else if(((String)rec[1]).equals(JsonLog.LEVEL_M)) m = (Long)rec[0];
		}
		if (e != MAX_REC_E){
		  if (e < MAX_REC_E) AddLogRec(JsonLog.LEVEL_E, MAX_REC_E - e, em);
		  else DelLogRec(JsonLog.LEVEL_E, e - MAX_REC_E, em);
		}
		if (m != MAX_REC_M){
		  if (m < MAX_REC_M) AddLogRec(JsonLog.LEVEL_M, MAX_REC_M - m, em);
		  else DelLogRec(JsonLog.LEVEL_M, m - MAX_REC_M, em);
		}

	}catch (Exception re) {
	    re.printStackTrace();
	    throw new RuntimeException(re.getMessage());
	}finally{
	 em.close();	
	}
	}
	
	void AddLogRec(String Type, long num, EntityManager em) throws Exception{
		if (num <= 0) return;
		Arm_user user = new Arm_user();
		user.setId(JsonLog.DEF_USER);
		user = em.find(Arm_user.class, user.getId()); 
		EntityTransaction tr = em.getTransaction();
	    tr.begin();
		for(int i = 0; i < num; i++){
		  RsysLog rl = new RsysLog();
		  rl.setUserId(user);
		  rl.setEventdate(new Date());
		  rl.setLevellog(Type);
		  em.persist(rl);
		}
		tr.commit();
	}
	void DelLogRec(String Type, long num, EntityManager em) throws Exception{
		if (num <= 0) return;
		Query sql = em.createQuery("select l.id from RsysLog l where l.levellog=?1 order by l.eventdate ASC").setParameter(1, Type);
		sql.setFirstResult(1);
		sql.setMaxResults((int)num);
		List<Long> l = (List<Long>)sql.getResultList();
		RsysLog rl;
		EntityTransaction tr = em.getTransaction();
	    tr.begin();
		for(Long rec: l){
		  rl = em.find(RsysLog.class, rec);
		  em.remove(rl);
		}
		tr.commit();
	}

	public void WriteLog(JsonLog Rec) {
		EntityManager em = emUser();
	try {
		List<RsysLog> l = (List<RsysLog>)em.createQuery("select l.id from RsysLog l where l.levellog=?1 order by l.eventdate ASC").
		   setParameter(1, Rec.getLevelLog()).setFirstResult(1).setMaxResults(1).getResultList();		

		Arm_user user = new Arm_user();
		user.setId(Rec.getUser());
		user = em.find(Arm_user.class, user.getId()); 
		EntityTransaction tr = em.getTransaction();
	    tr.begin();
	    RsysLog rl = em.find(RsysLog.class, l.get(0));
	    rl.setClassmet(Rec.getClassMet());
	    rl.setEventdate(Rec.getDate());
	    rl.setJtrace(Rec.getTrace());
	    rl.setMessage(Rec.getMessage());
	    rl.setMesstype(Rec.getMessType());
	    rl.setProject(Rec.getProject());
	    rl.setUserId(user);
		em.merge(rl);
		tr.commit();
	}catch (Exception re) {
	    re.printStackTrace();
	    throw new RuntimeException(re.getMessage());
	}finally{
		 em.close();	
		}
	}
}
