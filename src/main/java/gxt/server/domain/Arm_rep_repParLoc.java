package gxt.server.domain;

import jpa.rep.Arm_rep_repPar;
import jpa.rep.Arm_rep_repParPK;

import com.google.web.bindery.requestfactory.shared.Locator;

public class Arm_rep_repParLoc extends Locator<Arm_rep_repPar, Arm_rep_repParPK>{
    @Override
    public Arm_rep_repPar create(Class<? extends Arm_rep_repPar> clazz) {
	// TODO Auto-generated method stub
	return new Arm_rep_repPar(); 
    }

    @Override
    public Arm_rep_repPar find(Class<? extends Arm_rep_repPar> clazz, Arm_rep_repParPK id) {
	// TODO Auto-generated method stub
	return Dao.findArm_rep_repPar(id);
    }

    @Override
    public Class<Arm_rep_repPar> getDomainType() {
	// TODO Auto-generated method stub
	return Arm_rep_repPar.class;
    }

    @Override
    public Arm_rep_repParPK getId(Arm_rep_repPar domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getId();
    }

    @Override
    public Class<Arm_rep_repParPK> getIdType() {
	// TODO Auto-generated method stub
	return Arm_rep_repParPK.class;
    }

    @Override
    public Object getVersion(Arm_rep_repPar domainObject) {
	// TODO Auto-generated method stub
	return domainObject.getVersion();
    }

}
