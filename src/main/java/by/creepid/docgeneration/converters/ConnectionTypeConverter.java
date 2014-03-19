package by.creepid.docgeneration.converters;

import java.beans.PropertyEditorSupport;

import by.creepid.docgeneration.model.ConnectionType;

public class ConnectionTypeConverter extends PropertyEditorSupport{
	
	@Override public void setAsText(final String text) 
			throws IllegalArgumentException
    {
        setValue(ConnectionType.getType(text));
    }

}
