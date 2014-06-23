package by.creepid.docgeneration.converters;

import java.beans.PropertyEditorSupport;


public class EnumTypeConverter extends PropertyEditorSupport {

    private final Class clazz;

    public EnumTypeConverter(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getAsText() {
        return (getValue() == null ? "" : ((Enum) getValue()).name());
    }

    @Override
    public void setAsText(final String text)
            throws IllegalArgumentException {
        Enum type = Enum.valueOf(clazz, text);
        setValue(type);
    }

}
