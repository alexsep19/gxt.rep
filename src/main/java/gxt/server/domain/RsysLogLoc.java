package gxt.server.domain;

import jpa.other.RsysLog;

import com.google.web.bindery.requestfactory.shared.Locator;

public class RsysLogLoc extends Locator<RsysLog, Long>{
    @Override
    public RsysLog create(Class<? extends RsysLog> clazz) {
	return new RsysLog(); 
    }

    @Override
    public RsysLog find(Class<? extends RsysLog> clazz, Long id) {
	return Dao.findRsysLog(id);
    }

    @Override
    public Class<RsysLog> getDomainType() {
	return RsysLog.class;
    }

    @Override
    public Long getId(RsysLog domainObject) {
	return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
	return Long.class;
    }

    @Override
    public Object getVersion(RsysLog domainObject) {
	return domainObject.getVersion();
    }
}
