package gxt.server.domain;

import java.util.List;
import jpa.rep.Arm_rep_param;

import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class ParsLoadResultBean extends ListLoadResultBean<Arm_rep_param>{
    private static final long serialVersionUID = -1922741397527217605L;
    public ParsLoadResultBean() {
	// TODO Auto-generated constructor stub
    }
    public ParsLoadResultBean(List<Arm_rep_param> list) {
	      super(list);
	    }
    public Integer getVersion() { return 1;}

}
