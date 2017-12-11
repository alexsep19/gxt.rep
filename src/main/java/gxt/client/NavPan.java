package gxt.client;

import gxt.client.sms.PanSms;
import gxt.client.tools.PanTools;
import gxt.client.domain.Factory;
import gxt.client.files.PanFiles;
import gxt.client.log.PanLog;
import gxt.client.reps.PanReps;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.RootPanel;

public class NavPan extends ContentPanel{
    FlowLayoutContainer playCont = null; 
    VBoxLayoutContainer bc;
    Factory fct;
    String login = "";
    TextButton bReps, bFile, bTools, bLog, bSms;
    PanReps panReps = null;
    PanFiles panFiles = null;
    PanTools panTools = null;
    PanLog panLog = null;
    PanSms panSms = null;
    Viewport viewport = null;
    
    public void setActive(String Login, String Role){
	    login = Login;
        bReps.setEnabled(true);
        if (Role.indexOf("A") >= 0) {
          bTools.setEnabled(true);
          bLog.setEnabled(true);
          bSms.setEnabled(true);
        }
        if (panReps == null) panReps = new PanReps(fct, login){
        	@Override
        	public void delLoading(){
        		Element e = Document.get().getElementById("loading");
              if (e != null) {
               e.removeFromParent();
//               viewport.show();
               RootPanel.get().add(viewport);
//               viewport.setVisible(true);
//               setVisible(true);
//               viewport.forceLayout();
              }}};
	    ShowPan(panReps);
    }
    
	public NavPan(FlowLayoutContainer cont, final Factory Fct, Viewport v){
	playCont = cont;
	fct = Fct;
	viewport = v;
	
	setHeadingText("Навигация");
	getHeader().addStyleName("txt_center");
        bReps = new TextButton("Отчеты");
        bReps.setEnabled(false);
	    bReps.addSelectHandler( new SelectHandler(){
	         @Override
 	         public void onSelect(SelectEvent event) {
	             if (panReps == null) panReps = new PanReps(fct, login);
		         ShowPan(panReps);  }});
        
	    bFile = new TextButton("Файлы", new SelectHandler(){
		    @Override
		    public void onSelect(SelectEvent event) {
			if (panFiles == null) panFiles = new PanFiles(fct);
			ShowPan(panFiles);
		    }}); 
        
        bTools = new TextButton("Настройки");
        bTools.setEnabled(false);
        bTools.addSelectHandler(new SelectHandler(){
  	      @Override
	     public void onSelect(SelectEvent event) {
	          if (panTools == null) panTools = new PanTools(fct);
	       ShowPan(panTools);
	      }}
        );
        bLog = new TextButton("Журнал");
        bLog.setEnabled(false);
        bLog.addSelectHandler(new SelectHandler(){
  	      @Override
	     public void onSelect(SelectEvent event) {
	          if (panLog == null) panLog = new PanLog(fct);
	       ShowPan(panLog);
	      }}
        );
        bSms = new TextButton("Смс");
        bSms.setEnabled(false);
        bSms.addSelectHandler(new SelectHandler(){
  	      @Override
	     public void onSelect(SelectEvent event) {
	          if (panSms == null) panSms = new PanSms(fct);
	       ShowPan(panSms);
	      }}
        );


        bc = new VBoxLayoutContainer();
        bc.setPadding(new Padding(1));
        bc.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        bc.add(bReps, new BoxLayoutData(new Margins(0, 0, 1, 0)));
        bc.add( bFile, new BoxLayoutData(new Margins(0, 0, 1, 0)));
        bc.add( bLog, new BoxLayoutData(new Margins(0, 0, 1, 0)));
        bc.add( bSms, new BoxLayoutData(new Margins(0, 0, 1, 0)));
        bc.add( bTools, new BoxLayoutData(new Margins(0)));
        
        add(bc);
    }
    
    private void ShowPan(ContentPanel pan){
	  playCont.clear();
	  playCont.add(pan);
    }
}
