package gxt.server.domain;

import jpa.rep.Arm_rep_param;

import com.google.web.bindery.requestfactory.shared.Locator;

public class Arm_rep_paramLoc extends Locator<Arm_rep_param, Long>{
    @Override
    public Arm_rep_param create(Class<? extends Arm_rep_param> clazz) {
	// TODO Auto-generated method stub
	return new Arm_rep_param(); 
    }

    @Override
    public Arm_rep_param find(Class<? extends Arm_rep_param> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findArm_rep_param(id);
    }

    @Override
    public Class<Arm_rep_param> getDomainType() {
	// TODO Auto-generated method stub
	return Arm_rep_param.class;
    }

    @Override
    public Long getId(Arm_rep_param domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(Arm_rep_param domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }
}
