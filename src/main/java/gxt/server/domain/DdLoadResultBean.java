package gxt.server.domain;

import java.util.List;
import jpa.rep.Arm_rep_paramDd;

import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class DdLoadResultBean extends ListLoadResultBean<Arm_rep_paramDd>{
	private static final long serialVersionUID = 1242430613921710050L;
	public DdLoadResultBean() {
	// TODO Auto-generated constructor stub
    }
    public DdLoadResultBean(List<Arm_rep_paramDd> list) {
	      super(list);
	    }
    public Integer getVersion() { return 1;}

}
