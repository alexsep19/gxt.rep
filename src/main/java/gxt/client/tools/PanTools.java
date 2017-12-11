package gxt.client.tools;

import java.util.List;
import java.text.ParseException;
import gx.client.panlist.PanList;
import gxt.client.domain.Arm_rep_paramDdPrx;
import gxt.client.domain.Arm_rep_paramPrx;
import gxt.client.domain.Factory;
import gxt.client.domain.Factory.RepRequest;
import gxt.client.domain.LabVal;
import gxt.client.domain.listDataPrx;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.sencha.gxt.data.shared.Converter;

public class PanTools extends ContentPanel{
	private static Logger rootLogger = Logger.getLogger("");

//========================= PAR ===================	
    private static final int PAN_PAR_WIDTH = 420;
    private static final int PAN_PAR_HEIGHT = 410;
    interface ParProperties extends PropertyAccess<Arm_rep_paramPrx> {
	    ModelKeyProvider<Arm_rep_paramPrx> id();
	    ValueProvider<Arm_rep_paramPrx, String> pname();
	    ValueProvider<Arm_rep_paramPrx, String> plabel();
	    ValueProvider<Arm_rep_paramPrx, String> maxlen();
	    ValueProvider<Arm_rep_paramPrx, Arm_rep_paramDdPrx> arm_rep_paramDd();
	    ValueProvider<Arm_rep_paramPrx, String> ptype();
	  }
    private final ParProperties propPar = GWT.create(ParProperties.class);
    interface DDProperties extends PropertyAccess<Arm_rep_paramDdPrx> {
	    ModelKeyProvider<Arm_rep_paramDdPrx> id();
	  }
    private final DDProperties propDd = GWT.create(DDProperties.class);
    final ListStore<Arm_rep_paramDdPrx> stDd = new ListStore<Arm_rep_paramDdPrx>(propDd.id());
    ComboBox<Arm_rep_paramDdPrx> cbDd;
    
    interface TypeProperties extends PropertyAccess<LabVal> {
	    @Path("value")
	    ModelKeyProvider<LabVal> id();
	    ValueProvider<LabVal, String> label();
    }
    private final TypeProperties propType = GWT.create(TypeProperties.class);
    final ListStore<LabVal> stType = new ListStore<LabVal>(propType.id());
    ComboBox<LabVal> cbType;
//========================= DD ====================
    private static final int PAN_DD_WIDTH = 420;
    private static final int PAN_DD_HEIGHT = 410;
    interface DownProperties extends PropertyAccess<Arm_rep_paramDdPrx> {
	    ModelKeyProvider<Arm_rep_paramDdPrx> id();
	    @Path("id")
	    ValueProvider<Arm_rep_paramDdPrx, Long> idVal();
	    ValueProvider<Arm_rep_paramDdPrx, String> alias();
	    ValueProvider<Arm_rep_paramDdPrx, String> ddType();
	    ValueProvider<Arm_rep_paramDdPrx, String> sqlBody();
	  }
    private final DownProperties propDown = GWT.create(DownProperties.class);
//========================= ROLE ====================
//    interface RepProperties extends PropertyAccess<Arm_rep_repPrx> {
//        ModelKeyProvider<Arm_rep_repPrx> id();
//        @Path("rname")
//        ValueProvider<Arm_rep_repPrx, String> name();
//      }
//      private RepProperties propRep = GWT.create(RepProperties.class);
//===================================================    
    
