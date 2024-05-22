package org.zerock.mallapi.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zerock.mallapi.security.filter.JWTCheckFilter;
import org.zerock.mallapi.security.handler.APILoginFailHandler;
import org.zerock.mallapi.security.handler.APILoginSuccessHandler;
import org.zerock.mallapi.security.handler.CustomAccessDeniedHandler;

import java.util.Arrays;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity // 메서드별 시큐리티 적용 p335추가 (메서드에 @PreAuthorize 활용) 컨트롤러에 적용
public class CustomSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        log.info("------------------- 시큐리티 환경설정 filterChain() 메서드 실행------------------");

        // p303 추가 (CSRF 설정은 GET방식을 제외한 모든 요청에 CSRF 토큰이라는 것을 사용하는 방식임

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });
        // API 서버는 무상태(Stateless)를 기본으로 사용하기 때문에 서버 내부에서 세션을 생성하지 않도록 추가
        http.sessionManagement(sessionConfig ->  sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 하지만 API 서버를 작성할 때는 사용하지 않는 것이 일반적이므로 설정을 추가함
        http.csrf(config -> config.disable());

        http.formLogin(config -> {
            config.loginPage(("/api/member/login"));
            config.successHandler(new APILoginSuccessHandler()); // p316 추가( 로그인 후 처리 json)
            config.failureHandler(new APILoginFailHandler()); // p320  추가 ( 로그인 실패 처리 json)
        }); // p312 추가 (postman에서 로그인 기능 테스트)

        // p329 추가
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class); //JWT체크

        // p340 추가
        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new CustomAccessDeniedHandler());
        }); // 예외발생시 메시지 전달

        return http.build();
    }  // 스프링 시큐리티 동작용 메서드 (CustomServletConfig.java에 있는 설정을 이곳에 이동, CORS 관련설정 추가 필수)

    @Bean // Ajax을 이용해서 호출시 교차 출처 리소스 공유(CORS) 제한이 됨 -> 리엑트에서 스프링 부트로 동작하는 서버를 호출해야됨 (시큐리티 컨피그로 이동)
    public CorsConfigurationSource corsConfigurationSource() {

            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOriginPatterns(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            return source;

        }
        
    @Bean // 패스워드 암호화용 코드 객체로 생성
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }    


}
