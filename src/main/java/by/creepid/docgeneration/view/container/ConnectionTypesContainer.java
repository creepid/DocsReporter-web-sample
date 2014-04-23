/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docgeneration.view.container;

import by.creepid.docgeneration.model.ConnectionType;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author rusakovich
 */
@ManagedBean
@ApplicationScoped
public class ConnectionTypesContainer {

    public ConnectionType[] getValues() {
        return ConnectionType.values();
    }
}
