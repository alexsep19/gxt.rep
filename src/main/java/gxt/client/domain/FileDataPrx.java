package gxt.client.domain;

import gxt.server.domain.FileData;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = FileData.class)
public interface FileDataPrx extends ValueProxy{

    public String getCreated();
    public String getName();
    public String getSize();

    public Integer getVersion();
}
