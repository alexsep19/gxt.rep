package gxt.server.domain;

import jpa.rep.Arm_rep_paramDd;

import com.google.web.bindery.requestfactory.shared.Locator;

public class Arm_rep_paramDdLoc extends Locator<Arm_rep_paramDd, Long>{
    @Override
    public Arm_rep_paramDd create(Class<? extends Arm_rep_paramDd> clazz) {
	// TODO Auto-generated method stub
	return new Arm_rep_paramDd(); 
    }

    @Override
    public Arm_rep_paramDd find(Class<? extends Arm_rep_paramDd> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findArm_rep_paramDd(id);
    }

    @Override
    public Class<Arm_rep_paramDd> getDomainType() {
	// TODO Auto-generated method stub
	return Arm_rep_paramDd.class;
    }

    @Override
    public Long getId(Arm_rep_paramDd domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(Arm_rep_paramDd domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }
}