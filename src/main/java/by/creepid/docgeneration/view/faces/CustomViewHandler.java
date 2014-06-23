/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docgeneration.view.faces;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class CustomViewHandler extends ViewHandlerWrapper {
	
	private static final String VIEW_PREFIX = "/";

	private ViewHandler handler;

	public CustomViewHandler(ViewHandler handler) {
		this.handler = handler;
	}

	@Override
	public ViewHandler getWrapped() {
		return handler;
	}

	@Override
	public String getActionURL(FacesContext ctx, String viewId) {
		return viewId.startsWith(VIEW_PREFIX)
			? ((HttpServletRequest)ctx.getExternalContext().getRequest()).getRequestURI()
			: handler.getActionURL(ctx, viewId);
	}

}
