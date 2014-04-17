package by.creepid.docgeneration.view.utils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.Locale;

public final class WebUtils {

	private WebUtils() {
	}

	public static Locale getCurrentLocale() {
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	public static String getMessage(String key) {
		return getMessage(key, FacesContext.getCurrentInstance());
	}

	public static String getMessage(String key, FacesContext facesContext) {
		return (String) resolveExpression("#{msg['" + key + "']}", facesContext);
	}

	public static Object resolveExpression(String expression,
			FacesContext facesContext) {
		Application app = facesContext.getApplication();

		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();

		ValueExpression valueExp = elFactory.createValueExpression(elContext,
				expression, Object.class);

		return valueExp.getValue(elContext);
	}

	public static void addFacesMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(WebUtils.getMessage(msg)));
	}

	public static void addFacesMessage(FacesMessage.Severity severity,
			String summary) {
		addFacesMessageAsIsWithContext(null, severity,
				WebUtils.getMessage(summary), FacesContext.getCurrentInstance());
	}

	public static void addFacesMessageAsIs(String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(msg));
	}

	public static void addFacesMessageAsIs(FacesMessage.Severity severity,
			String message) {
		final FacesContext currentInstance = FacesContext.getCurrentInstance();
		addFacesMessageAsIsWithContext(null, severity, message, currentInstance);
	}

	public static void addFacesMessageAsIsWithClientId(String clientId,
			FacesMessage.Severity severity, String message) {
		final FacesContext currentInstance = FacesContext.getCurrentInstance();
		addFacesMessageAsIsWithContext(clientId, severity, message,
				currentInstance);
	}

	public static void addFacesMessageAsIsWithContext(
			FacesMessage.Severity severity, String message, FacesContext ctx) {
		addFacesMessageAsIsWithContext(null, severity, message, ctx);
	}

	public static void addFacesMessageAsIsWithContext(String clientId,
			FacesMessage.Severity severity, String message, FacesContext ctx) {
		ctx.addMessage(clientId, new FacesMessage(severity, message, ""));
	}

	@SuppressWarnings("unchecked")
	public static <T> T findBean(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return (T) context.getApplication().evaluateExpressionGet(context,
				"#{" + beanName + "}", Object.class);
	}

	public static <T> T findBean(String beanName, FacesContext context) {
		return (T) context.getApplication().evaluateExpressionGet(context,
				"#{" + beanName + "}", Object.class);
	}

	public static int numberOfRows(String value) {
		if (value == null || value.length() == 0) {
			return 0;
		}

		int c = 0;
		for (char ch : value.toCharArray()) {
			if (ch == '\n') {
				c++;
			}
		}

		return c;
	}

	public static void refreshCurrentView() {
		FacesContext context = FacesContext.getCurrentInstance();

		String viewId = context.getViewRoot().getViewId();

		ViewHandler handler = context.getApplication().getViewHandler();
		UIViewRoot root = handler.createView(context, viewId);

		root.setViewId(viewId);
		context.setViewRoot(root);
	}

	public static void clearFacesMessages() {
		clearFacesMessages(FacesContext.getCurrentInstance());
	}

	public static void clearFacesMessages(FacesContext fc) {
		Iterator<FacesMessage> msgIterator = fc.getMessages();
		
		while (msgIterator.hasNext()) {
			msgIterator.next();
			msgIterator.remove();
		}

	}

	public static String getClientId(String id) {
		try {
			return findComponent(id).getClientId();
		} catch (NullPointerException e) {
			return "?";
		}
	}

	public static UIComponent findComponent(String id) {
		return findComponent(FacesContext.getCurrentInstance().getViewRoot(),
				id);
	}

	public static UIComponent findComponent(UIComponent parent, String id) {
		if (id.equals(parent.getId())) {
			return parent;
		}
		
		Iterator<UIComponent> kids = parent.getFacetsAndChildren();
		while (kids.hasNext()) {
			
			UIComponent kid = kids.next();
			UIComponent found = findComponent(kid, id);
			
			if (found != null) {
				return found;
			}
			
		}
		return null;
	}

	public static String getRequestParameter(String name) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(name);
	}
	
}
