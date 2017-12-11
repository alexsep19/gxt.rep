package gxt.server.domain;

import jpa.user.Arm_users_grantsDict;

import com.google.web.bindery.requestfactory.shared.Locator;

public class Arm_users_grantsDictLoc extends Locator<Arm_users_grantsDict, Long>{
    @Override
    public Arm_users_grantsDict create(Class<? extends Arm_users_grantsDict> clazz) {
	// TODO Auto-generated method stub
	return new Arm_users_grantsDict(); 
    }

    @Override
    public Arm_users_grantsDict find(Class<? extends Arm_users_grantsDict> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findArm_users_grantsDict(id);
    }

    @Override
    public Class<Arm_users_grantsDict> getDomainType() {
	// TODO Auto-generated method stub
	return Arm_users_grantsDict.class;
    }

    @Override
    public Long getId(Arm_users_grantsDict domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(Arm_users_grantsDict domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
