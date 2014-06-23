package by.creepid.docgeneration.view.domain;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author rusakovich
 */
@ManagedBean(name = "helloBean")
@SessionScoped
public class HelloBean {
    
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void hello(ActionEvent actionEvent) {
        setMessage("World?");
    }
}

