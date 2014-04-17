package by.creepid.docgeneration.view.domain;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.icefaces.application.PushRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import by.creepid.docgeneration.view.security.AuthenticationService;
import by.creepid.docgeneration.view.utils.WebUtils;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final static Logger log = LoggerFactory.getLogger(LoginBean.class);

    private String username;
    private String password;

    @ManagedProperty(value = "#{authenticationService}")
    private transient AuthenticationService authenticationService;

    public String login() {
    	
    	//The PushRenderer allows an application to initiate rendering 
    	//asynchronously and independently of user interaction for a 
    	//group of sessions or views. When a session is rendered, 
    	//all windows or views that a particular user has open in 
    	//their session will be updated via Ajax Push with the current 
    	//application state
        PushRenderer.addCurrentSession("all");

        boolean success = authenticationService.login(username, password);

        if (success) {
            log.info("User " + this.username + " has logged in");
            return "success";
        } else {
            WebUtils.addFacesMessage(FacesMessage.SEVERITY_ERROR, "authentication.failed");
        }
        
        return "failure";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

}

