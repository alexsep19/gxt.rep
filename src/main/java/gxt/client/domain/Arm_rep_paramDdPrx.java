package gxt.client.domain;

import jpa.rep.Arm_rep_paramDd;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Arm_rep_paramDd.class, locator = gxt.server.domain.Arm_rep_paramDdLoc.class)
public interface Arm_rep_paramDdPrx extends EntityProxy{
    public static final int LEN_alias = 50;
    public static final int LEN_ddType = 1;
    public static final int LEN_sqlBody = 1000;
	
    public long getId();
    public String getAlias();
	public void setAlias(String alias);
	public String getDdType();
	public void setDdType(String ddType);
	public String getSqlBody();
	public void setSqlBody(String sqlBody);
    
    public Integer getVersion();
}
