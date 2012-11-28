/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;
import java.util.List;

/**
 * Command-line tunnel client.
 *
 * @author Yannick
 */
public class TunnelCLI {

    private static void usage(JCommander jCommander) {
        List<ParameterDescription> parameterDescriptions = jCommander.getParameters();
        for (ParameterDescription parameterDescription : parameterDescriptions) {
            String names = parameterDescription.getNames();
            String description = parameterDescription.getDescription();
            System.out.println(names + ": " + description);
        }
    }

    private static boolean isParamComplete(TunnelClientParams tunnelClientParams) {
        String destHostName = tunnelClientParams.getDestHostName();
        Integer destPort = tunnelClientParams.getDestPort();
        String tunnelServerUrl = tunnelClientParams.getTunnelServerUrl();
        return destHostName != null && destPort != null && tunnelServerUrl != null;
    }

    public static void main(String... args) {
        TunnelClientParams tunnelClientParams = new TunnelClientParams();
        JCommander jCommander = new JCommander(tunnelClientParams, args);
        if (!isParamComplete(tunnelClientParams)) {
            usage(jCommander);
            System.exit(-1);
        }
        TunnelPortForwarder tunnelPortForwarder = new TunnelPortForwarder(tunnelClientParams);
        tunnelPortForwarder.startServer();
    }
}
