package cloud.fogbow.shipapp;

import cloud.fogbow.shipapp.utils.HttpClientWrapper;
import cloud.fogbow.shipapp.api.http.ShibResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ShibApplication extends Application {

	private DateUtils dateUtils;

	public ShibApplication() {
		this.dateUtils = new DateUtils();
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/", ShibResource.class);
		return router;
	}

	public DateUtils getDateUtils() {
		return this.dateUtils;
	}
	
	public void setDateUtils(DateUtils dateUtils) {
		this.dateUtils = dateUtils;
	}
	
	public class DateUtils {
		
		public long getCurrentTimeMillis() {
			return System.currentTimeMillis();
		}
		
	}	
	
}
