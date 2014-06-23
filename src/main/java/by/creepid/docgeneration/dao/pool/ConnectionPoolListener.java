/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docgeneration.dao.pool;

import com.mchange.v2.c3p0.ConnectionCustomizer;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rusakovich
 */
public class ConnectionPoolListener implements ConnectionCustomizer {

    private static final Logger logger = LoggerFactory
            .getLogger(ConnectionPoolListener.class);
    private AtomicInteger activeConnections = new AtomicInteger();
    private AtomicInteger acquiredConnections = new AtomicInteger();

    @Override
    public void onAcquire(Connection c, String pdsIdt) {
        logger.info("onAcquire: Connection acquired from database : " + c
                + " [" + pdsIdt + "]");
        acquiredConnections.incrementAndGet();
        logger.info("onAcquire: Total Open Connections in Pool : "
                + acquiredConnections);
    }

    @Override
    public void onDestroy(Connection c, String pdsIdt) {
        logger.info("onDestroy: Connection closed with database : " + c + " ["
                + pdsIdt + "]");
        acquiredConnections.decrementAndGet();
        logger.info("onDestroy: Total Open Connections in Pool : "
                + acquiredConnections);

    }

    @Override
    public void onCheckOut(Connection c, String pdsIdt) {
        logger.info("onCheckOut: Connection from pool provide to application : "
                + c + " [" + pdsIdt + "]");
        activeConnections.incrementAndGet();
        logger.info("onCheckOut: Total Active Connections in Pool : "
                + activeConnections);
    }

    @Override
    public void onCheckIn(Connection c, String pdsIdt) {
        logger.info("onCheckIn: Connection returned to pool from application : "
                + c + " [" + pdsIdt + "]");
        activeConnections.decrementAndGet();
        logger.info("onCheckIn: Total Active Connections in Pool : "
                + activeConnections);

    }
}
