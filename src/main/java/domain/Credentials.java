package domain;

import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = "credentials.findByToken", query = "SELECT c FROM Credentials c WHERE c.token = :token"),
        @NamedQuery(name = "credentials.findByApplicationName", query = "SELECT c FROM Credentials c WHERE c.applicationName = :applicationName")
})
public class Credentials implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String applicationName;

    private Date expirationDate;

    private String token;

    public Credentials() {
        this.expirationDate = DateUtils.addMonths(new Date(), 1);
    }

    public Credentials(String applicationName) {
        this();
        this.applicationName = applicationName;
    }

    public Long getId() {
        return id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