    public PanTools(final Factory Fct) {
	setCollapsible(false);
	getHeader().addStyleName("txt_center");
	setHeadingText("Настройки");
	addStyleName("margin-10");
	setPixelSize(1000, 855);
	HtmlLayoutContainer contTools = new HtmlLayoutContainer(getImportMarkup());
	
	//==================== Panel Role
//	FramedPanel panRep = new FramedPanel();
//	VerticalLayoutContainer vconRep = new VerticalLayoutContainer();
//	panRep.addStyleName("margin-10");
//	panRep.setHeadingText("Роли");
//	panRep.setPixelSize(400, 400);

//==================== Panel Dd
	PanList<Arm_rep_paramDdPrx> panDd =  new PanList<Arm_rep_paramDdPrx>(PAN_DD_WIDTH, PAN_DD_HEIGHT, "Выпадалочки"){
	    ColumnConfig<Arm_rep_paramDdPrx, Long> ccIdVal;
	    ColumnConfig<Arm_rep_paramDdPrx, String> ccAlias;
	    ColumnConfig<Arm_rep_paramDdPrx, String> ccDdType;
	    ColumnConfig<Arm_rep_paramDdPrx, String> ccSqlBody;
	    TextField txAlias = new TextField();
	    TextField txDdType = new TextField();
	    TextArea txSqlBody = new TextArea();
	    RepRequest reqIns;
	    @Override
        public void fill(){
	       ccIdVal = new ColumnConfig<Arm_rep_paramDdPrx, Long>( propDown.idVal(), 20, "id");
	       ccIdVal.setCell(new AbstractCell<Long>() {
			      @Override
			      public void render(Context context, Long value, SafeHtmlBuilder sb) {
				      sb.appendHtmlConstant(value.toString());
			      } });
	       ccAlias = new ColumnConfig<Arm_rep_paramDdPrx, String>(propDown.alias(), 40, "Алиас");
		   ccDdType = new ColumnConfig<Arm_rep_paramDdPrx, String>(propDown.ddType(), 20, "Тип");
		   ccSqlBody = new ColumnConfig<Arm_rep_paramDdPrx, String>(propDown.sqlBody(), 100, "Sql");
		   getCcL().add(ccIdVal);
	       getCcL().add(ccAlias);
	       getCcL().add(ccDdType);
	       getCcL().add(ccSqlBody);
	       setStT(new ListStore<Arm_rep_paramDdPrx>(propDown.id()));
	       setRfpT(new RequestFactoryProxy<ListLoadConfig, ListLoadResult<Arm_rep_paramDdPrx>>() {
	 		@Override
	 		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<Arm_rep_paramDdPrx>> receiver) {
	 		  RepRequest req = Fct.creRepRequest();
	 		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
	 		  req.getListDd(sortInfo).to(receiver).fire();
	 		  }});
	       
	       initValues(true, true, true);
	       getEditing().addEditor(ccAlias, txAlias);
	       getEditing().addEditor(ccDdType, txDdType);
	       getEditing().addEditor(ccSqlBody, txSqlBody);
	    }
		    @Override
		    public void mergItem(Arm_rep_paramDdPrx item){
		       RepRequest req = null;
		       if (isIns) {req = reqIns; isIns = false;}
		       else req = Fct.creRepRequest();
		       Arm_rep_paramDdPrx editItem = req.edit(item);
		       editItem.setAlias(txAlias.getText());
		       editItem.setDdType(txDdType.getText());
		       editItem.setSqlBody(txSqlBody.getText());
		       req.merg(editItem).fire(mergReceiver);
		    }
		    @Override
		    public void insItem(){
		     	reqIns = Fct.creRepRequest();
		     	Arm_rep_paramDdPrx o = reqIns.create(Arm_rep_paramDdPrx.class);
		     	o.setAlias("");
		     	o.setDdType("");
		     	o.setSqlBody("");
		        stT.add(0, o);
		    }
		    @Override
		    public String getItemName(Arm_rep_paramDdPrx item){
			return String.valueOf(item.getId());
		    }
		    @Override
		    public void delItem(Arm_rep_paramDdPrx item, Receiver<Void> R){
		    	Fct.creRepRequest().removDd(item).fire(R);
		    }
		    @Override
		    protected void beforEdit(){
		    	txAlias.getCell().getInputElement(txAlias.getElement()).setMaxLength(Arm_rep_paramDdPrx.LEN_alias);
		    	txDdType.getCell().getInputElement(txDdType.getElement()).setMaxLength(Arm_rep_paramDdPrx.LEN_ddType);
		    	txSqlBody.getCell().getInputElement(txSqlBody.getElement()).setMaxLength(Arm_rep_paramDdPrx.LEN_sqlBody);
		    }
	};
//==================== Panel PAR	
	PanList<Arm_rep_paramPrx> panPar =  new PanList<Arm_rep_paramPrx>(PAN_PAR_WIDTH, PAN_PAR_HEIGHT, "Параметры"){
	    ColumnConfig<Arm_rep_paramPrx, String> ccPname;
	    ColumnConfig<Arm_rep_paramPrx, String> ccPlabel;
	    ColumnConfig<Arm_rep_paramPrx, String> ccMaxlen;
	    ColumnConfig<Arm_rep_paramPrx, Arm_rep_paramDdPrx> ccDd;
	    ColumnConfig<Arm_rep_paramPrx, String> ccType;

	    TextField txPname = new TextField();
	    TextField txPlabel = new TextField();
	    TextField txMaxlen = new TextField();
	    ComboBox<Arm_rep_paramDdPrx> cbDd;
	    ComboBox<LabVal> cbType;
	    RepRequest reqIns;
	    @Override
        public void fill(){
		ccPname = new ColumnConfig<Arm_rep_paramPrx, String>(propPar.pname(), 80, "Наименование");
		ccPlabel = new ColumnConfig<Arm_rep_paramPrx, String>(propPar.plabel(), 80, "Русский текст");
		ccMaxlen = new ColumnConfig<Arm_rep_paramPrx, String>(propPar.maxlen(), 30, "Длина");
		ccDd = new ColumnConfig<Arm_rep_paramPrx, Arm_rep_paramDdPrx>( propPar.arm_rep_paramDd(), 40, "id списка");
		ccDd.setCell(new AbstractCell<Arm_rep_paramDdPrx>() {
		      @Override
		      public void render(Context context, Arm_rep_paramDdPrx value, SafeHtmlBuilder sb) {
			      sb.appendHtmlConstant(String.valueOf(value.getId()));
		      } });
		ccType = new ColumnConfig<Arm_rep_paramPrx, String>( propPar.ptype(), 80, "Тип");
		ccType.setCell(new AbstractCell<String>() {
		      @Override
		      public void render(Context context, String value, SafeHtmlBuilder sb) {
//		    	  stType.findModelWithKey(value).getLabel();
		    	  for(LabVal it: stType.getAll()){
			        if (it.getValue().equals(value)) {
			        	sb.appendHtmlConstant(it.getLabel());
			        	return;
			        }
			      }
			      sb.appendHtmlConstant("XX");
		      } });
		
	        getCcL().add(ccPname);
	        getCcL().add(ccPlabel);
	        getCcL().add(ccMaxlen);
	        getCcL().add(ccDd);
	        getCcL().add(ccType);
	        setStT(new ListStore<Arm_rep_paramPrx>(propPar.id()));
	        setRfpT(new RequestFactoryProxy<ListLoadConfig, ListLoadResult<Arm_rep_paramPrx>>() {
	 		@Override
	 		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<Arm_rep_paramPrx>> receiver) {
	 		  RepRequest req = Fct.creRepRequest();
	 		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
	 		  req.getListPars(sortInfo).with("data.arm_rep_paramDd").to(receiver).fire();
              Fct.creRepRequest().getAllDd().fire(new Receiver<List>(){
		  	      @Override
		  	      public void onSuccess(List response){
		  		     if (response != null)  cbDd.getStore().replaceAll((List<Arm_rep_paramDdPrx>)response);
		  		     else cbDd.clear();
		  	      }});
	 		  }});

	        cbDd = new ComboBox<Arm_rep_paramDdPrx>(stDd, new LabelProvider<Arm_rep_paramDdPrx>(){
	            @Override
	            public String getLabel(Arm_rep_paramDdPrx item) {
	              return item==null ? "": String.valueOf(item.getId());
	            }
	        });
	    	cbDd.setPropertyEditor(new PropertyEditor<Arm_rep_paramDdPrx>() {
		          @Override
		          public Arm_rep_paramDdPrx parse(CharSequence text) throws ParseException {
		            for(Arm_rep_paramDdPrx it: stDd.getAll()){
		        	if (String.valueOf(it.getId()).equals(text)) return it;
		            }
		            return null;
		          }
		          @Override
		          public String render(Arm_rep_paramDdPrx object) {
		            return object == null ? "XXX" : String.valueOf(object.getId());
		          }});
	    	cbDd.setTriggerAction(TriggerAction.ALL);
	    	cbDd.setForceSelection(true);
 	        stType.add(new LabVal("D", "D дата"));
 	        stType.add(new LabVal("B", "B дата начала"));
 	        stType.add(new LabVal("E", "E дата конца"));
 	        stType.add(new LabVal("N", "N число"));
 	        stType.add(new LabVal("L", "L список"));
 	        stType.add(new LabVal("T", "T текст"));
 	        cbType = new ComboBox<LabVal>(stType, new LabelProvider<LabVal>(){
	            @Override
	            public String getLabel(LabVal item) {
	              return item==null ? "": item.getLabel();
	            }
	        });
 	       cbType.setPropertyEditor(new PropertyEditor<LabVal>() {
		          @Override
		          public LabVal parse(CharSequence text) throws ParseException {
		            for(LabVal it: stType.getAll()){
		        	if (it.getLabel().equals(text)) return it;
		            }
		            return null;
		          }
		          @Override
		          public String render(LabVal object) {
		            return object == null ? "XXX" : object.getLabel();
		          }});
 	       cbType.setTriggerAction(TriggerAction.ALL);
 	       cbType.setForceSelection(true);
	        initValues(true, true, true);
	        getEditing().addEditor(ccPname, txPname);
	        getEditing().addEditor(ccPlabel, txPlabel);
	        getEditing().addEditor(ccMaxlen, txMaxlen);
	        getEditing().addEditor(ccDd, cbDd);
	        getEditing().addEditor(ccType, new Converter<String, LabVal>(){
				@Override
				public String convertFieldValue(LabVal object) {
					return object == null ? "" : object.getValue();
				}
				@Override
				public LabVal convertModelValue(String object) {
					return stType.findModelWithKey(object);
				}
	        },cbType);
	    }
	    
