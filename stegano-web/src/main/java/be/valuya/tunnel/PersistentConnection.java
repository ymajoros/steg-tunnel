/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.tunnel;

import java.net.Socket;
import java.util.Objects;

/**
 *
 * @author Yannick
 */
public class PersistentConnection {

    private String id;
    private Socket socket;
    private long ttl;
    private long maxTimeMillis;

    public PersistentConnection() {
    }

    public PersistentConnection(String id, Socket socket, long ttl, long maxTimeMillis) {
        this.id = id;
        this.socket = socket;
        this.ttl = ttl;
        this.maxTimeMillis = maxTimeMillis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public long getMaxTimeMillis() {
        return maxTimeMillis;
    }

    public void setMaxTimeMillis(long maxTimeMillis) {
        this.maxTimeMillis = maxTimeMillis;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersistentConnection other = (PersistentConnection) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PersistentConnection{" + "id=" + id + ", socket=" + socket + ", ttl=" + ttl + ", maxTimeMillis=" + maxTimeMillis + '}';
    }
}
