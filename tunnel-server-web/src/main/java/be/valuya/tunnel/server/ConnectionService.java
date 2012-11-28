/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 * Persistent connection service. Creates and maintains connections.
 *
 * @author Yannick
 */
@Singleton
public class ConnectionService {

    private static final Logger LOG = Logger.getLogger(ConnectionService.class.getName());
    private Map<String, PersistentConnection> persistentConnectionMap;

    @PostConstruct
    protected void init() {
        persistentConnectionMap = Collections.synchronizedMap(new HashMap<String, PersistentConnection>());
    }

    @Schedule(hour = "*", minute = "*", persistent = false)
    protected void clean() throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        List<PersistentConnection> connectionToRemoveList = new ArrayList<>();
        for (PersistentConnection persistentConnection : persistentConnectionMap.values()) {
            long maxTimeMillis = persistentConnection.getMaxTimeMillis();
            if (maxTimeMillis <= currentTimeMillis) {
                connectionToRemoveList.add(persistentConnection);
            }
        }
        for (PersistentConnection persistentConnection : connectionToRemoveList) {
            disconnect(persistentConnection);
        }
    }

    public PersistentConnection createConnection(String hostname, int port, long ttl) throws UnknownHostException, IOException {
        UUID randomUUID = UUID.randomUUID();
        String id = randomUUID.toString();
        Socket socket = new Socket(hostname, port);
        long maxTimeMillis = calcMaxTimeMillis(ttl);
        PersistentConnection persistentConnection = new PersistentConnection(id, socket, ttl, maxTimeMillis);
        persistentConnectionMap.put(id, persistentConnection);
        return persistentConnection;
    }

    public PersistentConnection getConnection(String id) {
        PersistentConnection persistentConnection = persistentConnectionMap.get(id);
        if (persistentConnection != null) {
            updateMaxTimeMillis(persistentConnection);
        }
        return persistentConnection;
    }

    public void disconnect(PersistentConnection persistentConnection) throws IOException {
        Socket socket = persistentConnection.getSocket();
        socket.close();
        String id = persistentConnection.getId();
        persistentConnectionMap.remove(id);
        int connectionCount = persistentConnectionMap.size();
        String message = MessageFormat.format("Terminated connection {0}, {1,number,0} active connection(s)", persistentConnection, connectionCount);
        LOG.log(Level.INFO, message);
    }

    private long calcMaxTimeMillis(long ttl) {
        long currentTimeMillis = System.currentTimeMillis();
        long maxTimeMillis = currentTimeMillis + ttl;
        return maxTimeMillis;
    }

    private void updateMaxTimeMillis(PersistentConnection persistentConnection) {
        long ttl = persistentConnection.getTtl();
        long maxTimeMillis = calcMaxTimeMillis(ttl);
        persistentConnection.setMaxTimeMillis(maxTimeMillis);
    }
}