	    @Override
	    public void mergItem(Arm_rep_paramPrx item){
	       RepRequest req = null;
	       if (isIns) {req = reqIns; isIns = false;}
	       else req = Fct.creRepRequest();
	       Arm_rep_paramPrx editItem = req.edit(item);
	       editItem.setPlabel(txPlabel.getText());
	       editItem.setPname(txPname.getText());
	       editItem.setPtype(cbType.getCurrentValue().getValue());
	       editItem.setMaxlen(txMaxlen.getText());
	       editItem.setArm_rep_paramDd(cbDd.getCurrentValue());
	       req.merg(editItem).fire(mergReceiver);
	    }
	    @Override
	    public void insItem(){
	     	reqIns = Fct.creRepRequest();
	     	Arm_rep_paramPrx o = reqIns.create(Arm_rep_paramPrx.class);
	     	o.setPlabel("");
	     	o.setPname("");
	     	o.setMaxlen("");
	     	o.setArm_rep_paramDd(stDd.get(0));
	     	o.setPtype(stType.get(0).getValue());
	        stT.add(0, o);
	    }
	    @Override
	    public String getItemName(Arm_rep_paramPrx item){
		return item.getPlabel();
	    }
	    @Override
	    public void delItem(Arm_rep_paramPrx item, Receiver<Void> R){
	    	Fct.creRepRequest().removParam(item).fire(R);
	    }
	    @Override
	    protected void beforEdit(){
	    	txPlabel.getCell().getInputElement(txPlabel.getElement()).setMaxLength(Arm_rep_paramPrx.LEN_plabel);
	    	txPname.getCell().getInputElement(txPname.getElement()).setMaxLength(Arm_rep_paramPrx.LEN_pname);
	    	txMaxlen.getCell().getInputElement(txMaxlen.getElement()).setMaxLength(Arm_rep_paramPrx.LEN_maxlen);
	    }

	};
	panPar.fill();
	panDd.fill();
