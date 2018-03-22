package dao.jpa;

import dao.AuthenticationDao;
import dao.JPADisplacementSystem;
import domain.Credentials;

import javax.ejb.Stateless;

@Stateless
@JPADisplacementSystem
public class AuthenticationDaoJPAImpl extends GenericDaoJPAImpl<Credentials> implements AuthenticationDao {

    @Override
    public Credentials findByToken(String token) {
        return getEntityManager().createNamedQuery("credentials.findByToken", Credentials.class)
                .setParameter("token", token)
                .getSingleResult();
    }

    @Override
    public Credentials findByApplicationName(String applicationName) {
        return getEntityManager().createNamedQuery("credentials.findByApplicationName", Credentials.class)
                .setParameter("applicationName", applicationName)
                .getSingleResult();
    }
}
