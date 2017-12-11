package gxt.client.domain;


import gxt.server.domain.Worker;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = Worker.class)
public interface WorkerPrx extends ValueProxy{
    public Integer getVersion();
    public String getFId();
    public String getFFileName();
    public String getFRep();
    public String getFStartTime();

}
