package gxt.client.domain;

import jpa.user.Arm_user;
import gxt.server.domain.Arm_userLoc;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value =  Arm_user.class, locator = Arm_userLoc.class)
public interface Arm_userPrx extends EntityProxy{
	public long getId();
	public String getEmail();
	public String getFI();
	public String getFirstName();
	public String getIsActive();
	public String getLastName();
	public String getSecondName();
	public String getUserStatus();
	Integer getVersion();
}
