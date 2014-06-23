/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docgeneration.view.converter;

import by.creepid.docgeneration.model.ConnectionType;
import javax.faces.convert.EnumConverter;

/**
 *
 * @author rusakovich
 */
public class ConnectionTypeEnumConverter extends EnumConverter {

    public ConnectionTypeEnumConverter() {
        super(ConnectionType.class);
    }
}
