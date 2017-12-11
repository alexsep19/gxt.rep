package gxt.client.domain;

import java.util.Date;

import jpa.rep.Arm_rep_repPar;



import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Arm_rep_repPar.class, locator = gxt.server.domain.Arm_rep_repParLoc.class)
public interface Arm_rep_repParPrx extends EntityProxy{
	public Arm_rep_repParPKPrx getId();
//	public long getId();
	public Date getChanged();
	public void setChanged(Date changed);
	public String getLabelOrder();
	public void setLabelOrder(String labelOrder);
    public Arm_rep_paramPrx getArm_rep_param();

	Integer getVersion();
}
