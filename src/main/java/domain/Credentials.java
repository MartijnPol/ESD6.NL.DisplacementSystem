package domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(name = "credentials.findByToken", query = "SELECT c FROM Credentials c WHERE c.token = :token"),
        @NamedQuery(name = "credentials.findByUsername", query = "SELECT c FROM Credentials c WHERE c.username = :username"),
        @NamedQuery(name = "credentials.findByUsernameAndPassword", query = "SELECT c FROM Credentials c WHERE c.username = :username AND c.password = :password")
})
public class Credentials implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String token;

    public Credentials() {
    }

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
