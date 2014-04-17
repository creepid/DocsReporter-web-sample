package by.creepid.docgeneration.view.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(AuthenticationServiceImpl.class);

	@Resource(name = "authenticationManager")
	private AuthenticationManager authenticationManager; // specific for Spring
															// Security

	@Override
	public boolean login(String username, String password) {
		try {
			Authentication authenticate = authenticationManager
					.authenticate(
							new UsernamePasswordAuthenticationToken(username, password));
			
			if (authenticate.isAuthenticated()) {
				
				SecurityContextHolder.getContext().setAuthentication(
						authenticate);
				return true;
			}
			
		} catch (AuthenticationException e) {
			logger.error("Access denied", e);
		}
		
		return false;
	}

	@Override
	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		// currentUser.unauthenticate();
	}

}
