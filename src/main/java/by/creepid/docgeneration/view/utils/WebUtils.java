package by.creepid.docgeneration.view.utils;

import java.io.IOException;
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
import java.util.List;
import java.util.Locale;
import javax.faces.context.ExternalContext;

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
            msgIterator.remove();
        }

        Iterator<String> itIds = fc.getClientIdsWithMessages();
        while (itIds.hasNext()) {
            List<FacesMessage> messageList = fc.getMessageList(itIds.next());
            messageList.clear();
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

    private static String getMappingForRequest(String servletPath, String pathInfo) {

        if (servletPath == null) {
            return null;
        }

        // If the path returned by HttpServletRequest.getServletPath()
        // returns a zero-length String, then the FacesServlet has
        // been mapped to '/*'.
        if (servletPath.length() == 0) {
            return "/*";
        }

        // presence of path info means we were invoked
        // using a prefix path mapping
        if (pathInfo != null) {
            return servletPath;
        } else if (servletPath.indexOf('.') < 0) {
            // if pathInfo is null and no '.' is present, assume the
            // FacesServlet was invoked using prefix path but without
            // any pathInfo - i.e. GET /contextroot/faces or
            // GET /contextroot/faces/
            return servletPath;
        } else {
            // Servlet invoked using extension mapping
            return servletPath.substring(servletPath.lastIndexOf('.'));
        }
    }

    public static String getFacesMapping(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();

        String servletPath = extContext.getRequestServletPath();
        String pathInfo = extContext.getRequestPathInfo();

        return getMappingForRequest(servletPath, pathInfo);
    }

    public static boolean isPrefixMapped(String mapping) {
        return (mapping.charAt(0) == '/');
    }

    public static void redirect(FacesContext context, String redirectPage, boolean isKeepFlashMessages)
            throws IOException {
        ExternalContext extContext = context.getExternalContext();

        extContext.getFlash().setKeepMessages(isKeepFlashMessages);
        extContext.getFlash().setRedirect(true);

        context.getExternalContext().redirect(redirectPage + "?faces-redirect=true");
    }
}
