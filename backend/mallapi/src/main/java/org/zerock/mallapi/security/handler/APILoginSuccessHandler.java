package org.zerock.mallapi.security.handler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.mallapi.dto.MemberDTO;
//import org.zerock.mallapi.util.JWTUtil;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.zerock.mallapi.util.JWTUtil;

@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler{
    // 로그인 성공 후 APILoginSuccessHandler에서 json으로 후 처리 진행

    @Override // p316 추가
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("-------------로그인 성공 후 처리 기능 ------------------------");
        log.info(authentication);
        log.info("-------------------------------------");

        //-------------로그인 성공 후 처리 기능 ------------------------
        // UsernamePasswordAuthenticationToken [Principal=MemberDTO(email=user9@aaa.com, pw=$2a$10$J0N.haZsmJyAd0eo7tfMye9FE9h4OFHeDvZIUHkh75nZgnxDln7i6, nickname=USER9, social=false, roleNames=[USER, MANAGER, ADMIN]), Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_ADMIN, ROLE_MANAGER, ROLE_USER]]
        // -------------------------------------

        MemberDTO memberDTO = (MemberDTO)authentication.getPrincipal(); //p317 추가~ 로그인 성공 후 json 출력

        Map<String, Object> claims = memberDTO.getClaims(); //claims 청구, 주장, 요구하다.

        // JWT 인증서 유효기간 설정
        String accessToken = JWTUtil.generateToken(claims, 1); // 1분간 유효
        String refreshToken = JWTUtil.generateToken(claims,10); // 10분 유효

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();

        //{
        //    "social": false,
        //    "pw": "$2a$10$J0N.haZsmJyAd0eo7tfMye9FE9h4OFHeDvZIUHkh75nZgnxDln7i6",
        //    "nickname": "USER9",
        //    "accessToken": "",
        //    "roleNames": [
        //        "USER",
        //        "MANAGER",
        //        "ADMIN"
        //    ],
        //    "email": "user9@aaa.com",
        //    "refreshToken": ""
        //}

        // 토큰 추가 후
        //        {
        //            "social": false,
        //                "pw": "$2a$10$J0N.haZsmJyAd0eo7tfMye9FE9h4OFHeDvZIUHkh75nZgnxDln7i6",
        //                "nickname": "USER9",
        //                "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzb2NpYWwiOmZhbHNlLCJwdyI6IiQyYSQxMCRKME4uaGFac21KeUFkMGVvN3RmTXllOUZFOWg0T0ZIZUR2WklVSGtoNzVuWmdueERsbjdpNiIsIm5pY2tuYW1lIjoiVVNFUjkiLCJyb2xlTmFtZXMiOlsiVVNFUiIsIk1BTkFHRVIiLCJBRE1JTiJdLCJlbWFpbCI6InVzZXI5QGFhYS5jb20iLCJpYXQiOjE3MTE1MDU5MjksImV4cCI6MTcxMTUwNjUyOX0.rcYqdIpWRPP6axLNohL5FAdylZBTuYpLIZzC4NdYpts",
        //                "roleNames": [
        //            "USER",
        //                    "MANAGER",
        //                    "ADMIN"
        //    ],
        //            "email": "user9@aaa.com",
        //                "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzb2NpYWwiOmZhbHNlLCJwdyI6IiQyYSQxMCRKME4uaGFac21KeUFkMGVvN3RmTXllOUZFOWg0T0ZIZUR2WklVSGtoNzVuWmdueERsbjdpNiIsIm5pY2tuYW1lIjoiVVNFUjkiLCJyb2xlTmFtZXMiOlsiVVNFUiIsIk1BTkFHRVIiLCJBRE1JTiJdLCJlbWFpbCI6InVzZXI5QGFhYS5jb20iLCJpYXQiOjE3MTE1MDU5MjksImV4cCI6MTcxMTU5MjMyOX0.GbtrkoEfi0tgN-Iz5R3tmiRVr9RTO9xQQHN3Q9KrrDw"
        //        }

        // 토큰 검증 사이트 jwt.io

    }
}
