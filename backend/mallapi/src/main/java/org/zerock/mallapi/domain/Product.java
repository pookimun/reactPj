package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity // 테이블 객체임
@Table(name = "tbl_product")  //테이블 정의
@Getter // 게터 설정
@ToString(exclude = "imageList") //이미지 리스트 객체 문자 변환
@Builder // 빌더 패턴 사용
@AllArgsConstructor // 모든 매개값으로 생성자 생성
@NoArgsConstructor  // 매개값이 없는 생성자 생성
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    // 실제 데이터 베이스 내에서 상품의 삭제는 나중에 구매기록과 이어지기 때문에 주의해야 함
    // 특정 상품이 데이터베이스에서 삭제되면 해당 상품 데이터를 사용한 모든 구매나 상품 문의 등의 데이터들이 같이 삭제 됨(fk문제)
    // 대안 : 삭제 대신 특정한 칼럼 값을 기준으로 해당 상품이 삭제 되었는지를 구분하는 필드를 만들어 delete 대신 update 처리함(Soft Delete) 
    private boolean delFlag;  // 0이면 판매중, 1이면 삭제됨


    public void changeDel(boolean delFlag) {
        this.delFlag = delFlag;
    }  // 상품 삭제로 표시


    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();
    // ProductImage 이미지를 추가할 수 있도록 구성

    // changeXXX는 상품 수정을 위한 필드
    public void changePrice(int price) {
        this.price = price;
    }

    public void changeDesc(String desc){
        this.pdesc = desc;
    }

    public void changeName(String name){
        this.pname = name;
    }

    public void addImage(ProductImage image) {

        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    public void addImageString(String fileName){

        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .build();
        addImage(productImage);

    }
    
    // 첨부파일 데이터를 비우고 다시 이미지 추가하는 방식
    public void clearList() {
        this.imageList.clear();
    }
    //Hibernate: create table product_image_list (product_pno bigint not null, file_name varchar(255), ord integer) engine=InnoDB
    //Hibernate: create table tbl_product (pno bigint not null auto_increment, del_flag bit not null, pdesc varchar(255), pname varchar(255), price integer not null, primary key (pno)) engine=InnoDB
    //Hibernate: alter table if exists product_image_list add constraint FKfqvvs4dg13jiki1fur4s3qa43 foreign key (product_pno) references tbl_product (pno)
}