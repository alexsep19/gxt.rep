package gxt.client.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import gx.client.panlist.PanList;
import gxt.client.domain.Arm_rep_repParPKPrx;
import gxt.client.domain.Arm_rep_repPrx;
import gxt.client.domain.Arm_rep_repRolePKPrx;
import gxt.client.domain.Arm_rep_repRolePrx;
import gxt.client.domain.Arm_users_grantsDictPrx;
import gxt.client.domain.Factory;
import gxt.client.domain.Factory.RepRequest;
import gxt.client.tools.PanTools.ParProperties;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import java.text.ParseException;

import jpa.user.Arm_users_grantsDict;

public class PanRep extends FramedPanel{
	protected List<ColumnConfig<Arm_rep_repPrx, ?>> ccL = new ArrayList<ColumnConfig<Arm_rep_repPrx, ?>>();
	protected RequestFactoryProxy<ListLoadConfig, ListLoadResult<Arm_rep_repPrx>> rfpT;
	protected ListStore<Arm_rep_repPrx> stT;
	protected Grid<Arm_rep_repPrx> gRep;
	Arm_rep_repPrx curRep;
	VerticalLayoutContainer vcon = new VerticalLayoutContainer();
	private static Logger rootLogger = Logger.getLogger("");
	PanList<Arm_rep_repRolePrx> panRole;
	
	interface DictProperties extends PropertyAccess<Arm_rep_repRolePrx> {
//		@Path("hashCode")
		@Path("sum")
	    ModelKeyProvider<Arm_rep_repRolePrx> id();
//	    @Path("arm_users_grantsDict.name")
	    ValueProvider<Arm_rep_repRolePrx, Arm_users_grantsDictPrx> arm_users_grantsDict();
	    @Path("arm_users_grantsDict.description")
	    ValueProvider<Arm_rep_repRolePrx, String> description();
	}
	private final DictProperties propDict = GWT.create(DictProperties.class);
	
	interface RepProperties extends PropertyAccess<Arm_rep_repPrx> {
	    ModelKeyProvider<Arm_rep_repPrx> id();
	    @Path("id")
	    ValueProvider<Arm_rep_repPrx, Long> idval();
	    ValueProvider<Arm_rep_repPrx, String> rname();
	    ValueProvider<Arm_rep_repPrx, String> rlabel();
	}
	private final RepProperties propRep = GWT.create(RepProperties.class);

	interface RoleProperties extends PropertyAccess<Arm_users_grantsDictPrx> {
	    @Path("id")
	    ModelKeyProvider<Arm_users_grantsDictPrx> id();
	    ValueProvider<Arm_users_grantsDictPrx, String> Description();
	  }
    private static final RoleProperties propRole = GWT.create(RoleProperties.class);
    ComboBox<Arm_users_grantsDictPrx> cbRole;
    final ListStore<Arm_users_grantsDictPrx> stRole = new ListStore<Arm_users_grantsDictPrx>(propRole.id());
	
