package org.zerock.mallapi.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.util.JWTUtil;

import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {
    // Access Token은 API 서버의 특정 경로를 접근하기 위해서 사용
    // HTTP 헤더 항목 중에서 Authorization 항목 값으로 전달해서 서버에서 이를 체크한다.
    // Authorization 헤더는 타입,토큰 형식으로 중간에 공백 문자로 구분된 값으로 구성됨 -> JWT는 Bearer라는 타입으로 지정됨
    // 서버에서는 보호하고자 하는 자원에 대해서 Access Token을 체크해서 유효한 경우에 접근을 허용하는 구현이 필요함
    // 필터, 인터셉터, 스프링 스큐리티필터를 활용 함
    // 우리는 필터를 추가해서 /api/todo, /api/products/* 경로에 접근 할 경우 토큰을 확인하게 구현할 것임.
    // extends OncePerRequestFilter는 주로 모든 요청에 대해서 체크하려 할 때 사용함

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // extends OncePerRequestFilter의 상위 클래스에서 정의된 메서드로 필터로 체크하지 않을 경로나 메서드(get/post)등을 지정

        // p330 추가
        // Preflight요청은 체크하지 않음 
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }
        String path = request.getRequestURI();
        log.info("shouldNotFilter()메서드 실행 check uri........." + path);

        // p330 추가 api/member/ 경로의 호출은 체크하지 않음
        if(path.startsWith("/api/member/")) {
            return true;
        }

        // p330 추가 이미지 조회 경로는 체크하지 않는다면
        if(path.startsWith("/api/products/view/")) {
            return true;
        }
      

        
        return false;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // JWTUtil이 가는 validateToken()을 활용해서 예외 발생 여부를 확인
        log.info("-------doFilterInternal() 메서드 실행중----------------------------");

        // p332 추가
        log.info("------------------------JWTCheckFilter.......................");

        String authHeaderStr = request.getHeader("Authorization");

        try {
            //Bearer accestoken...
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);
            //filterChain.doFilter(request, response); // p336 제거 아래 추가

            //jwt 트큰 내에는 인증에 필요한 모든 정보를 가지고 있기 때문에 필요한 객체로 생성함
            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames);

            log.info("-----------------------------------");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        }catch(Exception e){

            log.error("JWT Check Error..............");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();

        } // try문 종료 (컨트롤러까지 호출이 가능, Access Token에 문제가 있으면 json으로 에러 출력
        // http://localhost:8080/api/products/list
        // {
        //    "error": "ERROR_ACCESS_TOKEN"
        //}
        // postman 테스트 로그인 -> 토큰확인 -> list 페이지 header에 값 추가 성공?

    }
}
