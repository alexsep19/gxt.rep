package gxt.client.domain;

import jpa.rep.Arm_rep_param;
import jpa.rep.Arm_rep_paramDd;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Arm_rep_param.class, locator = gxt.server.domain.Arm_rep_paramLoc.class)
public interface Arm_rep_paramPrx extends EntityProxy{
    public static final int LEN_maxlen = 3;
    public static final int LEN_plabel = 50;
    public static final int LEN_pname = 50;

	public long getId();
	public void setId(long id);
	public String getMaxlen();
	public String getPlabel();
	public String getPname();
	public String getPtype();
	public void setMaxlen(String l);
	public void setPlabel(String l);
	public void setPname(String l);
	public void setPtype(String l);
	public Arm_rep_paramDdPrx getArm_rep_paramDd();
	public void setArm_rep_paramDd(Arm_rep_paramDdPrx arm_rep_paramDd);
    Integer getVersion();
}
