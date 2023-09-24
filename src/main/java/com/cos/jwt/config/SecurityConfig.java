package com.cos.jwt.config;

import com.cos.jwt.auth.jwt.JwtAuthenticationFilter;
import com.cos.jwt.auth.jwt.JwtAuthorizationFilter;
import com.cos.jwt.filter.*;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity // 활성화, 스프링 시큐리티 필터(SecurityConfig)가 스프링 필터체인에 등록됨
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //https://iseunghan.tistory.com/365 Spring Security Filter 구조
        // 시큐리티 필터보다 먼저 실행시키고 싶으면 가장 먼저 있는 SecurityContextPersistenceFilter.class 보다 전에 추가해주면된다.
        //http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        http.csrf().disable(); // http.csrf() -> 정상적인 페이지에는 Csrf Token 값을 알려줘야 하는데 Tymeleaf에서는 페이지를 만들때 자동으로 Csrf Token을 넣어줍니다.
        //따로 추가하지 않았는데 아래와 같은 코드가 form tag안에 자동으로 생성됩니다.
        //대신 굳이 사용자에게 보여줄 필요가 없는 값이기 때문에 hidden으로 처리한다.rest api를 이용한 서버라면, session 기반 인증과는 다르게 stateless하기 때문에 서버에 인증정보를 보관하지 않기때문에 disable시킨다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 만드는 방식을 사용하진않음 설정
        .and()
        .addFilter(corsFilter)
        // addFilter() -> 크로스 오리진 요청이 와도 허용(CORS)하는 설정, @CrossOrigin(인증이 필요 없는 경우 사용), 시큐리티 필터에 등록 (인증이 필요한 경우)
        .formLogin().disable() //폼로그인 사용안함
        .httpBasic().disable()
        // httpBasic().disable() -> headers에 Anthorization:ID,PW 담아서 요청하는 방식이 http Basic 인증 방식.
        // 매번 요청할 때마다 아이디랑 패스워드를 달고 요청. 쿠키에 세션을 만들 필요가 없음.
        // 확장성은 좋으나 아이디와 패스워드가 암호화가 안되기때문에 중간에 노출이 될 수 있다.
        // https를 사용하면 아이디와 패스워드가 암호화된다.
        // headers에 Anthorization:토큰(ID와 PW를 통해 만듬)을 넣는 방식(Bearer 방식)을 사용하기위해 http Basic방식을 disable 시킨다.
        // Bearer 방식 -> 토큰을 들고 가는 방식이어서 노출이 되면 안되지만 노출이 돼도 아이디랑 비밀번호를 노출되는건 아니기때문에 basic 방식보다는 안전하다.
        // 토큰은 로그인 할 때마다 서버쪽에서 다시 만들어주고 유효시간도 있기때문에 한 번 노출된다고 위험하진않다.
        // 토큰은 JWT(JSON WEB TOKEN) 사용, 세션 사용X
        .addFilter(new JwtAuthenticationFilter(authenticationManager())) // 꼭 전달해줘야하는 파라미터 AuthenticationManager. 로그인 컨트롤을 위해 UsernamePasswordAuthenticationFilter를 상속받은 JwtAuthenticationFilter객체를 넣어준다.
        .addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository))
        .authorizeRequests()
        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')")
        .anyRequest().permitAll();
    }
}
