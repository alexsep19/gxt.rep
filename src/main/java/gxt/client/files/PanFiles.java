package gxt.client.files;

import java.util.ArrayList;
import java.util.List;

import gxt.client.domain.Factory;
import gxt.client.domain.Factory.RepRequest;
import gxt.client.domain.FileDataPrx;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class PanFiles extends ContentPanel{
    private static int PAGE_SIZE = 40;
    interface FileProxyProperties extends PropertyAccess<FileDataPrx> {
	    @Path("name")
	    ModelKeyProvider<FileDataPrx> key();
	    ValueProvider<FileDataPrx, String> getName();
	    ValueProvider<FileDataPrx, String> getSize();
	    ValueProvider<FileDataPrx, String> getCreated();
	  }


    public PanFiles(final Factory fct){
	setCollapsible(false);
	getHeader().addStyleName("txt_center");
	setHeadingText("Файлы");
	addStyleName("margin-10");
	setPixelSize(400, 900);
	
        RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<FileDataPrx>> proxy = 
        	new RequestFactoryProxy<FilterPagingLoadConfig, PagingLoadResult<FileDataPrx>>() {
            @Override
            public void load(FilterPagingLoadConfig loadConfig, Receiver<? super PagingLoadResult<FileDataPrx>> receiver) {
              RepRequest req = fct.creRepRequest();
              List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
//              List<FilterConfig> filterConfig = createRequestFilterConfig(req, loadConfig.getFilters());
              req.getFiles(loadConfig.getOffset(), loadConfig.getLimit(), sortInfo).to(receiver).fire();;
            }};

        final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<FileDataPrx>> loader = 
             new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<FileDataPrx>>(proxy) {
        	@Override
        	protected FilterPagingLoadConfig newLoadConfig() {
        	   return new FilterPagingLoadConfigBean();
            }};
        loader.setRemoteSort(true);    
        FileProxyProperties props = GWT.create(FileProxyProperties.class);
        ListStore<FileDataPrx> store = new ListStore<FileDataPrx>(props.key());
        loader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, FileDataPrx, PagingLoadResult<FileDataPrx>>(store));
        final PagingToolBar toolBar = new PagingToolBar(PAGE_SIZE);
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");
        toolBar.bind(loader);
        ColumnConfig<FileDataPrx, String> colName = new ColumnConfig<FileDataPrx, String>(props.getName(), 100, "Файл");
        ColumnConfig<FileDataPrx, String> colSize = new ColumnConfig<FileDataPrx, String>(props.getSize(), 50, "Размер");
        ColumnConfig<FileDataPrx, String> colCre = new ColumnConfig<FileDataPrx, String>(props.getCreated(), 50, "Создание");
        List<ColumnConfig<FileDataPrx, ?>> l = new ArrayList<ColumnConfig<FileDataPrx, ?>>();
        l.add(colName);
        l.add(colSize);
        l.add(colCre);
        ColumnModel<FileDataPrx> cm = new ColumnModel<FileDataPrx>(l);
        Grid<FileDataPrx> view = new Grid<FileDataPrx>(store, cm) {
            @Override
            protected void onAfterFirstAttach() {
              super.onAfterFirstAttach();
              Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//+                @Override
                public void execute() {
                  loader.load();
                }
              }); }};
     
       view.getView().setForceFit(true);
       view.setLoadMask(true);
       view.setLoader(loader);
       view.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<FileDataPrx>(){
//+ 	    @Override
	    public void onSelectionChanged(SelectionChangedEvent<FileDataPrx> event) {
 		Window.open(GWT.getModuleBaseURL() + "srvFile?"+event.getSource().getSelectedItem().getName() , "", "");// "_self", "enabled"
   	     }    });
       VerticalLayoutContainer con = new VerticalLayoutContainer();
       con.setBorders(true);
       con.add(view, new VerticalLayoutData(1, 1));
       con.add(toolBar, new VerticalLayoutData(1, -1));
       setWidget(con);

    }
}