    final ToolButton tbRefresh;
//-------------------------------------------------	
	public PanRep(final Factory Fct){
       addStyleName("margin-10");
       setHeadingText("Отчеты");
       setPixelSize(500, 410);
       tbRefresh = new ToolButton(ToolButton.REFRESH, new SelectHandler() {
   		  @Override public void onSelect(SelectEvent event) {
   			gRep.getLoader().load();
   		}
       	});
       tbRefresh.setTitle("Обновить");
       getHeader().addTool(tbRefresh);

// rootLogger.log(Level.INFO,  "PanRep");
       
       ColumnConfig<Arm_rep_repPrx, Long> ccId = new ColumnConfig<Arm_rep_repPrx, Long>(propRep.idval(), 30, "№");
       ccId.setCell(new AbstractCell<Long>() {
		      @Override
		      public void render(Context context, Long value, SafeHtmlBuilder sb) {
			      sb.appendHtmlConstant(String.valueOf(value));
		      } });
       ColumnConfig<Arm_rep_repPrx, String> ccName = new ColumnConfig<Arm_rep_repPrx, String>(propRep.rname(), 70, "Jasp");
       ColumnConfig<Arm_rep_repPrx, String> ccLabel = new ColumnConfig<Arm_rep_repPrx, String>(propRep.rlabel(), 300, "Наименование");
       ccL.add(ccId);
       ccL.add(ccName);
       ccL.add(ccLabel);
       ColumnModel<Arm_rep_repPrx> cmML = new ColumnModel<Arm_rep_repPrx>(ccL);
       stT = new ListStore<Arm_rep_repPrx>(propRep.id());
       rfpT = new RequestFactoryProxy<ListLoadConfig, ListLoadResult<Arm_rep_repPrx>>() {
	 		@Override
	 		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<Arm_rep_repPrx>> receiver) {
	 		  RepRequest req = Fct.creRepRequest();
	 		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
	 		  req.getListReps(sortInfo).to(receiver).fire();
	 		  Fct.creRepRequest().getAllRole().fire(new Receiver<List<Arm_users_grantsDictPrx>>(){
 		  	  @Override
 		  	  public void onSuccess(List<Arm_users_grantsDictPrx> response){
 		  		if (response != null)  cbRole.getStore().replaceAll((List<Arm_users_grantsDictPrx>)response);
 		  		else cbRole.clear();
 		  	   }});
	 		  }};
       final ListLoader<ListLoadConfig, ListLoadResult<Arm_rep_repPrx>> loaderT = new ListLoader<ListLoadConfig, ListLoadResult<Arm_rep_repPrx>>(rfpT);
       loaderT.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, Arm_rep_repPrx, ListLoadResult<Arm_rep_repPrx>>(stT));
       gRep = new Grid<Arm_rep_repPrx>(stT, cmML) {
           @Override
           protected void onAfterFirstAttach() {
             super.onAfterFirstAttach();
             Scheduler.get().scheduleDeferred(new ScheduledCommand() {
               @Override
               public void execute() {
        	   loaderT.load();
               } }); }};
       gRep.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Arm_rep_repPrx>(){
		@Override
		public void onSelectionChanged(SelectionChangedEvent<Arm_rep_repPrx> event) {
			if (event.getSelection().size() <= 0) {
				panRole.getG().setVisible(false);
				return; 
			}else if (!panRole.getG().isVisible()) panRole.getG().setVisible(true);
			curRep = event.getSource().getSelectedItem();
			panRole.getG().getLoader().load();
		}
    	   
       });
       gRep.getView().setForceFit(true);
       gRep.setLoadMask(true);
       gRep.setLoader(loaderT);
       gRep.mask("Загрузка");
       gRep.getLoader().load();
