/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.client;

import be.valuya.tunnel.util.TunnelServerException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yannick
 */
public class TunnelThreadGroup extends ThreadGroup {

    private static final Logger LOG = Logger.getLogger(TunnelThreadGroup.class.getName());

    public TunnelThreadGroup() {
        super("tunnel-client");
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable instanceof TunnelServerException) {
            TunnelServerException tunnelServerException = (TunnelServerException) throwable;
            LOG.log(Level.WARNING, "Uncaught tunnel server exception: ", tunnelServerException);
        } else {
            String message = throwable.getMessage();
            LOG.log(Level.WARNING, message, throwable);
        }
    }
}
