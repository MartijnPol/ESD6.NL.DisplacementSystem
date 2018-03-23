package service;

import dao.AuthenticationDao;
import domain.Credentials;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private Credentials credentials;
    private Credentials credentialsSecond;
    private Date dateFuture;
    private Date dateToday;
    private Date datePast;

    @Mock
    private AuthenticationDao authenticationDao;

    @Mock
    private AuthenticationService authenticationServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authenticationService = new AuthenticationService();
        authenticationService.setAuthenticationDao(authenticationDao);

        credentials = new Credentials("AccountAdministrationSystem");
        credentials.setToken("fmrh7fpci1m6t53618b94iqr0d");
        credentials = new Credentials("PoliceAdministrationSystem");
        credentials.setToken("d0rqi49b81635t6m1icpf7hrmf");
        dateFuture = DateUtils.addMonths(new Date(), 1);
        datePast = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
        dateToday = new Date();
    }

    @Test
    public void create_NewCredentials_CredentialsCreated() {
        authenticationService.createCredentials(credentials);
        verify(authenticationDao, Mockito.times(1)).create(credentials);
    }

    @Test
    public void tokenValid_Token_TokenIsValid() {
        when(authenticationServiceMock.tokenIsValid("fmrh7fpci1m6t53618b94iqr0d")).thenReturn(true);
        when(authenticationDao.findByToken("fmrh7fpci1m6t53618b94iqr0d")).thenReturn(credentials);
        boolean isTokenValid = authenticationService.tokenIsValid("fmrh7fpci1m6t53618b94iqr0d");
        assertThat(isTokenValid, equalTo(true));
    }

    @Test
    public void tokenValid_Token_TokenIsNotValid() {
        when(authenticationServiceMock.tokenIsValid("d0rqi49b81635t6m1icpf7hrmf")).thenReturn(true);
        when(authenticationDao.findByToken("d0rqi49b81635t6m1icpf7hrmf")).thenReturn(credentialsSecond);
        boolean isTokenValid = authenticationService.tokenIsValid("fmrh7fpci1m6t53618b94iqr0d");
        assertThat(isTokenValid, equalTo(false));
    }

    @Test
    public void dateExpired_DateFuture_DateIsNotExpired() {
        when(authenticationServiceMock.isDateExpired(dateFuture)).thenReturn(false);
        boolean isDateExpired = authenticationService.isDateExpired(DateUtils.addMonths(new Date(), 1));
        assertThat(isDateExpired, equalTo(false));
    }

    @Test
    public void dateExpired_DatePast_DateIsExpired() {
        when(authenticationServiceMock.isDateExpired(datePast)).thenReturn(true);
        boolean isDateExpired = authenticationService.isDateExpired(new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime());
        assertThat(isDateExpired, equalTo(true));
    }

    @Test
    public void findByName_ApplicationName_CredentialsFound() {
        when(authenticationService.findByApplicationName("AccountAdministrationSystem")).thenReturn(credentials);
        Credentials foundCredentials = authenticationDao.findByApplicationName("AccountAdministrationSystem");
        assertThat(foundCredentials, is(credentials));
    }
}
