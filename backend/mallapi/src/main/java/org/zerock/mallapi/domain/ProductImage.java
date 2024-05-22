package org.zerock.mallapi.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable //해당 클래스의 인스턴스가 값 타입 객체임
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String fileName;

    private int ord;
    // 순서라는 속성을 가짐 나중에 이미지마다 번호를 지정하고 상품 목록을 출력할 때 ord 값이 0번이 이미지들만 화면에서 볼 수 있도록 하기 위함.
    // 대표 이미지는 0번임

    public void setOrd(int ord){
        this.ord = ord;
    }

    //Hibernate: create table product_image_list (product_pno bigint not null, file_name varchar(255), ord integer) engine=InnoDB

}