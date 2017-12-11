package gxt.client.domain;

import gxt.server.domain.listData;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = listData.class)
public interface listDataPrx extends ValueProxy{
//    public long getId();
    public String getName();
    public String getValue();
    public Integer getVersion();
}
