package dao;

import domain.Credentials;

public interface AuthenticationDao extends GenericDao<Credentials> {
    Credentials findByToken(String token);

    Credentials findByApplicationName(String applicationName);
}
