package org.zerock.mallapi.dto;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO extends User{
    // 시큐리티에 내장된 User를 상속 받아 사용 (extends User)

    private String email;

    private String pw;

    private String nickname;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(String email, String pw, String nickname, boolean social, List<String> roleNames) {
        super(email, pw, roleNames.stream()
                .map(str -> new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList()));
        // 상속하는 부모 클래스의 생성자 함수 때문에 생성자 방식을 사용
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;
        this.social = social;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims() {
        // 현재 사용자의 정보를 Map 타입으로 변환하도록 구성
        // 나중에 JWT 문자열 생성시 사용함.
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("email", email);
        dataMap.put("pw",pw);
        dataMap.put("nickname", nickname);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;
    }
}
