package service;

import dao.AuthenticationDao;
import dao.JPADisplacementSystem;
import domain.Credentials;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

@Stateless
public class AuthenticationService {

    @Inject
    @JPADisplacementSystem
    AuthenticationDao authenticationDao;

    public AuthenticationService() {
    }

    /**
     *
     * @param credentials
     */
    public void createCredentials(Credentials credentials) {
        authenticationDao.create(credentials);
    }

    /**
     * @param username
     * @param password
     * @return
     */
    public boolean authenticate(String username, String password) {
        Credentials foundCredentials = authenticationDao.findByUsernameAndPassword(username, password);

        if (foundCredentials.equals(null)) {
            return false;
        }

        return true;
    }

    /**
     * @param username
     * @return
     */
    public String issueToken(String username) {
        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString();

        Credentials foundCredentials = authenticationDao.findByUsername(username);
        foundCredentials.setToken(token);

        authenticationDao.update(foundCredentials);

        return token;
    }

    public boolean validateToken(String token) {
        Credentials foundCredentials = authenticationDao.findByToken(token);

        if (foundCredentials.equals(null)) {
            return false;
        } else {
            return true;
        }
    }
}