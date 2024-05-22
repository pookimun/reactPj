package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsertMember(){

        for (int i = 0; i < 10 ; i++) {

            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("USER"+i)
                    .build();

            member.addRole(MemberRole.USER);

            if(i >= 5){
                member.addRole(MemberRole.MANAGER);
            }

            if(i >=8){
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);

//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: select m1_0.email,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//            Hibernate: insert into member (nickname,pw,social,email) values (?,?,?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
//            Hibernate: insert into member_member_role_list (member_email,member_role_list) values (?,?)
        }
    }

    @Test
    public void testRead() {

        String email = "user9@aaa.com";

        Member member = memberRepository.getWithRoles(email);

        log.info("-----------------");
        log.info(member);
        log.info(member.getMemberRoleList());
        // select m1_0.email,m2_0.member_email,m2_0.member_role_list,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 left join member_member_role_list m2_0 on m1_0.email=m2_0.member_email where m1_0.email=?

        // Member(email=user9@aaa.com, pw=$2a$10$J0N.haZsmJyAd0eo7tfMye9FE9h4OFHeDvZIUHkh75nZgnxDln7i6, nickname=USER9, social=false)
        // [USER, MANAGER, ADMIN]
    }
}