//       gRep.getSelectionModel().setSelection((List<Arm_rep_repPrx>) gRep.getStore().get(0));
//============================= ROLE ======================================       
//rootLogger.log(Level.INFO,  "ROLE");
   	    panRole = new PanList<Arm_rep_repRolePrx>(100, 150, "Роли"){
//   		Arm_rep_repPrx curRep;
	    ColumnConfig<Arm_rep_repRolePrx, Arm_users_grantsDictPrx> ccVal;

	    ColumnConfig<Arm_rep_repRolePrx, String> ccName;
	    RepRequest reqIns;
	    @Override
        public void fill(){
	       ccVal = new ColumnConfig<Arm_rep_repRolePrx, Arm_users_grantsDictPrx>(propDict.arm_users_grantsDict(), 40, "Name");
   		   ccVal.setCell(new AbstractCell<Arm_users_grantsDictPrx>() {
				@Override
				public void render(Context context,	Arm_users_grantsDictPrx value, SafeHtmlBuilder sb) {
					sb.appendHtmlConstant(value.getName());
				}} );
	       ccName = new ColumnConfig<Arm_rep_repRolePrx, String>(propDict.description(), 40, "Description");
		   getCcL().add(ccVal);
	       getCcL().add(ccName);
	       setStT(new ListStore<Arm_rep_repRolePrx>(propDict.id()));
//rootLogger.log(Level.INFO,  "fill() = setRfpT");
//rootLogger.log(Level.INFO,  "fill() = " + gRep.getSelectionModel().getSelection().size());
	       setRfpT(new RequestFactoryProxy<ListLoadConfig, ListLoadResult<Arm_rep_repRolePrx>>() {
	 		@Override
	 		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<Arm_rep_repRolePrx>> receiver) {
              if (curRep != null){ 
            	 setHeadingText("Роли "+curRep.getRname());
	 		     RepRequest req = Fct.creRepRequest();
	 		     List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
	 		     req.getListRole(sortInfo, curRep).with("data.arm_users_grantsDict").to(receiver).fire();
              } else G.setVisible(false);
	 		  }});
//rootLogger.log(Level.INFO,  "initValues()");
	       initValues(true, true, true);
	       cbRole = new ComboBox<Arm_users_grantsDictPrx>(stRole, new LabelProvider<Arm_users_grantsDictPrx>(){
	            @Override
	            public String getLabel(Arm_users_grantsDictPrx item) {
	              return item==null ? "": item.getName()+" "+item.getDescription();
	            }
	        });
		   cbRole.setPropertyEditor(new PropertyEditor<Arm_users_grantsDictPrx>() {
		          @Override
		          public Arm_users_grantsDictPrx parse(CharSequence text) throws ParseException {
		            for(Arm_users_grantsDictPrx it: stRole.getAll()){
		        	if (text.equals(it.getName()+" "+it.getDescription())) return it;
		            }
		            return null;
		          }
		          @Override
		          public String render(Arm_users_grantsDictPrx object) {
		            return object == null ? "XXX" : object.getName()+" "+object.getDescription();
		          }});
		   cbRole.setTriggerAction(TriggerAction.ALL);
		   cbRole.setForceSelection(true);

	       getEditing().addEditor(ccVal, cbRole);
//	       getEditing().addEditor(ccName, txName);

	    }
	    @Override
	    public void mergItem(Arm_rep_repRolePrx item){
	       RepRequest req = null;
//rootLogger.log(Level.INFO,  "isIns = "+isIns);
           Arm_rep_repRolePrx editItem;
           Arm_rep_repRolePKPrx pk;
           if (isIns) {req = reqIns; 
             editItem = req.edit(item);
             pk = reqIns.create(Arm_rep_repRolePKPrx.class);
             pk.setRepId(editItem.getArm_rep_rep().getId());
             pk.setRoleId(editItem.getArm_users_grantsDict().getId());
             editItem.setId(pk);
//        	 item.getId().setRoleId(cbRole.getCurrentValue().getId());
//        	 item.setArm_users_grantsDict(cbRole.getCurrentValue());
//        	 reqIns.mergRole( item, item, isIns).fire(mergReceiver);
           }else{
             req = Fct.creRepRequest();
             editItem = req.edit(item);
//           pk = req.create(Arm_rep_repRolePKPrx.class);
//             pk = editItem.getId();
//             pk.setRoleId(editItem.getArm_users_grantsDict().getId());
//             editItem.setId(pk);
           }
//rootLogger.log(Level.INFO,  "pk");           
           editItem.setArm_users_grantsDict(cbRole.getCurrentValue());
rootLogger.log(Level.INFO,  "setArm_users_grantsDict");           
           req.mergRole(editItem, item, isIns).fire(mergReceiver);
           isIns = false;
	    }
	    @Override
	    public void insItem(){
//rootLogger.log(Level.INFO,  "insItem()");
	     	reqIns = Fct.creRepRequest();
	     	Arm_rep_repRolePrx o = reqIns.create(Arm_rep_repRolePrx.class);
//rootLogger.log(Level.INFO,  "Arm_rep_repRolePrx o");
//        	Arm_rep_repRolePKPrx pk = reqIns.create(Arm_rep_repRolePKPrx.class);
	     	o.setArm_rep_rep(curRep);
//rootLogger.log(Level.INFO,  "bRole.getCurrentValue() = "+stRole.get(0));
            o.setArm_users_grantsDict(stRole.get(0));
//            pk.setRepId(curRep.getId());
//            pk.setRoleId(cbRole.getCurrentValue().getId());
//            o.setId(pk);
	        stT.add(0, o);
//rootLogger.log(Level.INFO,  "end insItem()");
	    }
	    @Override
	    public String getItemName(Arm_rep_repRolePrx item){
		return item.getArm_users_grantsDict().getDescription();
	    }
	    @Override
	    public void delItem(Arm_rep_repRolePrx item, Receiver<Void> R){
	    	Fct.creRepRequest().removRole(item).fire(R);
	    }
//	    @Override
//	    protected void beforEdit(){
//	    	txPlabel.getCell().getInputElement(txPlabel.getElement()).setMaxLength(Arm_rep_paramPrx.LEN_plabel);
//	    	txPname.getCell().getInputElement(txPname.getElement()).setMaxLength(Arm_rep_paramPrx.LEN_pname);
//	    	txMaxlen.getCell().getInputElement(txMaxlen.getElement()).setMaxLength(Arm_rep_paramPrx.LEN_maxlen);
//	    }

   	   };
//rootLogger.log(Level.INFO,  "panRole.fill()");
   	   panRole.fill();
//=========================================================================       
       vcon.setBorders(true);
       vcon.add(gRep, new VerticalLayoutData(1, 1));
       vcon.add(panRole, new VerticalLayoutData(1, -1, new Margins(5)));
       setWidget(vcon);
	}
	
}
