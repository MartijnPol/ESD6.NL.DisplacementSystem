package dao.jpa;

import dao.AuthenticationDao;
import dao.JPA;
import domain.Credentials;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@JPA
public class AuthenticationDaoJPAImpl extends GenericDaoJPAImpl<Credentials> implements AuthenticationDao {

    @Override
    public Credentials findByToken(String token) {
        TypedQuery<Credentials> query = getEntityManager().createNamedQuery("credentials.findByToken", Credentials.class)
                .setParameter("token", token);

        return oneResult(query);
    }

    @Override
    public Credentials findByApplicationName(String applicationName) {
        TypedQuery<Credentials> query = getEntityManager().createNamedQuery("credentials.findByApplicationName", Credentials.class)
                .setParameter("applicationName", applicationName);

        return oneResult(query);
    }
}
