package kr.co.cmtinfo.seal.app.web.config;

import kr.co.cmtinfo.seal.core.security.authorization.SimpleLogoutSuccessHandler;
import kr.co.cmtinfo.seal.app.web.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private MyUserDetailsService myUserDetailsService;

    @Autowired
    public void setMyUserDetailsService(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(myUserDetailsService);

//        auth.inMemoryAuthentication()
//            .withUser("user1").password(passwordEncoder().encode("user1Pass")).roles("USER")
//            .and()
//            .withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("USER")
//            .and()
//            .withUser("admin").password(passwordEncoder().encode("adminPass")).roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
//            .anonymous()
//                .disable()
            .authorizeRequests()
//            .antMatchers("/admin/**")
//                .hasRole("ADMIN")
////            .antMatchers("/anonymous*")
////                .anonymous()
//            .antMatchers("/authorization/login*", "/authorization/registration*")
//                .permitAll()
//            .antMatchers("/css/**", "/js/**", "/images/**")
//                .permitAll()
//            .anyRequest()
//                .authenticated()
            .and()
            .formLogin()
                .loginPage("/authorization/login")
//                .loginProcessingUrl("/perform_login")
                .usernameParameter("email")
                .defaultSuccessUrl("/welcome", true)
                .failureUrl("/authorization/login?error=true")
                .failureHandler(authenticationFailureHandler())
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/authorization/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new SimpleLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return null;
    }

}
