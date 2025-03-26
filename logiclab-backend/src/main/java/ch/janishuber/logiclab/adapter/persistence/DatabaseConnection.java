package ch.janishuber.logiclab.adapter.persistence;

import java.io.IOException;
import java.util.Properties;

public class DatabaseConnection {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnection() throws IOException {
        Properties props = new Properties();
        props.load(DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties"));
        this.url = props.getProperty("db.url");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");
    }

    public String getUrl() {
        return this.url;
    }
    public String getUser() {
        return this.user;
    }
    public String getPassword() {
        return this.password;
    }
}
