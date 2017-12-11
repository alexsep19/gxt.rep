package gxt.server.domain;

import java.util.List;
import jpa.user.Arm_user;

import com.google.web.bindery.requestfactory.shared.Locator;

public class Arm_userLoc extends Locator<Arm_user, Long>{
    @Override
    public Arm_user create(Class<? extends Arm_user> clazz) {
	// TODO Auto-generated method stub
	return new Arm_user(); 
    }

    @Override
    public Arm_user find(Class<? extends Arm_user> clazz, Long id) {
	// TODO Auto-generated method stub
	return Dao.findArm_user(id);
    }

    @Override
    public Class<Arm_user> getDomainType() {
	// TODO Auto-generated method stub
	return Arm_user.class;
    }

    @Override
    public Long getId(Arm_user domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	// TODO Auto-generated method stub
	return Long.class;
    }

    @Override
    public Object getVersion(Arm_user domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }
}
