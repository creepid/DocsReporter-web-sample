package by.creepid.docgeneration.view.faces.lifecycle;

import org.icefaces.application.SessionExpiredException;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rusakovich
 */
public class LoggingExceptionHandler extends ExceptionHandlerWrapper {

	private final static Logger logger = LoggerFactory
			.getLogger(LoggingExceptionHandler.class);

	private final static String REDIRECT_URL = "/faces/index.jsp";

	private ExceptionHandler wrapped;

	public LoggingExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	private void redirect(ViewExpiredException vee) throws IOException {

		FacesContext fc = FacesContext.getCurrentInstance();
		// Map<String, Object> requestMap =
		// fc.getExternalContext().getRequestMap();

		NavigationHandler nav = fc.getApplication().getNavigationHandler();

		// Push some useful stuff to the request scope for
		// use in the page
		// requestMap.put("currentViewId", vee.getViewId());
		if (fc.getViewRoot() == null) {
			UIViewRoot view = fc.getApplication().getViewHandler()
					.createView(fc, vee.getViewId());
			fc.setViewRoot(view);
		}

		logger.info("View expired, redirecting to home page transparently");

		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(REDIRECT_URL);

		fc.renderResponse();
	}

	@Override
	public void handle() throws FacesException {

		Iterable<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents();

		try {
			for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents()
					.iterator(); i.hasNext();) {

				ExceptionQueuedEvent event = i.next();
				ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event
						.getSource();
				Throwable t = context.getException();

				if (t instanceof ViewExpiredException) {

					ViewExpiredException vee = (ViewExpiredException) t;

					try {
						redirect(vee);

					} catch (IOException e) {
						logger.error("Error while redirecting", e);

					} finally {
						i.remove();
					}

				} else if (t instanceof SessionExpiredException) {
					logger.info("Session has expired");
					i.remove();

				} else {

					logger.error("JSF exception", t);

					getWrapped().handle();
				}
			}

		} catch (ConcurrentModificationException e) {
			// stupid ICEfaces throws it somehow
		}

	}

}
