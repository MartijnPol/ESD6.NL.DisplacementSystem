package service;

import dao.AuthenticationDao;
import dao.JPA;
import domain.Credentials;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Stateless
public class AuthenticationService {

    @Inject
    @JPA
    AuthenticationDao authenticationDao;

    /**
     * Empty constructor
     */
    public AuthenticationService() {
    }

    /**
     * Function to save a given Credentials object in the database.
     *
     * @param credentials The CarTracker object that needs to be saved
     */
    public void createCredentials(Credentials credentials) {
        authenticationDao.create(credentials);
    }

    /**
     * Function that checks if there are credentials that belong to a given application name.
     *
     * @param applicationName Application name that
     * @return True when credentials are found, otherwise false
     */
    public boolean authenticate(String applicationName) {
        return this.findByApplicationName(applicationName) != null;
    }

    /**
     * Issue a random token.
     * Right after the token is generated, the credentials that belong the the provided application name are updated.
     *
     * @param applicationName is the name of the Application that issues a Token
     * @return the token
     */
    public String issueToken(String applicationName) {
        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);

        Credentials foundCredentials = this.findByApplicationName(applicationName);
        foundCredentials.setToken(token);

        this.authenticationDao.update(foundCredentials);

        return token;
    }

    /**
     * Function that checks if the provided token exists in the database.
     * Besides that the expiration date is also looked at.
     * When the date is expired, the token is not valid and the application should issue a new token.
     *
     * @param token Token that should be searched for
     * @return True if token is valid, otherwise false
     */
    public boolean tokenIsValid(String token) {
        Credentials foundCredentials = this.authenticationDao.findByToken(token);

        return foundCredentials != null && !this.isDateExpired(foundCredentials.getExpirationDate());
    }

    /**
     * Function that check if date is expired.
     * The current date is compared to the provided date
     * If the current date is greater than the provided date false is returned, al other cases true.
     *
     * @param date Date that should be checked
     * @return True when provided date is expired, false otherwise
     */
    public boolean isDateExpired(Date date) {
        Date now = new Date();
        return date.compareTo(now) < 0;
    }

    /**
     * Function that searches the database for credentials.
     *
     * @param applicationName Application name that is searched for
     * @return Credentials object that was found
     */
    public Credentials findByApplicationName(String applicationName) {
        return authenticationDao.findByApplicationName(applicationName);
    }

    /**
     * Sets the AuthenticationDao of the AuthenticationService.
     *
     * @param authenticationDao
     */
    public void setAuthenticationDao(AuthenticationDao authenticationDao) {
        this.authenticationDao = authenticationDao;
    }
}