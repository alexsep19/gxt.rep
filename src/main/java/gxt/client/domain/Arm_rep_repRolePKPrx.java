package gxt.client.domain;

import jpa.rep.Arm_rep_repRolePK;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = Arm_rep_repRolePK.class)
public interface Arm_rep_repRolePKPrx extends ValueProxy{
	public long getRepId();
	public void setRepId(long repId);
	public long getRoleId();
	public void setRoleId(long roleId);
	
//	public Integer getVersion();
}
