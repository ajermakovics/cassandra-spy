package org.andrejs.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.Optional;

public class App {

    private final String host;
    private final int port;
    private Session session;
    private PreparedStatement insertStatement;
    private PreparedStatement selectStatement;

    public App(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        connect();

        session.execute("CREATE KEYSPACE IF NOT EXISTS test " +
                "WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};");
        session.execute("CREATE TABLE IF NOT EXISTS test.users (" +
                "id int PRIMARY KEY, " +
                "name text)");

        insertStatement = session.prepare("INSERT INTO test.users (id, name) VALUES (?, ?)");
        selectStatement = session.prepare("SELECT name FROM test.users WHERE id = ?");
    }

    private void connect() {
        Cluster cluster = Cluster.builder().addContactPoints(host).withPort(port).build();
        session = cluster.newSession();
    }

    public void addUser(int id, String name) {
        session.execute(insertStatement.bind(id, name));
    }

    public Optional<String> getUser(int id) {
        ResultSet result = session.execute(selectStatement.bind(id));

        return Optional.ofNullable(result.one())
                .map(row -> row.getString(0));
    }
}
