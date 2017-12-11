package gxt.client.reps;

import gxt.client.domain.Arm_rep_paramDdPrx;
import gxt.client.domain.Arm_rep_repParPrx;
import gxt.client.domain.Arm_rep_repPrx;
import gxt.client.domain.Factory;
import gxt.client.domain.WorkerPrx;
import gxt.client.domain.Factory.RepRequest;
import gxt.client.domain.listDataPrx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.ParseErrorHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.info.Info;

public class RepEditor extends FlowLayoutContainer/*Composite*/ implements Editor<Arm_rep_repPrx>{
    private static Logger rootLogger = Logger.getLogger("");
    Factory fct;
    Arm_rep_repPrx rep;
    String dateFormat = "dd-MM-yyyy";
    String monthFormat = "MM-yyyy";
//    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//    int numParams = 0;
//    ArrayList<listData> l = new ArrayList<listData>();
    ArrayList<Widget> w = new ArrayList<Widget>();
    ParseErrorHandler dataValid = new ParseErrorHandler() {
	      @Override
	      public void onParseError(ParseErrorEvent event) {
	        Info.display("Parse Error", event.getErrorValue() + " это не дата");
	      } };
    ListStore<listDataPrx> store = null;      
    ComboBox<listDataPrx> cmb = null;
    final HideHandler hideHandler = new HideHandler() {
	      @Override
	      public void onHide(HideEvent event) {
	        Dialog btn = (Dialog) event.getSource();
//	        String msg = Format.substitute("The '{0}' button was pressed", btn.getHideButton().getText());
	        String msg = "XXX";
	        Info.display("MessageBox", msg);
	      }
	    };

