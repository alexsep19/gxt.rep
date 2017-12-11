package gxt.server.domain;

import jpa.rep.Arm_rep_repRole;
import jpa.rep.Arm_rep_repRolePK;

import com.google.web.bindery.requestfactory.shared.Locator;

public class Arm_rep_repRoleLoc extends Locator<Arm_rep_repRole, Arm_rep_repRolePK>{
    @Override
    public Arm_rep_repRole create(Class<? extends Arm_rep_repRole> clazz) {
	// TODO Auto-generated method stub
	return new Arm_rep_repRole(); 
    }

    @Override
    public Arm_rep_repRole find(Class<? extends Arm_rep_repRole> clazz, Arm_rep_repRolePK id) {
	// TODO Auto-generated method stub
	return Dao.findArm_rep_repRole(id);
    }

    @Override
    public Class<Arm_rep_repRole> getDomainType() {
	// TODO Auto-generated method stub
	return Arm_rep_repRole.class;
    }

    @Override
    public Arm_rep_repRolePK getId(Arm_rep_repRole domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Arm_rep_repRolePK> getIdType() {
	// TODO Auto-generated method stub
	return Arm_rep_repRolePK.class;
    }

    @Override
    public Object getVersion(Arm_rep_repRole domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
