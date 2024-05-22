package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "owner")  // owner 필드을 문자열로 변환 금지
@Table(
        name = "tbl_cart",
        indexes = { @Index(name="idx_cart_email", columnList = "member_owner") }
)  // tbl_cart라는 테이블 명으로 생성하고 사용자의 이메일을 통해서 검색 함으로 index를 생성
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne // 사용자 테이블의 1:1관계 설정
    @JoinColumn(name="member_owner")
    private Member owner;
    // create table tbl_cart (cno bigint not null auto_increment, member_owner varchar(255), primary key (cno)) engine=InnoDB
}