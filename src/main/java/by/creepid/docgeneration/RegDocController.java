package by.creepid.docgeneration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import by.creepid.docgeneration.converters.EnumTypeConverter;
import by.creepid.docgeneration.header.DocsHeaderManager;
import by.creepid.docgeneration.model.ConnectionType;
import by.creepid.docgeneration.model.FirmReg;
import by.creepid.docgeneration.model.services.FirmRegService;
import by.creepid.docgeneration.validation.InvalidRequestException;
import by.creepid.docgeneration.view.faces.FacesServletHelper;
import by.creepid.docgeneration.view.utils.WebUtils;
import by.creepid.docsreporter.ReportTemplate;
import by.creepid.docsreporter.context.validation.ReportProcessingException;
import by.creepid.docsreporter.converter.DocFormat;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping(value = "/reg")
public class RegDocController {

    private static final Logger logger = LoggerFactory
            .getLogger(RegDocController.class);

    @Autowired
    @Qualifier("regTemplate")
    private ReportTemplate regTemplate;

    @Autowired
    @Qualifier("regHeaderManager")
    private DocsHeaderManager headerManager;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FirmRegService firmRegService;

    private FirmReg processIgnoreMapping(FirmReg reg) {

        firmRegService.fillChoice(reg);
        firmRegService.fillRegionStr(reg);
        firmRegService.fillStreet_typeStr(reg);

        return reg;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ConnectionType.class,
                "type_connection",
                new EnumTypeConverter(ConnectionType.class));
    }

    @ModelAttribute("connectionType")
    public ConnectionType[] connectionType() {
        return ConnectionType.values();
    }

    private void reportWrite(HttpServletResponse response, String acceptType,
            Locale locale, FirmReg firmReg) {

        DocFormat targetFormat = headerManager.getFormatFromAccept(acceptType);

        ByteArrayOutputStream output;
        try {
            output = (ByteArrayOutputStream) regTemplate.generateReport(
                    targetFormat, processIgnoreMapping(firmReg), null);
        } catch (ReportProcessingException ex) {
            logger.error("Error while report creating", ex);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            String errorMessage = messageSource.getMessage(
                    "error.validation.template", null, locale);
            throw new InvalidRequestException(errorMessage, ex.getErrors());
        }

        response.setContentType(targetFormat.getMimeType());

        Map.Entry<String, String> attachEntry = headerManager
                .getContentEntry(targetFormat);
        response.addHeader(attachEntry.getKey(), attachEntry.getValue());

        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();

            response.setContentLength(output.size());
            stream.write(output.toByteArray());

        } catch (IOException ex) {
            logger.error("Error while report creating", ex);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            String errorMessage = messageSource.getMessage(
                    "error.servlet.output", null, locale);
            throw new InvalidRequestException(errorMessage, null);
        } finally {
            if (stream != null) {
                try {
                    stream.flush();
                    stream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, headers = "content-type=application/x-www-form-urlencoded")
    @ResponseBody
    public void create(@ModelAttribute FirmReg firmReg,
            @RequestHeader("Accept") String acceptType,
            BindingResult results, ModelMap map, Locale locale,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        try {
            FacesContext context = FacesServletHelper.getFacesContext(request, response);
            WebUtils.clearFacesMessages(context);

            HttpServletResponse resp = (HttpServletResponse) context.getExternalContext().getResponse();

            this.reportWrite(resp, acceptType, locale, firmReg);

            context.responseComplete();
        } catch (InvalidRequestException ex) {
            logger.error("Error while report creating", ex);
            FacesContext context = FacesServletHelper.getFacesContext(request, response);
            context.getExternalContext().getFlash().setKeepMessages(true);

            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));

            String message = null;

            Errors errors = ex.getErrors();
            if (errors != null) {
                List<FieldError> fieldErrors = errors.getFieldErrors();

                for (FieldError fieldError : fieldErrors) {
                    message = messageSource.getMessage(fieldError, locale);

                    context.addMessage(fieldError.getField(), new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, message, message));

                }

                WebUtils.redirect(context, "/faces/public/registration.xhtml", true);
            }
        }

    }

    public void setRegTemplate(ReportTemplate regTemplate) {
        this.regTemplate = regTemplate;
    }

    public void setHeaderManager(DocsHeaderManager headerManager) {
        this.headerManager = headerManager;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setFirmRegService(FirmRegService firmRegService) {
        this.firmRegService = firmRegService;
    }
}
