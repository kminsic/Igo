package com.wak.igo.configuration;

import com.wak.igo.jwt.AccessDeniedHandlerException;
import com.wak.igo.jwt.AuthenticationEntryPointException;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

    @Value("${jwt.secret}")
    String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointException authenticationEntryPointException;
    private final AccessDeniedHandlerException accessDeniedHandlerException;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer(){
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**");
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource());

        http.headers().frameOptions().sameOrigin();

        http.csrf().disable()


                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException) // 인증되지 않은 사용자가 요청했을 때 동작
                .accessDeniedHandler(accessDeniedHandlerException) // 특정 권한이 있어야 접근 가능한 api에 일반 사용자가 접근했을 때 동작

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/kakao/callback").permitAll()
                .antMatchers("/naver/callback").permitAll()
                .antMatchers("/notification/*").permitAll()
                .antMatchers("/notifications/*").permitAll()
                .antMatchers("/subscribe/*").permitAll()
                .antMatchers("/api/member/subscribe/**").permitAll()
                .antMatchers("/api/member/notifications/**").permitAll()
                .antMatchers("/refresh").permitAll()
                .antMatchers("/api/member/signup").permitAll()
                .antMatchers("/api/member/login").permitAll()
                .antMatchers("/api/posts/**").permitAll()
                .antMatchers("/api/detail/**").permitAll()
                .antMatchers("/api/storys/**").permitAll()
                .antMatchers("/api/comments/**").permitAll()
                .antMatchers("/api/search/**").permitAll()
                .antMatchers("/loading-image").permitAll()
                .antMatchers("/api/sotry/**").permitAll()
                .antMatchers("/api/heart/**").permitAll()
                .antMatchers("/api/report/**").permitAll()
                .anyRequest().authenticated()
//                .anyRequest().permitAll()

                .and()
                .apply(new JwtSecurityConfiguration(SECRET_KEY, tokenProvider, userDetailsService)); // 회원가입 된 사용자임을 확인하고 토큰 부여

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//배포용       configuration.addAllowedOriginPattern("http://eunjiroh.shop.s3-website.ap-northeast-2.amazonaws.com");
//        configuration.addAllowedOriginPattern("http://eunjiroh.shop");
//        configuration.addAllowedOriginPattern("http://3.88.14.18");
        configuration.addAllowedOriginPattern("https://naedonnaeyo.com");
        configuration.addAllowedOriginPattern("https://localhost:8080");
        configuration.addAllowedOriginPattern("/");
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("RefreshToken");
        configuration.addExposedHeader("Content-type");
        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}