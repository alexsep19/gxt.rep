package gxt.server.domain;

//import gxt.client.domain.Arm_rep_repRolePrx;
//import java.util.List;

import jpa.rep.Arm_rep_rep;

import com.google.web.bindery.requestfactory.shared.Locator;

public class Arm_rep_repLoc extends Locator<Arm_rep_rep, Long>{
    @Override
    public Arm_rep_rep create(Class<? extends Arm_rep_rep> clazz) {
	// TODO Auto-generated method stub
	return new Arm_rep_rep(); 
    }

    @Override
    public Arm_rep_rep find(Class<? extends Arm_rep_rep> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findArm_rep_rep(id);
    }

    @Override
    public Class<Arm_rep_rep> getDomainType() {
	// TODO Auto-generated method stub
	return Arm_rep_rep.class;
    }

    @Override
    public Long getId(Arm_rep_rep domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(Arm_rep_rep domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }
//    public List<Arm_rep_repRolePrx> getArm_rep_repRole(){
//		return null;
//    	
//    }
}
