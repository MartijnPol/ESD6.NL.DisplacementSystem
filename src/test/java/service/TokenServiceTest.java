package service;

import io.jsonwebtoken.MalformedJwtException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenServiceTest {

	TokenService tokenService;

	@Before
	public void Setup(){
		tokenService = new TokenService();
	}

	@Test
	public void GetUsernameFromToken_ValidToken_ReturnsUsername(){
		String token = tokenService.EncodeToken("test");

		String extractedUsername = tokenService.getAppFromToken(token);

		Assert.assertEquals("test",extractedUsername);
	}

	@Test(expected = MalformedJwtException.class)
	public void GetUsernameFromToken_InvalidToken_ReturnsNull(){
		tokenService.getAppFromToken("this.is.fake");
	}

	@Test
	public void CheckTrustedToken_ValidToken_ReturnsTrue(){
		String token = tokenService.EncodeToken("test");

		Assert.assertTrue(tokenService.CheckIfTokenIsTrusted(token));
	}

	@Test
	public void CheckTrustedToken_InvalidToken_ReturnsFalse(){
		String token = "lol";

		Assert.assertFalse(tokenService.CheckIfTokenIsTrusted(token));
	}

	@Test
	public void CheckTrustedToken_FakeToken_ReturnsFalse(){
		String token = "this.is.fake";

		Assert.assertFalse(tokenService.CheckIfTokenIsTrusted(token));
	}
}
