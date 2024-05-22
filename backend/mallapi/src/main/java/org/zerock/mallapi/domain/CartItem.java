package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(exclude="cart")
@Table(name = "tbl_cart_item", indexes = {
        @Index(columnList = "cart_cno", name = "idx_cartitem_cart"),
        @Index(columnList = "product_pno, cart_cno", name="idx_cartitem_pno_cart")
})
public class CartItem {

    @Id  // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동번호 생성
    private Long cino; //번호

    @ManyToOne
    @JoinColumn(name = "product_pno")
    private Product product;  //상품명

    @ManyToOne
    @JoinColumn(name = "cart_cno")
    private Cart cart;  // 장바구니

    private int qty; // 수량

    public void changeQty(int qty){
        this.qty = qty;
    }  // 수량 변경설정

    //create table tbl_cart_item (cino bigint not null auto_increment, qty integer not null, cart_cno bigint, product_pno bigint, primary key (cino)) engine=InnoDB
    //create index idx_cart_email on tbl_cart (member_owner)
    //alter table if exists tbl_cart drop index if exists UK_4x59g5i516ao54rb4tnf1x85d
    //alter table if exists tbl_cart add constraint UK_4x59g5i516ao54rb4tnf1x85d unique (member_owner)
    //create index idx_cartitem_cart on tbl_cart_item (cart_cno)
    //create index idx_cartitem_pno_cart on tbl_cart_item (product_pno, cart_cno)
    //alter table if exists tbl_cart add constraint FK4pvmyvyqisuytcxntao9kimj7 foreign key (member_owner) references member (email)
    //alter table if exists tbl_cart_item add constraint FKem31i0jytusbded0v2wvnntyw foreign key (cart_cno) references tbl_cart (cno)
    //alter table if exists tbl_cart_item add constraint FKs7vg62w3nq7igdxgssq1u0biw foreign key (product_pno) references tbl_product (pno)
}