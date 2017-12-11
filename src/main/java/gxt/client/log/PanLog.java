package gxt.client.log;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;

import gx.client.panpage.PanPage;
import gxt.client.domain.Arm_userPrx;
import gxt.client.domain.Factory;
import gxt.client.domain.RsysLogPrx;
import gxt.client.domain.SmsArchivePrx;
import gxt.client.domain.Factory.RepRequest;

public class PanLog extends ContentPanel{
	private static int PAGE_SIZE = 35;
    private static final int PAN_WIDTH = 750;
    private static final int PAN_HEIGHT = 850;

    interface LogProperties extends PropertyAccess<RsysLogPrx> {
	    ModelKeyProvider<RsysLogPrx> id();
//	    ValueProvider<RsysLogPrx, String> classmet();
//	    ValueProvider<RsysLogPrx, String> jtrace();
	    ValueProvider<RsysLogPrx, String> message();
	    ValueProvider<RsysLogPrx, String> messtype();
	    ValueProvider<RsysLogPrx, String> project();
	    ValueProvider<RsysLogPrx, Arm_userPrx> userId();
	    ValueProvider<RsysLogPrx, String> userString();
//	    ValueProvider<RsysLogPrx, String> levellog();
//	    ValueProvider<RsysLogPrx, String> eventdate();
	    ValueProvider<RsysLogPrx, String> strEventdate();
	  }
    private final LogProperties propLog = GWT.create(LogProperties.class);

  //===================================================  
    public PanLog(final Factory fct) {
		setCollapsible(false);
		getHeader().addStyleName("txt_center");
		setHeadingText("Журнал");
		addStyleName("margin-10");
		setPixelSize(1000, 900);
		//==================== Panel Log
		PanPage<RsysLogPrx> panLog = new PanPage<RsysLogPrx>(PAN_WIDTH, PAN_HEIGHT, PAGE_SIZE, "Журнал сообщений"){
//			ColumnConfig<RsysLogPrx, String> ccClassmet;
//			ColumnConfig<RsysLogPrx, String> ccJtrace;
			ColumnConfig<RsysLogPrx, String> ccMessage;
			ColumnConfig<RsysLogPrx, String> ccMesstype;
//			ColumnConfig<RsysLogPrx, String> ccLevellog;
			ColumnConfig<RsysLogPrx, String> ccProject;
			ColumnConfig<RsysLogPrx, String> ccUserId;
			ColumnConfig<RsysLogPrx, String> ccEventdate;
			
	        public void fill(){
		      ccEventdate = new ColumnConfig<RsysLogPrx, String>( propLog.strEventdate(), 30, "Время");
	          ccProject = new ColumnConfig<RsysLogPrx, String>( propLog.project(), 20, "Проект");
	          ccMesstype = new ColumnConfig<RsysLogPrx, String>( propLog.messtype(), 20, "Тип сообщения");
//	          ccJtrace = new ColumnConfig<RsysLogPrx, String>( propLog.jtrace(), 80, "Trace");
	          ccMessage = new ColumnConfig<RsysLogPrx, String>( propLog.message(), 80, "Сообщение");
	          ccMessage.setSortable(false);
	          ccMessage.setCell(new AbstractCell<String>() {
			      @Override
			      public void render(Context context, String value, SafeHtmlBuilder sb) {
			    	 getStT().get(context.getIndex()).getLevellog();
//			    	 String style = "style='background-color: " + value + "'";
//		             sb.appendHtmlConstant("<span " + style + " >" + value + "</span>");
		             sb.appendHtmlConstant("<span style='color:" + (getStT().get(context.getIndex()).getLevellog().equals("E")?"red":"blue") + "'>" + value + "</span>");
			      } });
//	          ccLevellog = new ColumnConfig<RsysLogPrx, String>( propLog.levellog(), 20, "Тип ошибки");
//	          ccClassmet = new ColumnConfig<RsysLogPrx, String>( propLog.classmet(), 40, "Метод");
	          ccUserId = new ColumnConfig<RsysLogPrx, String>( propLog.userString(), 50, "Юзер");
//	          ccUserId = new ColumnConfig<RsysLogPrx, Arm_userPrx>( propLog.userId(), 50, "Юзер");
//		      ccUserId.setCell(new AbstractCell<Arm_userPrx>() {
//				      @Override
//				      public void render(Context context, Arm_userPrx value, SafeHtmlBuilder sb) {
//					      sb.appendHtmlConstant(new StringBuilder(value.getLastName()).append(" ")
//					    		   .append(value.getFirstName()).append(" ").append(value.getSecondName()).toString());
//				      } });
			  IdentityValueProvider<RsysLogPrx> identity = new IdentityValueProvider<RsysLogPrx>();
			  RowExpander<RsysLogPrx> expander = new RowExpander<RsysLogPrx>(identity, new AbstractCell<RsysLogPrx>() {
			      @Override
			      public void render(Context context, RsysLogPrx value, SafeHtmlBuilder sb) {
			        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>Class.Method:</b> " + value.getClassmet());
			        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>Message:</b> " + value.getMessage());
			        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>JTrace:</b> " + value.getJtrace());
			      }
			    });
			  getCcL().add(expander);
		      getCcL().add(ccEventdate);
		      getCcL().add(ccProject);
		      getCcL().add(ccMesstype);
		      getCcL().add(ccMessage);
//		      getCcL().add(ccClassmet);
		      getCcL().add(ccUserId);
//		      getCcL().add(ccJtrace);
		      setStT(new ListStore<RsysLogPrx>(propLog.id()));
		      setRfpT(new RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<RsysLogPrx>>() {
			 		public void load(FilterPagingLoadConfig loadConfig, Receiver<? super PagingLoadResult<RsysLogPrx>> receiver) {
			 		  RepRequest req = fct.creRepRequest(); 
			 		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
		              List<FilterConfig> filterConfig = createRequestFilterConfig(req, loadConfig.getFilters());//,"data.atPart"
			 		  req.getListLog(loadConfig.getOffset(), loadConfig.getLimit(), sortInfo, filterConfig)
			 		  .with("data.userId").to(receiver).fire();
			 		  }
              });
		      initValues(false, false);
		      expander.initPlugin(getGrT());
//		      SortInfo si = new SortInfo();
		      getGrT().getLoader().addSortInfo(new SortInfoBean(propLog.strEventdate(), SortDir.DESC));
		      getFilters().addFilter(new StringFilter<RsysLogPrx>(propLog.project()));
		      getFilters().addFilter(new StringFilter<RsysLogPrx>(propLog.messtype()));
		      getFilters().addFilter(new StringFilter<RsysLogPrx>(propLog.userString()));
		      getGrT().getLoader().load();
			}

			
		};
		//==================== Panel
		panLog.fill();
		setWidget(panLog);
	}

}
