package org.zerock.mallapi.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    // 상품을 조회하는 화면에서 사용자가 자신의 장바구니에 상품을 추가하는 경우
    // 사용자의 이메일, 추가하고 샆은 상품번호, 수량
    // 장바구니의 아이템 목록에서 상품 수량을 조절하는 경우(아이템번호, 수량)
    private String email;  // pk

    private Long pno; // 상품번호

    private int qty; // 수량

    private Long cino; // 카트 아이템 번호

}
