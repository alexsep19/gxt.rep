package gxt.client.reps;

import gxt.client.domain.Arm_rep_repPrx;
import gxt.client.domain.Factory;
import gxt.client.domain.Factory.RepRequest;
import gxt.client.domain.WorkerPrx;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;

import com.google.gwt.editor.client.Editor.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.state.client.GridStateHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.ParseErrorHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.info.Info;

public class PanReps extends ContentPanel {
    private static Logger rootLogger = Logger.getLogger("");
    private Factory repFct;
    final ToolButton tbRef;
    String login;
    AccordionLayoutContainer contColl;
    ContentPanel panColl;
    final Grid<WorkerPrx> grid;
    final ListStore<WorkerPrx> store;
    interface WorkerProperties extends PropertyAccess<WorkerPrx> {
	    @Path("fId")
	    ModelKeyProvider<WorkerPrx> key();
	    ValueProvider<WorkerPrx, String> getFId();
	    ValueProvider<WorkerPrx, String> getFRep();
	    ValueProvider<WorkerPrx, String> getFFileName();
	    ValueProvider<WorkerPrx, String> getFStartTime();
	  }

    public PanReps(Factory Fct, String Login){
	login = Login;
//	rootLogger.log(Level.INFO,  "login = "+ login);
	repFct = Fct;//GWT.create(Factory.class);

	setCollapsible(false);
	getHeader().addStyleName("txt_center");
	setHeadingText("Отчеты");
	addStyleName("margin-10");
	
	final ContentPanel panReps = new ContentPanel();
	repFct.creRepRequest().getMaxDateStat().fire(new Receiver<String>() {
	    public void onSuccess(String data) {
		panReps.setHeadingText("Док.статистика " + data);
	    }
	});
	panReps.getHeader().addStyleName("txt_center");
	panReps.setBodyBorder(false);
//	panReps.setHeight("800px");
//	panReps.setPixelSize(200, 400);
//	panReps.setWidth(200);
//	panReps.addStyleName("margin-10");
	
	contColl = new AccordionLayoutContainer();
	contColl.setExpandMode(ExpandMode.MULTI);
	panReps.add(contColl);
	
	final AccordionLayoutAppearance appearance = GWT.<AccordionLayoutAppearance> create(AccordionLayoutAppearance.class);
 
	final VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);
//==== Grid	
        final ContentPanel panRR = new ContentPanel();
	panRR.setHeadingText("Запущенные отчеты");
	panRR.getHeader().addStyleName("txt_center");
	panRR.setBodyBorder(false);
	panRR.setHeight("100px");
	tbRef= new ToolButton(ToolButton.REFRESH, new SelectHandler() {
        @Override  
        public void onSelect(SelectEvent event) {   
        	tbRef.setVisible(false);
        	repFct.creRepRequest().getRunReps().fire(new Receiver<List<WorkerPrx>>() {
        	    public void onSuccess(List<WorkerPrx> data) {
        	      grid.getStore().clear();
        		  grid.getStore().addAll(data);
        		  grid.getView().refresh(false);
        		  tbRef.setVisible(true);
        	    }
        	});
           }    });
	tbRef.setTitle("Обновить");
	panRR.getHeader().addTool(tbRef);
//	panRR.setWidth("450px");

        WorkerProperties props = GWT.create(WorkerProperties.class);
	ColumnConfig<WorkerPrx, String> idCol = new ColumnConfig<WorkerPrx, String>(props.getFId(), 50, "№");
	ColumnConfig<WorkerPrx, String> repCol = new ColumnConfig<WorkerPrx, String>(props.getFRep(), 100, "Отчет");
	ColumnConfig<WorkerPrx, String> fileCol = new ColumnConfig<WorkerPrx, String>(props.getFFileName(), 300, "Файл");
	ColumnConfig<WorkerPrx, String> startCol = new ColumnConfig<WorkerPrx, String>(props.getFStartTime(), 130, "Время запуска");
	List<ColumnConfig<WorkerPrx, ?>> l = new ArrayList<ColumnConfig<WorkerPrx, ?>>();
	l.add(idCol);
	l.add(repCol);
	l.add(fileCol);
	l.add(startCol);
        ColumnModel<WorkerPrx> cm = new ColumnModel<WorkerPrx>(l);
	store = new ListStore<WorkerPrx>(props.key());
	grid = new Grid<WorkerPrx>(store, cm);

	repFct.creRepRequest().getRunReps().fire(new Receiver<List<WorkerPrx>>() {
	    public void onSuccess(List<WorkerPrx> data) {
//		rootLogger.log(Level.INFO,  "data = "+ data.size());
		grid.getStore().addAll(data);
		grid.getView().refresh(false);
	    }
	});
	
//	grid.getView().setAutoExpandColumn(nameCol);
	grid.getView().setStripeRows(true);
	grid.getView().setColumnLines(true);
        grid.setBorders(false);
        grid.setColumnReordering(true);
        grid.setStateful(true);
//        GridStateHandler<WorkerPrx> state = new GridStateHandler<WorkerPrx>(grid);
//        state.loadState();
//        grid.setStateId("gridExample");
        panRR.add(grid);
	con.add(panRR, new VerticalLayoutData(-1, -1));      
//==============================	
	repFct.creRepRequest().getUserReps(login).with("arm_rep_repPars.arm_rep_param","arm_rep_repPars.arm_rep_param.arm_rep_paramDd").fire(new Receiver<List<Arm_rep_repPrx>>() {
	      public void onSuccess(List<Arm_rep_repPrx> data) {
		  for(Arm_rep_repPrx it: data){
		    panColl = new ContentPanel(appearance);
		    panColl.setAnimCollapse(false);
		    panColl.setExpanded(false);
		    panColl.setHeadingText(it.getId() + "." + it.getRlabel() + " (имя файла "+it.getRname()+"_*"+/*(adm?", "+it.getAlias():"")+*/")");
		    panColl.setBodyStyleName("pad-text");
		    contColl.add(panColl); 
		    RepEditor re = new RepEditor(repFct);
//		    rootLogger.log(Level.INFO,  "panColl.add(re)");
		    panColl.add(re);
		    re.edit(it, grid);
		  }
//		  panReps.forceLayout();
	         con.add(panReps, new VerticalLayoutData(-1, -1));
//	         delLoading(viewport);
	         delLoading();
	      }});

        setWidget(con);
    }
    
    protected void delLoading(){
    }
//    private void delLoading(Viewport v){
//       Element e = Document.get().getElementById("loading");
//       if (e != null) {
//        e.removeFromParent();
//        v.setVisible(true);
//       }
//    }
}
