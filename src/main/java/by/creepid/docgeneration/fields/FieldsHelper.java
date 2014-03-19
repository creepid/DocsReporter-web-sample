package by.creepid.docgeneration.fields;

import java.lang.reflect.Field;

public final class FieldsHelper {
	
	private static final String SEPARATOR = "line.separator";

    private static final String newLine = System.getProperty(SEPARATOR);

    private FieldsHelper() {
    }

    public static String getFieldsDump(Object obj) {
        StringBuilder result = new StringBuilder();

        result.append(obj.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = obj.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            field.setAccessible(true);
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(obj));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

}
