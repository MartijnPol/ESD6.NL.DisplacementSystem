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
    public Credentials findByUsername(String username) {
        return getEntityManager().createNamedQuery("credentials.findByUsername", Credentials.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public Credentials findByUsernameAndPassword(String username, String password) {
        return getEntityManager().createNamedQuery("credentials.findByUsernameAndPassword", Credentials.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
    }
}
