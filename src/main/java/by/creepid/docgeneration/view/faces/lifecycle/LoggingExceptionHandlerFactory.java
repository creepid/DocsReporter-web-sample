package by.creepid.docgeneration.view.faces.lifecycle;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class LoggingExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory delegateFactory;

    public LoggingExceptionHandlerFactory (ExceptionHandlerFactory delegateFactory) {
        this.delegateFactory = delegateFactory;
    }

    public ExceptionHandler getExceptionHandler() {
        return new LoggingExceptionHandler(delegateFactory.getExceptionHandler());
    }
}
