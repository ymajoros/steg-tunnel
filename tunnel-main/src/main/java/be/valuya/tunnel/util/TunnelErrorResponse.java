/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.util;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yannick
 */
@XmlRootElement
public class TunnelErrorResponse implements Serializable {

    private String message;
    private String stackTraceText;
    private TunnelError tunnelError;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTraceText() {
        return stackTraceText;
    }

    public void setStackTraceText(String stackTraceText) {
        this.stackTraceText = stackTraceText;
    }

    public TunnelError getTunnelError() {
        return tunnelError;
    }

    public void setTunnelError(TunnelError tunnelError) {
        this.tunnelError = tunnelError;
    }
}