    public RepEditor(Factory repFct){
//	rootLogger.addHandler(new ConsoleLogHandler());
       fct = repFct;
//       numParams = 0;
    }
    
    
    public void edit(Arm_rep_repPrx Rep, final Grid<WorkerPrx> grid) {
       rep = Rep;
       for(Arm_rep_repParPrx it: rep.getArm_rep_repPars() ){
	 String type = it.getArm_rep_param().getPtype();
	 String fieldLabel = it.getArm_rep_param().getPlabel();
	 Arm_rep_paramDdPrx dd = it.getArm_rep_param().getArm_rep_paramDd();

	 if(type.equals("D")){
	    DateField df = new DateField();
	    df.setPropertyEditor(new DateTimePropertyEditor("dd-MM-yyyy"));
	    df.addParseErrorHandler(dataValid);
	    df.setValue(new Date());
	    w.add(df);
            add(new FieldLabel(df, fieldLabel), new VerticalLayoutData(1, -1));
	 }else if(type.equals("B")||type.equals("E")){
	    DateField df = new DateField();
	    df.setPropertyEditor(new DateTimePropertyEditor("MM-yyyy"));
	    df.addParseErrorHandler(dataValid);
	    df.setValue(new Date());
	    w.add(df);
           add(new FieldLabel(df, fieldLabel), new VerticalLayoutData(1, -1));
	 }else if(type.equals("N")){
	    TextField tf = new TextField();
	    w.add(tf);  
	    tf.setAllowBlank(false);
	    add(new FieldLabel(tf, fieldLabel), new VerticalLayoutData(1, -1));
	 }else if(type.equals("L")){
            store = new ListStore<listDataPrx>(new ModelKeyProvider<listDataPrx>() {
				public String getKey(listDataPrx item) {
					return String.valueOf(item.getValue());
				}});
	    cmb = new ComboBox<listDataPrx>(store, new LabelProvider<listDataPrx>() {
					public String getLabel(listDataPrx item) {
						return item.getName();
						}});
//    	    rootLogger.log(Level.INFO,  "store.addAll");
//	    rootLogger.log(Level.INFO,  "cmb.setValue(store.get(0))");
	    cmb.setTriggerAction(TriggerAction.ALL);
	    cmb.setEditable(false);
	    cmb.setForceSelection(true);
	    cmb.setWidth(200);
	    w.add(cmb);
	    add(new FieldLabel(cmb, fieldLabel), new VerticalLayoutData(1, -1));
//	    rootLogger.log(Level.INFO,  "before");
	    final ComboBox<listDataPrx> cmbFin = cmb;
	    fct.creRepRequest().getParamDd(dd).fire(new Receiver<List<listDataPrx>>() {
	           public void onSuccess(List<listDataPrx> data) {
			cmbFin.getStore().addAll((Collection<? extends listDataPrx>) data);
			cmbFin.setValue(cmbFin.getStore().get(0));
 	       }});
	  }else if (type.equals("T")) {
	    TextField tf = new TextField();
//	    textField.addValidator(new MinLengthValidator(50));
//	    textField.addValidator(new MaxLengthValidator(255));
//	    textField.addValidator(new RegExValidator("^[^a-z]+$", "Only uppercase letters allowed"));
	    w.add(tf);  
	    tf.setAllowBlank(false);
	    add(new FieldLabel(tf, fieldLabel), new VerticalLayoutData(1, -1));
	 }else {add(new Label(fieldLabel), new VerticalLayoutData(1, -1));
	 }

     }
       
	TextButton tb = new TextButton("Запустить");
	tb.addSelectHandler(new SelectHandler() {
	      @Override
	      public void onSelect(SelectEvent event) {
//		  RepRequest req = fct.creRepRequest();
		  String ret = "";
		  for(int i = 0; i < rep.getArm_rep_repPars().size(); i++ ){
		    String type = rep.getArm_rep_repPars().get(i).getArm_rep_param().getPtype();
		    if (type.equals("T")){
			ret = ret + rep.getArm_rep_repPars().get(i).getArm_rep_param().getPname() + "=" + ((TextField)w.get(i)).getValue() + ";";
		    }else if(type.equals("D")){
			ret = ret + rep.getArm_rep_repPars().get(i).getArm_rep_param().getPname() + "=" +
				DateTimeFormat.getFormat(dateFormat).format(((DateField)w.get(i)).getValue()) + ";";
//		              String.format("%1$td-%1$tm-%1$tY", ((DateField)w.get(i)).getValue()) + ";";
//                                df.format(((DateField)w.get(i)).getValue()) + ";";
		    }else if(type.equals("B")){
			ret = ret + rep.getArm_rep_repPars().get(i).getArm_rep_param().getPname() + "=01-" +
				DateTimeFormat.getFormat(monthFormat).format(((DateField)w.get(i)).getValue()) + ";";
//			      String.format("%1$td-%1$tm-%1$tY", ((DateField)w.get(i)).getValue()) + ";";
//                                df.format(((DateField)w.get(i)).getValue()) + ";";
		    }else if(type.equals("E")){
			ret = ret + rep.getArm_rep_repPars().get(i).getArm_rep_param().getPname() + "=" +
				DateTimeFormat.getFormat(monthFormat).format(((DateField)w.get(i)).getValue()) + ";";
//			      String.format("%1$td-%1$tm-%1$tY", ((DateField)w.get(i)).getValue()) + ";";
//                                df.format(((DateField)w.get(i)).getValue()) + ";";
		    }else if(type.equals("L")){
			ret = ret + rep.getArm_rep_repPars().get(i).getArm_rep_param().getPname() + "=" +
                               ((listDataPrx)((ComboBox)w.get(i)).getValue()).getValue() + ";";
		    }else{
			ret = ret + rep.getArm_rep_repPars().get(i).getArm_rep_param().getPname() + "=" +
                                ((TextField)w.get(i)).getValue() + ";";

		    }
		  }
		  
		  fct.creRepRequest().addRunReps(rep.getRname(), String.valueOf(rep.getId()), rep.getAlias(), rep.getRformat(), ret).fire(new Receiver<List<WorkerPrx>>() {
		    public void onSuccess(List<WorkerPrx> data) {
			rootLogger.log(Level.INFO,  "data = "+ data.size());
			grid.getStore().clear();
			grid.getStore().addAll(data);
			grid.getView().refresh(false);
		    }
		    public void onFailure(ServerFailure error) {
//		          ShowErrorAddr("Ошибка сервера", true);
		          AlertMessageBox d = new AlertMessageBox("Внимание", error.getMessage());
		          d.addHideHandler(hideHandler);
		          d.show();

		          super.onFailure(error);
		        }
		    });
	      } });
	add(tb);
	
//       driver.edit(rp, fct.creRepRequest());
    }
}