//	rootLogger.log(Level.INFO,  "PanTools = PanRep");
	PanRep panRep = new PanRep(Fct); 
//	rootLogger.log(Level.INFO,  "PanTools = PanRep end");
//=======================================================================
	FramedPanel panSvn = new FramedPanel();
	panSvn.addStyleName("margin-10");
	panSvn.setHeadingText("Svn");
	panSvn.setPixelSize(500, 410);
	ToolButton btHelp = new ToolButton(ToolButton.QUESTION);
	ToolTipConfig configHelp = new ToolTipConfig();
	configHelp.setTitleHtml("Внесение изменения в отчет");
	configHelp.setBodyHtml("<div><ul style=\"list-style: disc; margin: 0px 0px 5px 15px\">"+
	                          "<li>Скопировать отчет из svn(http://msk-svn01.msk.bank.mdm/svn/hibara/jasper/jasp56)</li>"+
			                  "<li>При помощи iReport(ver.5.6) внести изменения</li>"+
			                  "<li>Закомитить отчет в svn</li>"+
			                  "<li>на форме \"svn\" нажать кнопку \"Обновить из svn\". После выполнения, появится список обновленных отчетов. Символ T после имени отчета означает успех, F - ошибку</li>"+
	                          "</ul></div>");
	configHelp.setCloseable(true);
	btHelp.setToolTipConfig(configHelp);
	panSvn.addTool(btHelp);
    final TextButton btImpSvn = new TextButton("Обновить из svn");
    final TextArea taSvnLoaded = new TextArea();
    btImpSvn.addSelectHandler(new SelectHandler() {
    	@Override 
    	public void onSelect(SelectEvent event) {
    		btImpSvn.setVisible(false);
//    		redraw();
    		Fct.creRepRequest().getNewSvnJaspers().fire(new Receiver<List<listDataPrx>>() {
    		  @Override public void onSuccess(List<listDataPrx> response) {
    	 	       taSvnLoaded.clear();
    			   if (response.size() > 0){
    			       String s = "";
    			       for(listDataPrx it: response)
    				     s = s + "\n" + it.getName()+" "+it.getValue();
    			     taSvnLoaded.setValue(s);
    			   }else taSvnLoaded.setValue("ничего нет");
    			   btImpSvn.setVisible(true);
   			    }});
    		    }
    		  });
    taSvnLoaded.setAllowBlank(true);
	taSvnLoaded.setReadOnly(true);
	taSvnLoaded.setWidth(200);
	taSvnLoaded.setHeight(300);
	VerticalLayoutContainer vcon = new VerticalLayoutContainer();
    vcon.setBorders(true);
    vcon.add(btImpSvn, new VerticalLayoutData(1, 1));
    vcon.add(taSvnLoaded, new VerticalLayoutData(1, -1, new Margins(5)));
    panSvn.setWidget(vcon);
//=======================================================================	
	contTools.add( panPar, new HtmlData(".pars"));
	contTools.add( panDd, new HtmlData(".dd"));
	contTools.add( panRep, new HtmlData(".role"));
	contTools.add( panSvn, new HtmlData(".svn"));
	
	setWidget(contTools);
    }
//rowspan=2 
    private native String getImportMarkup() /*-{
    return [ '<table cellpadding=0 cellspacing=4 cols="2">',
        '<tr><td class=pars valign="top"></td><td class=role valign="top"></td></tr>',
        '<tr><td class=dd valign="top"></td><td class=svn valign="top"></td></tr>',
        '</table>'
    ].join("");
  }-*/;

}
