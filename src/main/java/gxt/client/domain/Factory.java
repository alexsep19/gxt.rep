package gxt.client.domain;

import gxt.server.domain.Dao;

import java.util.Date;
import java.util.List;

import gxt.server.domain.DaoServ;
import gxt.server.domain.DdLoadResultBean;
import gxt.server.domain.FilePagingLoadResultBean;
import gxt.server.domain.LogPagingLoadResultBean;
import gxt.server.domain.ParsLoadResultBean;
import gxt.server.domain.RepLoadResultBean;
import gxt.server.domain.RoleLoadResultBean;
import gxt.server.domain.SmsLoadResultBean;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.Service;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface Factory extends RequestFactory{
//	public final static int SQL_ALLDD = 1;
//	public final static int SQL_ALLROLE = 1;
	
    RepRequest creRepRequest();
    @Service(value = Dao.class, locator = DaoServ.class)
    interface RepRequest extends RequestContext {
	Request<String> getUserInfo();
	Request<String> getFIs();
	Request<String> getRole();
	Request<String> getLogin();
	Request<List<String>> getSessionUsers();
	
	Request<Void> merg(Arm_rep_paramPrx rec);
	Request<Void> merg(Arm_rep_paramDdPrx rec);
	Request<Void> mergRole(Arm_rep_repRolePrx rec, Arm_rep_repRolePrx oldRec, boolean isIns);
	
	Request<Void> removParam(Arm_rep_paramPrx rec);
	Request<Void> removDd(Arm_rep_paramDdPrx rec);
	Request<Void> removRole(Arm_rep_repRolePrx rec);
	
	Request<List<Arm_rep_paramDdPrx>> getAllDd();
	Request<List<Arm_users_grantsDictPrx>> getAllRole();
	
	Request<List<Arm_rep_repPrx>> getUserReps(String login);
	Request<List<listDataPrx>> getParamDd(Arm_rep_paramDdPrx ddId);
	
	@ProxyFor(LogPagingLoadResultBean.class)
	public interface LogPagingLoadResultProxy extends ValueProxy, PagingLoadResult<RsysLogPrx> {
	    @Override
	    public List<RsysLogPrx> getData();
	  }
	Request<LogPagingLoadResultProxy> getListLog(int start, int limit, List<? extends SortInfo> sortInfo, List<? extends FilterConfig> filterConfig);

	@ProxyFor(FilePagingLoadResultBean.class)
	public interface FilePagingLoadResultProxy extends ValueProxy, PagingLoadResult<FileDataPrx> {
        @Override
	    public List<FileDataPrx> getData();
	  }
	Request<FilePagingLoadResultProxy> getFiles(int offset, int limit, List<? extends SortInfo> sortInfo/*, List<? extends FilterConfig> filterConfig*/);
	
	Request<List<WorkerPrx>> getRunReps();
	Request<List<WorkerPrx>> addRunReps(String repName, String repId, String repAlias, String repFormat, String param);
	
	Request<String> getMaxDateStat();
	Request<List<listDataPrx>> getNewSvnJaspers();
	
	@ProxyFor(ParsLoadResultBean.class)
	public interface ParsLoadResultProxy extends ValueProxy, ListLoadResult<Arm_rep_paramPrx> {
	    @Override
	    public List<Arm_rep_paramPrx> getData();
	  }
	Request<ParsLoadResultProxy> getListPars(List<? extends SortInfo> sortInfo);
    
	@ProxyFor(SmsLoadResultBean.class)
	public interface SmsLoadResultProxy extends ValueProxy, ListLoadResult<SmsArchivePrx> {
	    @Override
	    public List<SmsArchivePrx> getData();
	  }
	Request<SmsLoadResultProxy> getListSms(List<? extends SortInfo> sortInfo, String ph, Date from, Date to);
    
	@ProxyFor(DdLoadResultBean.class)
	public interface DdLoadResultProxy extends ValueProxy, ListLoadResult<Arm_rep_paramDdPrx> {
	    @Override
	    public List<Arm_rep_paramDdPrx> getData();
	  }
	Request<DdLoadResultProxy> getListDd(List<? extends SortInfo> sortInfo);
    
	@ProxyFor(RepLoadResultBean.class)
	public interface RepLoadResultProxy extends ValueProxy, ListLoadResult<Arm_rep_repPrx> {
	    @Override
	    public List<Arm_rep_repPrx> getData();
	  }
	Request<RepLoadResultProxy> getListReps(List<? extends SortInfo> sortInfo);
	
	@ProxyFor(RoleLoadResultBean.class)
	public interface RoleLoadResultProxy extends ValueProxy, ListLoadResult<Arm_rep_repRolePrx>{
	    @Override
	    public List<Arm_rep_repRolePrx> getData();
	}
	Request<RoleLoadResultProxy> getListRole(List<? extends SortInfo> sortInfo, Arm_rep_repPrx r);
	
    }
}
