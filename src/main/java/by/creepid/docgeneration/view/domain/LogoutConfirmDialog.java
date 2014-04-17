package by.creepid.docgeneration.view.domain;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.creepid.docgeneration.view.security.AuthenticationService;

@ManagedBean(name = "logoutConfirmDialog")
@SessionScoped
public class LogoutConfirmDialog extends ConfirmDialog {
	
    private final static Logger logger = LoggerFactory.getLogger(LoginBean.class);

    @ManagedProperty(value = "#{authenticationService}")
    private AuthenticationService authenticationService;

    @Override
    public String confirm() {
        authenticationService.logout();
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
        
        try {
            httpSession.invalidate();
        } catch (org.icefaces.application.SessionExpiredException e) {
        	logger.error("Session expired", e);
            // we expect SessionExpiredException on invalidate() call here, after all beans were destroyred
        }
        
        setVisible(false);
        
        return "logout";
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}

