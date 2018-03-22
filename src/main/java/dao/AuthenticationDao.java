package dao;

import domain.Credentials;

public interface AuthenticationDao extends GenericDao<Credentials> {
    Credentials findByToken(String token);

    Credentials findByUsername(String username);

    Credentials findByUsernameAndPassword(String username, String password);
}
