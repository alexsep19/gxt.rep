package gxt.client.domain;

import java.util.Date;

import jpa.other.RsysLog;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = RsysLog.class, locator = gxt.server.domain.RsysLogLoc.class)
public interface RsysLogPrx extends EntityProxy{
    Integer getVersion();
	public long getId();
//	public void setId(long id);
	public String getClassmet();
	public void setClassmet(String classmet);
	public Date getEventdate();
	public String getJtrace();
	public String getLevellog();
	public String getMessage();
	public String getMesstype();
	public String getProject();
	public Arm_userPrx getUserId();
	public String getStrEventdate();
	public String getUserString();
}
