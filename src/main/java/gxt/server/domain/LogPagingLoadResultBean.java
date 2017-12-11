package gxt.server.domain;

import java.util.List;

import jpa.other.RsysLog;

import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class LogPagingLoadResultBean extends PagingLoadResultBean<RsysLog> {
    private static final long serialVersionUID = 1L;
    protected LogPagingLoadResultBean() {
       
    }
    public LogPagingLoadResultBean(List<RsysLog> list, int totalLength, int offset) {
      super(list, totalLength, offset);
    }
    public Integer getVersion() {
	      return 1;
  }
  }
