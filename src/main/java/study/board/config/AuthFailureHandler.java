package study.board.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String message = "Invalid UserId or Password";
        if(exception instanceof DisabledException) {
            message = "DisabledException";
        } else if(exception instanceof CredentialsExpiredException) {
            message = "CredentialsExpiredException";
        } else if(exception instanceof BadCredentialsException) {
            message = "BadCredentialsException";
        }

        setDefaultFailureUrl("/login?error=true&exception=" + message);
        super.onAuthenticationFailure(request, response, exception);
    }
}
