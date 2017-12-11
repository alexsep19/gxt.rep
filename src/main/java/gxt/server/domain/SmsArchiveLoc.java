package gxt.server.domain;

import jpa.other.Sms_archive;
import com.google.web.bindery.requestfactory.shared.Locator;

public class SmsArchiveLoc extends Locator<Sms_archive, Long>{

	    @Override
	    public Sms_archive create(Class<? extends Sms_archive> clazz) {
		return new Sms_archive(); 
	    }

	    @Override
	    public Sms_archive find(Class<? extends Sms_archive> clazz, Long id) {
		return Dao.findSmsArchive(id);
	    }

	    @Override
	    public Class<Sms_archive> getDomainType() {
		return Sms_archive.class;
	    }

	    @Override
	    public Long getId(Sms_archive domainObject) {
		return domainObject.getId();
	    }

	    @Override
	    public Class<Long> getIdType() {
		return Long.class;
	    }

	    @Override
	    public Object getVersion(Sms_archive domainObject) {
		return domainObject.getVersion();
	    }

}
