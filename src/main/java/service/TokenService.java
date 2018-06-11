package service;

import io.jsonwebtoken.*;

public class TokenService {

	static String key = "b858b430e82c39965277796185c4272398ffd1f47cbaa12a398f4d91c9ee90cf";


	public String EncodeToken(String appType) {
		return Jwts.builder().setSubject("appAuthorization").signWith(SignatureAlgorithm.HS512, key).claim("appType", appType).compact();
	}


	public Boolean CheckIfTokenIsTrusted(String jwtToken) {
		try{
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody();
			if (claims.getSubject().equals("appAuthorization")) {
				return true;
			}
			return false;

		}
		catch(SignatureException | MalformedJwtException e){
			return false;
		}
	}

	public String getAppFromToken(String jwtToken){
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody();
		return (String) claims.get("appType");
	}
}
