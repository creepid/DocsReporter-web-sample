/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docgeneration.validation;

import by.creepid.docgeneration.ContextHelper;
import by.creepid.docgeneration.model.FirmReg;
import by.creepid.docgeneration.view.utils.WebUtils;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import org.springframework.context.MessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 *
 * @author rusakovich
 */
@FacesValidator("regValidator")
public class RegValidator implements Validator {

    private org.springframework.validation.Validator firmRegValidator;
    private MessageSource messageSource;

    public RegValidator() {
        firmRegValidator = ContextHelper.getBean("firmRegValidator",
                org.springframework.validation.Validator.class);

        messageSource = ContextHelper.getbean(MessageSource.class);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {

        Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();

        WebUtils.clearFacesMessages(context);
        FirmReg firmReg = (FirmReg)WebUtils.findBean("reg");
        
        System.out.println(firmReg.toString());

        Errors errors = new BeanPropertyBindingResult(firmReg, "reg");
        firmRegValidator.validate(firmReg, errors);

        if (errors != null && errors.hasErrors()) {
            String message = messageSource.getMessage("error.validation", null, locale);

            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, message, null));

            List<FieldError> fieldErrors = errors.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                message = messageSource.getMessage(fieldError, locale);
                System.out.println(fieldError.getField() + "   " + fieldError.getCode() + " " + fieldError.getObjectName());

                context.addMessage(fieldError.getField(), new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, message, message));
            }
        }
    }

}
