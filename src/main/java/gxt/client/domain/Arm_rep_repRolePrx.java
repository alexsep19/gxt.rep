package gxt.client.domain;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

import jpa.rep.Arm_rep_rep;
import jpa.rep.Arm_rep_repRole;
import jpa.rep.Arm_rep_repRolePK;
import jpa.user.Arm_users_grantsDict;

@ProxyFor(value = Arm_rep_repRole.class, locator = gxt.server.domain.Arm_rep_repRoleLoc.class)
public interface Arm_rep_repRolePrx extends EntityProxy{
	public Arm_rep_repRolePKPrx getId();
	public void setId(Arm_rep_repRolePKPrx id);
//	public int getHashCode();
	public long getSum();
	public void setSum(long sum);
	public Arm_rep_repPrx getArm_rep_rep();
	public void setArm_rep_rep(Arm_rep_repPrx arm_rep_rep);
	
	public Arm_users_grantsDictPrx getArm_users_grantsDict();
	public void setArm_users_grantsDict(Arm_users_grantsDictPrx arm_users_grantsDict);
	
	public Integer getVersion();
}
