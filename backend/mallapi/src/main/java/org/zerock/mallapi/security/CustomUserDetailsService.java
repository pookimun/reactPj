package org.zerock.mallapi.security;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * CustomUSerDetailsService
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    // implements UserDetailsService 인증 처리를 위한 구현체임 -> 커스터마이징 용

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 시큐리티를 적용하면 CustomUserDetailsServiced의 loadUserByUsername에서 사용자 정보를 조회하고
        // 해당 사용자의 인증과 권한 처리를 함
        // filterChain에 값 추가 필수(url 로그인 테스트용)

        log.info("----------------loadUserByUsername()메서드 실행-----------------------------");

        // 사용자 정보를 조회해서 객체 생성 (p314 추가)
        Member member = memberRepository.getWithRoles(username);
        // Member getWithRoles( @Param("email") String email )

        if(member == null){
            throw new UsernameNotFoundException("Not Found");
        }

        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList()
                        .stream()
                        .map(memberRole -> memberRole.name()).collect(Collectors.toList()));

        log.info(memberDTO);

        //select m1_0.email,m2_0.member_email,m2_0.member_role_list,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 left join member_member_role_list m2_0 on m1_0.email=m2_0.member_email where m1_0.email=?
        //: MemberDTO(email=user9@aaa.com, pw=$2a$10$J0N.haZsmJyAd0eo7tfMye9FE9h4OFHeDvZIUHkh75nZgnxDln7i6, nickname=USER9, social=false, roleNames=[USER, MANAGER, ADMIN])
        return memberDTO;
    }
}
