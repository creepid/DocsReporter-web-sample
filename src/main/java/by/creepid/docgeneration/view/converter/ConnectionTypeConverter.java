/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docgeneration.view.converter;

import by.creepid.docgeneration.model.ConnectionType;
import javax.faces.convert.EnumConverter;

/**
 *
 * @author rusakovich
 */
public class ConnectionTypeConverter extends EnumConverter {

    public ConnectionTypeConverter() {
        super(ConnectionType.class);
    }
}
