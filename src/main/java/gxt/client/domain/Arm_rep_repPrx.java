package gxt.client.domain;

import java.util.Date;
import java.util.List;

import jpa.rep.Arm_rep_rep;



import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value = Arm_rep_rep.class, locator = gxt.server.domain.Arm_rep_repLoc.class)
public interface Arm_rep_repPrx extends EntityProxy{
	public long getId();
	public void setId(long id);
	public String getAlias();
	public void setAlias(String alias);
	public Date getChanged();
	public void setChanged(Date changed);
	public String getRformat();
	public void setRformat(String rformat);
	public String getRlabel();
	public void setRlabel(String rlabel);
	public String getRname();
	public void setRname(String rname);
	public String getXlsauto();
	public void setXlsauto(String xlsauto);
	public List<Arm_rep_repParPrx> getArm_rep_repPars();
//	public void setArm_rep_repPars(List<Arm_rep_repParPrx> arm_rep_repPars);
	public List<Arm_rep_repRolePrx> getArm_rep_repRole();
	public void setArm_rep_repRole(List<Arm_rep_repRolePrx> arm_rep_repRole);

    Integer getVersion();
    
}
