/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.client;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * Tunnel client parameters (not specific to command-line).
 *
 * @author Yannick
 */
@Parameters
public class TunnelClientParams {

    @Parameter(names = {"--tunnelServerUrl", "-sUrl"}, description = "tunnel server url")
    private String tunnelServerUrl;
    @Parameter(names = {"--destHost", "-dh"}, description = "destination host")
    private String destHostName;
    @Parameter(names = {"--destPort", "-dp"}, description = "destination port")
    private Integer destPort;
    @Parameter(names = {"--listenPort", "-lp"}, description = "listen port")
    private int listenPort = 1859;
    @Parameter(names = {"--ttl"}, description = "default ttl")
    private long defaultTTL = 1000 * 60 * 60;

    public String getTunnelServerUrl() {
        return tunnelServerUrl;
    }

    public void setTunnelServerUrl(String tunnelServerUrl) {
        this.tunnelServerUrl = tunnelServerUrl;
    }

    public String getDestHostName() {
        return destHostName;
    }

    public void setDestHostName(String destHostName) {
        this.destHostName = destHostName;
    }

    public Integer getDestPort() {
        return destPort;
    }

    public void setDestPort(Integer destPort) {
        this.destPort = destPort;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public long getDefaultTTL() {
        return defaultTTL;
    }

    public void setDefaultTTL(long defaultTTL) {
        this.defaultTTL = defaultTTL;
    }
}
