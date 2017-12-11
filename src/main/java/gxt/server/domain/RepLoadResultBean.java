package gxt.server.domain;

import java.util.List;
import jpa.rep.Arm_rep_rep;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class RepLoadResultBean extends ListLoadResultBean<Arm_rep_rep>{
	private static final long serialVersionUID = -1434521951177361248L;
	public RepLoadResultBean() {
    }
    public RepLoadResultBean(List<Arm_rep_rep> list) {
	      super(list);
	    }
    public Integer getVersion() { return 1;}

}
