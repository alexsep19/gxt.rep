package gxt.server.domain;

import java.util.List;

import jpa.rep.Arm_rep_repRole;

import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class RoleLoadResultBean extends ListLoadResultBean<Arm_rep_repRole>{
	private static final long serialVersionUID = -8048694402322838538L;
	public RoleLoadResultBean() {
    }
    public RoleLoadResultBean(List<Arm_rep_repRole> list) {
	      super(list);
	    }
    public Integer getVersion() { return 1;}

}
