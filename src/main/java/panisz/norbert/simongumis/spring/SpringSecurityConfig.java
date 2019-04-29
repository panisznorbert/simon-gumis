package panisz.norbert.simongumis.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] MEGENGEDETT_URLEK = {
            "/VAADIN/**",
            "/HEARTBEAT/**",
            "/UIDL/**",
            "/frontend/**",
            "/resources/**"
    };
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(MEGENGEDETT_URLEK).permitAll()
                .antMatchers("/gumikkezelese", "/rendelesek", "/lefoglalt_idopontok").fullyAuthenticated();
    }
}
