package gxt.server.domain;

import java.util.List;
import jpa.other.Sms_archive;

import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class SmsLoadResultBean extends ListLoadResultBean<Sms_archive>{
	private static final long serialVersionUID = 1242430613921710050L;
	public SmsLoadResultBean() {
	// TODO Auto-generated constructor stub
    }
    public SmsLoadResultBean(List<Sms_archive> list) {
	      super(list);
	}
    public Integer getVersion() { return 1;}

}
