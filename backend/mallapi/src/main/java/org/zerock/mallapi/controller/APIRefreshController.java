package org.zerock.mallapi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.mallapi.util.CustomJWTException;
import org.zerock.mallapi.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {
    // Access Token은 일반적으로 짧은 유효시간을 지정해서 탈취를 당해도 위헙을 줄일 수 있도록 구현 해야 한다.
    // Access Token이 만료되면 사용자는 Refresh Token을 활용해서 새로운 Access Token을 발급 받을 수 있는 기능을 같이 사용
    // api/member/refresh라는 경로를 통해 2개의 토큰을 검증하고 Access Token이 만료되고, Refresh Token이 만료가 되지 않으면 새로운 Access Token을 전송해주려고 함



    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader, String refreshToken) {

        // 1. Access Token이 없거나 잘못된 JWT인 경우 예외발생
        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRASH");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        String accessToken = authHeader.substring(7);

        //2. Access 토큰이 만료되지 않았다면 -> 전달된 토큰을 그대로 전송
        if (checkExpiredToken(accessToken) == false) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        //3. Access Token은 만료시 Refresh토큰 검증 -> 새로운 Access Token 생성
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

        log.info("refresh ... claims: " + claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);

        //4. RefreshToken의 요효기간이 얼마 남지 않은 경우 -> 새로운 RefreshToken 생성 (1시간)
        //5. RefreshToken의 유효기간이 충분히 남은 경우 -> 기존 RefreshToken사용
        String newRefreshToken = checkTime((Integer) claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60 * 24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);

    }

    //4.5번 체크타임 : 시간이 1시간 미만으로 남았다면
    private boolean checkTime(Integer exp) {

        //JWT exp를 날짜로 변환
        java.util.Date expDate = new java.util.Date((long) exp * (1000));

        //현재 시간과의 차이 계산 - 밀리세컨즈
        long gap = expDate.getTime() - System.currentTimeMillis();

        //분단위 계산
        long leftMin = gap / (1000 * 60);

        //1시간도 안남았는지..
        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) {

        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if (ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }
}