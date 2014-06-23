/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docgeneration.validation;

import by.creepid.docgeneration.ContextHelper;
import by.creepid.docgeneration.view.utils.WebUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

/**
 *
 * @author rusakovich
 */
public class ValidatorPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        ExternalContext externalContext = facesContext.getExternalContext();

        Map requestMap = externalContext.getRequestMap();
        Map sessionMap = externalContext.getSessionMap();

        processValidators(requestMap, facesContext);
        processValidators(sessionMap, facesContext);
    }

    private void processValidators(Map scope, FacesContext facesContext) {
        Collection collection = scope.values();

        for (Object object : collection) {
            if (object instanceof Validatable) {
                validateValidatable((Validatable) object, facesContext);
            }
        }
    }

    private void validateValidatable(Validatable validatable, FacesContext facesContext) {
        Validator validator = (Validator) ContextHelper.getBean("firmRegValidator", Validator.class);

        WebUtils.clearFacesMessages(facesContext);

        Errors errors = new BindException(validatable, validatable.getClass().getSimpleName());
        validator.validate(validatable, errors);
        for (Object error : errors.getAllErrors()) {
            FieldError fieldError = (FieldError) error;

            String smessage = fieldError.getDefaultMessage();

            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary(smessage);
            message.setDetail(smessage);

            facesContext.addMessage(fieldError.getField(), message);
        }

        Iterator messages = facesContext.getMessages();
        while (messages.hasNext()) {
            FacesMessage message = (FacesMessage) messages.next();
            System.out.printf(message.getSummary() + "#" + message.getDetail());
        }

        if (errors.hasErrors()) {
            facesContext.renderResponse();
        }
    }

    @Override
    public void beforePhase(PhaseEvent arg0) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.UPDATE_MODEL_VALUES;
    }

}
