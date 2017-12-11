package gxt.client;

import gxt.client.domain.Factory;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class startPoint implements EntryPoint {
    private Factory fct;
    private static Logger rootLogger = Logger.getLogger("");
    FlowLayoutContainer playCont = new FlowLayoutContainer();
    BorderLayoutData westData;
    String role = "?";
    static String fio ="";
    protected ToolButton tbSes;
    
    @Override
    public void onModuleLoad() {
//	rootLogger.addHandler(new ConsoleLogHandler());
	fct = GWT.create(Factory.class);
	fct.initialize(new SimpleEventBus());
	ToolBar botBar = new ToolBar();
//	botBar.addStyleName(ThemeStyles.getStyle().borderBottom());
	final Status userState = new Status();
//	userState.setText("SEPITY@CORP.Iандр Юрьевич Sepity@mdmbank.com админ");
	userState.setWidth(450);
	
	Viewport viewport = new Viewport();
//	viewport.hide();

	final BorderLayoutContainer blc = new BorderLayoutContainer();
//	blc.hide();
	playCont.getScrollSupport().setScrollMode(ScrollMode.AUTO);
	blc.setCenterWidget(playCont);

	westData = new BorderLayoutData(.20);
	westData.setMargins(new Margins(0, 5, 5, 5));
	westData.setCollapsible(true);
	westData.setSplit(true);
	westData.setCollapseMini(true);
	final NavPan navPanel = new NavPan( playCont, fct, viewport);
//	navPanel.setVisible(false);
	blc.setWestWidget(navPanel, westData);

        fct.creRepRequest().getUserInfo().fire(new Receiver<String>() {
	      public void onSuccess(String data) {
		    userState.setText(data); 
	        fct.creRepRequest().getFIs().fire(new Receiver<String>() {
		      public void onSuccess(String data) {
		    	fio = data;  
			    userState.setText(userState.getText().concat(" ").concat(data));
		        fct.creRepRequest().getRole().fire(new Receiver<String>() {
			      public void onSuccess(String data) {
				  role = data;
				   fct.creRepRequest().getLogin().fire(new Receiver<String>() {
				      public void onSuccess(String data) {
					  navPanel.setActive(data, role);
				      } });
			      }
			      });
		      } });
	      } });

	
	BorderLayoutData southData = new BorderLayoutData(25);
	southData.setMargins(new Margins(0));
	southData.setCollapsible(false);
	southData.setSplit(false);
	southData.setCollapseMini(false);

	tbSes = new ToolButton(ToolButton.EXPAND, new SelectHandler() {
        @Override  
        public void onSelect(SelectEvent event) {   
       	 fct.creRepRequest().getSessionUsers().fire(new Receiver<List<String>>() {
     	       @Override public void onSuccess(List<String> response) {
     	    	 ToolTipConfig sesList = new ToolTipConfig();
     	    	 sesList.setTitleHtml("Юзеры");
     	    	 sesList.setCloseable(true);
     	    	   StringBuilder body = new StringBuilder("<div><ul style=\"list-style: disc; margin: 0px 0px 5px 15px\">");
     	    	   for(String it: response){
     	    		 body.append("<li>").append(it).append("</li>");   
     	    	   }
     	    	   body.append("</ul></div>");
     	    	   sesList.setBodyHtml(body.toString());
     	    	   tbSes.setToolTipConfig(sesList);
     		    }}
           );}
        });
    tbSes.setTitle("");

	botBar.add(tbSes);
	botBar.add(userState);
	blc.setSouthWidget(botBar,southData);
	viewport.setWidget(blc);
//	viewport.setVisible(false);
//	RootPanel.get().add(viewport);
    }

}

