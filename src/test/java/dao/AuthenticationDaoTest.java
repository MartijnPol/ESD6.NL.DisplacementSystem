package dao;

import domain.Credentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Thom van de Pas on 7-5-2018
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationDaoTest {

    @Mock
    private AuthenticationDao authenticationDao;

    private Credentials accountAdministrationSystemCredentials;
    private Credentials driverSystemCredentials;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.accountAdministrationSystemCredentials = new Credentials("AccountAdministrationSystem");
        this.accountAdministrationSystemCredentials.setToken("fmrh7fpci1m6t53618b94iqr0d");
        this.driverSystemCredentials = new Credentials("DriverSystem");
        this.driverSystemCredentials.setToken("d0rqi49b81635t6m1icpf7hrmf");
    }

    @Test
    public void findCredentialsByTokenTest() {
        this.authenticationDao.create(this.accountAdministrationSystemCredentials);
        this.authenticationDao.create(this.driverSystemCredentials);
        verify(this.authenticationDao, Mockito.times(1)).create(accountAdministrationSystemCredentials);
        verify(this.authenticationDao, Mockito.times(1)).create(driverSystemCredentials);

        when(this.authenticationDao.findByToken("fmrh7fpci1m6t53618b94iqr0d"))
                .thenReturn(accountAdministrationSystemCredentials);
        Credentials foundCredentials = this.authenticationDao.findByToken("fmrh7fpci1m6t53618b94iqr0d");
        assertSame(accountAdministrationSystemCredentials, foundCredentials);
        assertNotSame(driverSystemCredentials, foundCredentials);

        when(this.authenticationDao.findByToken("d0rqi49b81635t6m1icpf7hrmf"))
                .thenReturn(driverSystemCredentials);
        foundCredentials = this.authenticationDao.findByToken("d0rqi49b81635t6m1icpf7hrmf");
        assertSame(driverSystemCredentials, foundCredentials);
        assertNotSame(accountAdministrationSystemCredentials, foundCredentials);
    }


}
