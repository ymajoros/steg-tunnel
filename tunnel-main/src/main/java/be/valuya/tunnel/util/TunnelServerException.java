/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.util;

import be.valuya.tunnel.util.TunnelError;

/**
 * Tunnel server exception;
 *
 * @author Yannick
 */
public class TunnelServerException extends RuntimeException {

    private TunnelError tunnelError;

    public TunnelServerException(TunnelError tunnelError) {
        this.tunnelError = tunnelError;
    }

    public TunnelServerException(TunnelError tunnelError, String message) {
        super(message);
        this.tunnelError = tunnelError;
    }

    public TunnelServerException(TunnelError tunnelError, String message, Throwable cause) {
        super(message, cause);
        this.tunnelError = tunnelError;
    }

    public TunnelServerException(TunnelError tunnelError, Throwable cause) {
        super(cause);
        this.tunnelError = tunnelError;
    }

    public TunnelServerException(TunnelError tunnelError, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.tunnelError = tunnelError;
    }

    public TunnelError getTunnelError() {
        return tunnelError;
    }

    public void setTunnelError(TunnelError tunnelError) {
        this.tunnelError = tunnelError;
    }

    @Override
    public String toString() {
        return "TunnelServerException{" + "tunnelError=" + tunnelError + ", " + getMessage() + "}";
    }
}
