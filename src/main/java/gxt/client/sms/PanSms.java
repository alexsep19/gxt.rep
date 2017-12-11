package gxt.client.sms;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import gx.client.panlist.PanList;
import gxt.client.domain.Arm_rep_paramDdPrx;
import gxt.client.domain.Factory;
import gxt.client.domain.SmsArchivePrx;
import gxt.client.domain.Factory.RepRequest;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.RequestFactoryProxy;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.ParseErrorHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.error.TitleErrorHandler;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.info.Info;

public class PanSms extends ContentPanel{
	private static Logger rootLogger = Logger.getLogger("");
    private static final int PAN_WIDTH = 900;
    private static final int PAN_HEIGHT = 500;
    PanList<SmsArchivePrx> panSel;
    final String maskTel = "^[0-9]{10}$";
    final String titleTel = "10 цифр";
    TextField txTel = new TextField();
    DateField dfFrom = new DateField();
    DateField dfTo = new DateField();
    HtmlLayoutContainer conFields;
    
    ParseErrorHandler dataValid = new ParseErrorHandler() {
	      @Override
	      public void onParseError(ParseErrorEvent event) {
	        Info.display("Parse Error", event.getErrorValue() + " это не дата");
	      } };
	      
	KeyDownHandler enterKeyHandler =  new KeyDownHandler() {
				@Override
				public void onKeyDown(KeyDownEvent event) {
		    	     if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
		    	    	 refreshTab();
		           }
				}};

    interface SelProperties extends PropertyAccess<SmsArchivePrx> {
	    ModelKeyProvider<SmsArchivePrx> id();
//	    ValueProvider<SmsArchivePrx, String> but();
	    ValueProvider<SmsArchivePrx, String> respUid();
	    ValueProvider<SmsArchivePrx, String> respClass();
	    ValueProvider<SmsArchivePrx, String> respText();
	    ValueProvider<SmsArchivePrx, String> normalizednumber();
	    ValueProvider<SmsArchivePrx, String> securMess();
	    ValueProvider<SmsArchivePrx, String> strReceiveTime();
//	    ValueProvider<SmsArchivePrx, Date> receiveTime();
//	    ValueProvider<SmsArchivePrx, Date> deliveryTime();
//	    ValueProvider<SmsArchivePrx, Date> sendTime();
	  }
    private final SelProperties propSel = GWT.create(SelProperties.class);

	public PanSms(final Factory Fct) {
		setCollapsible(false);
		getHeader().addStyleName("txt_center");
		setHeadingText("СМС");
		addStyleName("margin-10");
		setPixelSize(1000, 855);

		panSel =  new PanList<SmsArchivePrx>(PAN_WIDTH, PAN_HEIGHT, ""){
//		    ColumnConfig<SmsArchivePrx, String> ccRespUid;
//		    ColumnConfig<SmsArchivePrx, String> ccRespClass;
		    ColumnConfig<SmsArchivePrx, String> ccRespText;
		    ColumnConfig<SmsArchivePrx, String> ccPhoneNumber;
		    ColumnConfig<SmsArchivePrx, String> ccMsgText;
		    ColumnConfig<SmsArchivePrx, String> ccReceiveTime;
//		    ColumnConfig<SmsArchivePrx, Date> ccDeliveryTime;
//		    ColumnConfig<SmsArchivePrx, Date> ccSendTime;
//		    TextField txRespUid = new TextField();
//		    TextField txRespClass = new TextField();
//		    TextArea txRespText = new TextArea();
//		    TextField txPhoneNumber = new TextField();
//		    TextArea txMsgText = new TextArea();
//		    TextField txReceiveTime = new TextField();
//		    TextField txDeliveryTime = new TextField();
//		    TextField txSendTime = new TextField();
		    
		@Override
		public void fill(){
//		  ccRespUid = new ColumnConfig<SmsArchivePrx, String>( propSel.respUid(), 40, "RESP_UID");
//		  ccRespClass = new ColumnConfig<SmsArchivePrx, String>( propSel.respClass(), 20, "RESP_CLASS");
		  ccRespText = new ColumnConfig<SmsArchivePrx, String>( propSel.respText(), 20, "RESP_TEXT");
		  ccPhoneNumber = new ColumnConfig<SmsArchivePrx, String>( propSel.normalizednumber(), 30, "PHONE_NUMBER");
		  ccMsgText = new ColumnConfig<SmsArchivePrx, String>( propSel.securMess(), 100, "MSG_TEXT");
		  ccReceiveTime = new ColumnConfig<SmsArchivePrx, String>( propSel.strReceiveTime(), 30, "RECEIVE_TIME");
		  
		  IdentityValueProvider<SmsArchivePrx> identity = new IdentityValueProvider<SmsArchivePrx>();
		  RowExpander<SmsArchivePrx> expander = new RowExpander<SmsArchivePrx>(identity, new AbstractCell<SmsArchivePrx>() {
		      @Override
		      public void render(Context context, SmsArchivePrx value, SafeHtmlBuilder sb) {
		        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>RESP_UID:</b> " + value.getRespUid());
		        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>RESP_CLASS:</b> " + value.getRespClass());
		        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>RESP_TEXT:</b> " + value.getRespText());
		        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>PHONE_NUMBER:</b> " + value.getNormalizednumber());
		        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>MSG_TEXT:</b> "  + value.getSecurMess() + "</p>");
		        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>RECEIVE_TIME:</b> " + value.getStrReceiveTime() + "</p>");
		        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>SEND_TIME:</b> " + value.getStrSendTime() + "</p>");
		        sb.appendHtmlConstant("<p style='margin: 5px 5px 2px'><b>DELIVERY_TIME:</b> " + value.getStrDeliveryTime() + "</p>");
		      }
		    });
		  getCcL().add(expander);
		  getCcL().add(ccReceiveTime);
		  getCcL().add(ccMsgText);
//		  getCcL().add(ccRespUid);
//		  getCcL().add(ccRespClass);
		  getCcL().add(ccRespText);
		  getCcL().add(ccPhoneNumber);
		  setStT(new ListStore<SmsArchivePrx>(propSel.id()));
		  setRfpT(new RequestFactoryProxy<ListLoadConfig, ListLoadResult<SmsArchivePrx>>() {
		 		@Override
		 		public void load(ListLoadConfig loadConfig, Receiver<? super ListLoadResult<SmsArchivePrx>> receiver) {
		 		  RepRequest req = Fct.creRepRequest();
		 		  List<SortInfo> sortInfo = createRequestSortInfo(req, loadConfig.getSortInfo());
		 		  enableFields(false);
		 		  req.getListSms(sortInfo, txTel.getValue(), dfFrom.getValue(), dfTo.getValue()).to(receiver).fire(new Receiver<Void>(){
			  	      @Override public void onSuccess(Void v){enableFields(true);}
			  	      @Override public void onFailure(ServerFailure error) {
					      AlertMessageBox d = new AlertMessageBox("Ошибка", error.getMessage() + error.getStackTraceString());
					      d.show();
					      enableFields(true);
					      super.onFailure(error);
			  	      }});
		 	  }});
		       
	       initValues(false, false, false);
	       
	       expander.initPlugin(getG());
	       getG().setAllowTextSelection(true);
		}
		};
     conFields = new HtmlLayoutContainer(getFieldMarkup());
     
     txTel.setEmptyText("№ тел.10 цифр");
     txTel.addValidator(new RegExValidator( maskTel, titleTel));
     txTel.setErrorSupport(new TitleErrorHandler(txTel));
     txTel.addKeyDownHandler(enterKeyHandler);
     txTel.setAllowBlank(false);
     txTel.setWidth(100);
     
     Label lbFrom = new Label("с");
     dfFrom.setPropertyEditor(new DateTimePropertyEditor("dd-MM-yyyy"));
     dfFrom.addParseErrorHandler(dataValid);
     dfFrom.setErrorSupport(new TitleErrorHandler(dfFrom));
     dfFrom.addKeyDownHandler(enterKeyHandler);
     Date d = new Date();
     CalendarUtil.addDaysToDate(d, -1);
     dfFrom.setValue(d);
     dfFrom.setWidth(100);
     dfFrom.setAllowBlank(false);
     
     Label lbTo = new Label("по");
     dfTo.setPropertyEditor(new DateTimePropertyEditor("dd-MM-yyyy"));
     dfTo.addParseErrorHandler(dataValid);
     dfTo.setErrorSupport(new TitleErrorHandler(dfTo));
     dfTo.addKeyDownHandler(enterKeyHandler);
     dfTo.setValue(new Date());
     dfTo.setWidth(100);
     dfTo.setAllowBlank(false);
     
//     dfFrom.setText("01-01-2015");
//     dfTo.setText("03-01-2015");
     TextButton btFind = new TextButton("Найти", new SelectHandler() {
         @Override
         public void onSelect(SelectEvent event) {
//           Stock s = d.flush();
//           if (d.hasErrors()) {
//             new MessageBox("Please correct the errors before saving.").show();
//             return;
//           }
       	  refreshTab();
         }
       });
     conFields.add( txTel, new HtmlData(".fTel"));
     conFields.add( lbFrom, new HtmlData(".lbF"));
     conFields.add( dfFrom, new HtmlData(".fF"));
     conFields.add( lbTo, new HtmlData(".lbT"));
     conFields.add( dfTo, new HtmlData(".fT"));
     conFields.add( btFind, new HtmlData(".btFind"));
     conFields.add( panSel, new HtmlData(".pnTab"));
     panSel.fill();
     VerticalLayoutContainer v = new VerticalLayoutContainer();
     v.add(conFields, new VerticalLayoutData(-1, -1));
     v.add(panSel, new VerticalLayoutData(-1, -1));
	 setWidget(v);
	}
	
	void refreshTab(){
//	  if (!txTel.isValid() || !dfFrom.isValid() || !dfTo.isValid()) Window.alert("Исправь красные поля");
	  if (!isValidFields()) Window.alert("Исправь красные поля");
	  else panSel.getG().getLoader().load();
	}
	boolean isValidFields(){
//		if (txTel.isEditing()) 
			txTel.finishEditing(); 
//		if (dfFrom.isEditing()) 
			dfFrom.finishEditing(); 
//		if (dfTo.isEditing()) 
			dfTo.finishEditing();
		return txTel.isValid() && dfFrom.isValid() && dfTo.isValid();
	}
	void enableFields(boolean isEnable){
		conFields.setEnabled(isEnable);
	}
	
	 private native String getFieldMarkup() /*-{
	    return [ '<table cellpadding=0 cellspacing=5 cols="1">',
	        '<tr><td class=fTel valign="top"/><td class=lbF valign="top"/><td class=fF valign="top"/><td class=lbT valign="top"/><td class=fT valign="top"/><td class=btFind valign="top"/></tr>',
	        '</table>'
	    ].join("");
	  }-*/;
//     '<tr><td class=pnTab colspan="4"></td></tr>',

}
