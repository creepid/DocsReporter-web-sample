package by.creepid.docgeneration.view.faces.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RedirectListener implements PhaseListener {

    private final static Logger log = LoggerFactory.getLogger(RedirectListener.class);

    private static final String POST = "POST";

    public RedirectListener() {
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    @Override
    public void afterPhase(PhaseEvent event) {

    }

    /**
     * ReDirect to the next page only if there are no Error Messages.
     */

    public void beforePhase(PhaseEvent _event) {

        // We do this only for the RENDER_RESPONSE Phase
        if (_event.getPhaseId() != PhaseId.RENDER_RESPONSE) {
            return;
        }

        // Get the Current Request
        FacesContext facesContext = _event.getFacesContext();
        HttpServletRequest servletRequest =
                (HttpServletRequest) facesContext.getExternalContext().getRequest();

        // If POST Method
        if (POST.equals(servletRequest.getMethod())) {

            // Get the next View ID and URL
            String nextViewID = facesContext.getViewRoot().getViewId();
            String nextViewURL =
                    facesContext.getApplication().getViewHandler().getActionURL(facesContext,
                            nextViewID);

            // re-direct to the Next URL if Messages don't Exist
            try {
                // If the Form does not have Messages re-direct
                // Else use Default Navigation as defined in Faces Config
                // We-Default to re-direct so the URL will represent the Page they are on
                // and so the user can properly refresh their page
                // We do this because during a re-direct the Messages are Lost
                // and any user input is refreshed from what is currently in the model
                if (!isMessagesRendered(facesContext)) {
                    log.debug("Redirecting to " + nextViewURL);
                    facesContext.getExternalContext().redirect(nextViewURL);
                } else {
                    log.info("Messages Exist - Staying on the Same Page");
                }
            } catch (IOException ex) {
                log.error("beforePhase() -- Error re-directing to " +
                        nextViewURL + " and Id = " + nextViewID, ex);
            }
        }
    }

    private boolean isMessagesRendered(FacesContext facesContext) {
        return facesContext.getMessages().hasNext();
    }
}
