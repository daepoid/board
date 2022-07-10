package study.board.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthSuccessHandler authSuccessHandler;
    private final AuthFailureHandler authFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                    .and()
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .authorizeRequests()
                    .antMatchers("/", "/css/**", "/js/**", "/images/**", "/img/**", "/*.ico", "/error/**", "/static/**").permitAll()
                    .antMatchers(
                            "/login",
                            "/articles", "/articles/newly", "/articles/best",
                            "/articles/monthly", "/articles/weekly").permitAll()
                    .antMatchers("/members/**").permitAll() // 관리자만 접근 가능하도록 수정해야한다.
                    .antMatchers("/api/**").permitAll() // API 관리를 따로 해주어야 한다.
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .failureHandler(authFailureHandler).permitAll()
                    .defaultSuccessUrl("/")
                    .usernameParameter("loginId")
                    .passwordParameter("password")
                    .successHandler(authSuccessHandler).permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me").permitAll()
        ;

        return http.build();
    }
}
