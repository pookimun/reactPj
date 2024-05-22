package org.zerock.mallapi.service;
import java.util.LinkedHashMap;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.dto.MemberModifyDTO;  // 435 추가
import org.zerock.mallapi.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{
    // API 서버에서는 Access Token으로 기존 회원정보를 이용하거나 새로운 회원으로 추가할 것
    // accessToken을 파라미터로 받아서 로그인 처리에 사용 dto 반환
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder; // 423추가

    @Override
    public MemberDTO getKakaoMember(String accessToken) {

        String email = getEmailFromKakaoAccessToken(accessToken);

        log.info("email: " + email );

        // 423 추가
        Optional<Member> result = memberRepository.findById(email);

        //기존의 회원 (이메일이 있다면)
        if(result.isPresent()){

            MemberDTO memberDTO = entityToDTO(result.get());

            return memberDTO;

        }

        //회원이 아니었다면
        //닉네임은 '소셜회원'으로
        //패스워드는 임의로 생성
        Member socialMember = makeSocialMember(email);
        memberRepository.save(socialMember);

        MemberDTO memberDTO = entityToDTO(socialMember);

//        return null;
            return memberDTO ; // 424 변경
    }

    @Override  // 436 추가
    public void modifyMember(MemberModifyDTO memberModifyDTO) {
        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());

        Member member = result.orElseThrow();

        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
        member.changeSocial(false);
        member.changeNickname(memberModifyDTO.getNickname());

        memberRepository.save(member);
    }


    private String getEmailFromKakaoAccessToken(String accessToken){

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if(accessToken == null){
            throw new RuntimeException("Access Token is null");
        }
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type","application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(
                        uriBuilder.toString(),
                        HttpMethod.GET,
                        entity,
                        LinkedHashMap.class);

        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("------------------------------------");
        log.info(bodyMap);

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");

        log.info("kakaoAccount: " + kakaoAccount);

        return kakaoAccount.get("email");

    }
    // 423 이메일이 존재하지 않으면 객체 생성진행 용
    private Member makeSocialMember(String email) {

        String tempPassword = makeTempPassword();

        log.info("tempPassword: " + tempPassword);

        String nickname = "소셜회원";

        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;

    }

    // 422 만일 해당 이메일을 가진 회원이 없다면 새로운 회원을 추가할 때 패스워드를 임의로 생성함
    // 자동으로 생성된 패스워드는 알 수 없다. (일반 로그인불가 -> 카카오 로그인 후 패스워드 강제 설정)
    private String makeTempPassword() {

        StringBuffer buffer = new StringBuffer();

        for(int i = 0;  i < 10; i++){
            buffer.append(  (char) ( (int)(Math.random()*55) + 65  ));
        }
        return buffer.toString();
    }
//
//
//    @Override
//    public void modifyMember(MemberModifyDTO memberModifyDTO) {
//
//        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());
//
//        Member member = result.orElseThrow();
//
//        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
//        member.changeSocial(false);
//        member.changeNickname(memberModifyDTO.getNickname());
//
//        memberRepository.save(member);
//
//    }

}
