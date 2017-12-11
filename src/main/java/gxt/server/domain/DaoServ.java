package gxt.server.domain;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class DaoServ implements ServiceLocator{
    private static Dao serviceInstance;
  
    @Override
    public Object getInstance(Class<?> clazz) {
	if (serviceInstance == null) serviceInstance = new Dao();
	return serviceInstance;
    }
}
