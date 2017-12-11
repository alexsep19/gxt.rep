package gxt.client.domain;

import jpa.user.Arm_users_grantsDict;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Arm_users_grantsDict.class, locator = gxt.server.domain.Arm_users_grantsDictLoc.class)
public interface Arm_users_grantsDictPrx extends EntityProxy{
	public long getId();
	public void setId(long id);
	public String getDescription();
	public void setDescription(String description);
	public String getName();
	public void setName(String name);
	
	Integer getVersion();
}
