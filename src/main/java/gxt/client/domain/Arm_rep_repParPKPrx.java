package gxt.client.domain;

import jpa.rep.Arm_rep_repParPK;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = Arm_rep_repParPK.class)
public interface Arm_rep_repParPKPrx extends ValueProxy{
//        public long getId();
	public long getRepsId();
	public long getParamId();
	public void setRepsId(long repsId);
	public void setParamId(long paramId);
	public Integer getVersion();
}
