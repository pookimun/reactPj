package org.zerock.mallapi.repository;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.mallapi.domain.*;
import org.zerock.mallapi.dto.CartItemListDTO;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    public void testInsertByProduct() {

        log.info("test1-----------------------");

        //사용자가 전송하는 정보
        String email = "user9@aaa.com";
        Long pno = 18L;
        int qty = 4;  // 수량 변경 테스트

        //만일 기존에 사용자의 장바구니 아이템이 있었다면

        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if(cartItem != null) {
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);

            return;
        }

        //장바구니 아이템이 없었다면 장바구니부터 확인 필요

        //사용자가 장바구니를 만든적이 있는지 확인
        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;

        //사용자의 장바구니가 존재하지 않으면 장바구니 생성
        if(result.isEmpty()) {

            log.info("MemberCart is not exist!!");

            Member member = Member.builder().email(email).build();

            Cart tempCart = Cart.builder().owner(member).build();

            cart = cartRepository.save(tempCart);

        }else {

            cart = result.get();
        }

        log.info(cart);

        //-------------------------------------------------------------

        if(cartItem == null){
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();

        }
        //상품 아이템 저장
        cartItemRepository.save(cartItem);

//        select c1_0.cino,c1_0.cart_cno,c1_0.product_pno,c1_0.qty from tbl_cart_item c1_0 join tbl_cart c2_0 on c1_0.cart_cno=c2_0.cno where c2_0.member_owner=? and c1_0.product_pno=?
//        select c1_0.cno,c1_0.member_owner from tbl_cart c1_0 where c1_0.member_owner=?
//        select null,m1_0.nickname,m1_0.pw,m1_0.social from member m1_0 where m1_0.email=?
//        insert into tbl_cart (member_owner) values (?)
//        insert into tbl_cart_item (cart_cno,product_pno,qty) values (?,?,?)

        // 수량 변경 후 테스트
//        select c1_0.cino,c1_0.cart_cno,c1_0.product_pno,c1_0.qty from tbl_cart_item c1_0 join tbl_cart c2_0 on c1_0.cart_cno=c2_0.cno where c2_0.member_owner=? and c1_0.product_pno=?
//        select c1_0.cno,o1_0.email,o1_0.nickname,o1_0.pw,o1_0.social from tbl_cart c1_0 left join member o1_0 on o1_0.email=c1_0.member_owner where c1_0.cno=?
//        select p1_0.pno,p1_0.del_flag,p1_0.pdesc,p1_0.pname,p1_0.price from tbl_product p1_0 where p1_0.pno=?
//        update tbl_cart_item set cart_cno=?,product_pno=?,qty=? where cino=?
    }

    @Test
    @Commit  // 장바구니 아이템 수정 테스트 (아이템 수량을 조절하는 경우)
    public void tesstUpdateByCino() {

        Long cino = 1L;  // 카트아이템 번호

        int qty = 4; // 수량 변경

        Optional<CartItem> result = cartItemRepository.findById(cino);

        CartItem cartItem = result.orElseThrow();

        cartItem.changeQty(qty);

        cartItemRepository.save(cartItem);

        //select c1_0.cino,c2_0.cno,o1_0.email,o1_0.nickname,o1_0.pw,o1_0.social,p1_0.pno,p1_0.del_flag,p1_0.pdesc,p1_0.pname,p1_0.price,c1_0.qty from tbl_cart_item c1_0 left join tbl_cart c2_0 on c2_0.cno=c1_0.cart_cno left join member o1_0 on o1_0.email=c2_0.member_owner left join tbl_product p1_0 on p1_0.pno=c1_0.product_pno where c1_0.cino=?
        //select c1_0.cino,c2_0.cno,o1_0.email,o1_0.nickname,o1_0.pw,o1_0.social,p1_0.pno,p1_0.del_flag,p1_0.pdesc,p1_0.pname,p1_0.price,c1_0.qty from tbl_cart_item c1_0 left join tbl_cart c2_0 on c2_0.cno=c1_0.cart_cno left join member o1_0 on o1_0.email=c2_0.member_owner left join tbl_product p1_0 on p1_0.pno=c1_0.product_pno where c1_0.cino=?
        //update tbl_cart_item set cart_cno=?,product_pno=?,qty=? where cino=?

    }

    @Test  // 카트 아이템 리스트
    public void testListOfMember() {

        String email = "user1@aaa.com";

        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByEmail(email);

        for (CartItemListDTO dto : cartItemList) {
            log.info(dto);
        }
        // select c1_0.cino,c1_0.qty,p1_0.pno,p1_0.pname,p1_0.price,i1_0.file_name
        // from tbl_cart_item c1_0
        //      join tbl_cart c2_0 on c1_0.cart_cno=c2_0.cno
        //      left join tbl_product p1_0 on c1_0.product_pno=p1_0.pno
        //      left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno
        // where c2_0.member_owner=? and i1_0.ord=0 order by c1_0.cino desc
        // : CartItemListDTO(cino=1, qty=4, pno=5, pname=상품4, price=400, imageFile=IMAGE1.jpg)

    }


    @Test  // 장바구니 아이템 삭제 및 목록 조회
    public void testDeleteThenList() {
        // 아이템이 삭제 된 후에 다시 해당 아이템이 있었던 장바구니의 모든 아이템 목록을 반환 해야 한다.
        // 때문에 해당 장바구니 아이템을 삭제하기 전에 해당 장바구니의 번호를 구해 두고 삭제후 이를 이용해서 장바구니 아이템 목록을 구함.
        Long cino = 1L;

        //장바구니 번호
        Long cno = cartItemRepository.getCartFromItem(cino);

        //삭제
        cartItemRepository.deleteById(cino);

        //목록
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByCart(cno);

        for (CartItemListDTO dto : cartItemList) {
            log.info(dto);
        }
        // 삭제 전 테스트
        // select c1_0.cno from tbl_cart c1_0 join tbl_cart_item c2_0 on c2_0.cart_cno=c1_0.cno where c2_0.cino=?
        // select c1_0.cino,c1_0.qty,p1_0.pno,p1_0.pname,p1_0.price,i1_0.file_name
        //   from tbl_cart_item c1_0 join tbl_cart c2_0 on c1_0.cart_cno=c2_0.cno
        //     left join tbl_product p1_0 on c1_0.product_pno=p1_0.pno
        //       left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno
        //  where c2_0.cno=? and i1_0.ord=0 order by c1_0.cino desc
        // dto : CartItemListDTO(cino=1, qty=4, pno=5, pname=상품4, price=400, imageFile=IMAGE1.jpg)
        
        // 삭제 후 테스트
        //select c1_0.cno from tbl_cart c1_0 join tbl_cart_item c2_0 on c2_0.cart_cno=c1_0.cno where c2_0.cino=?
        //select c1_0.cino,c2_0.cno,o1_0.email,o1_0.nickname,o1_0.pw,o1_0.social,p1_0.pno,p1_0.del_flag,p1_0.pdesc,p1_0.pname,p1_0.price,c1_0.qty from tbl_cart_item c1_0 left join tbl_cart c2_0 on c2_0.cno=c1_0.cart_cno left join member o1_0 on o1_0.email=c2_0.member_owner left join tbl_product p1_0 on p1_0.pno=c1_0.product_pno where c1_0.cino=?
        //delete from tbl_cart_item where cino=?
        //select c1_0.cino,c1_0.qty,p1_0.pno,p1_0.pname,p1_0.price,i1_0.file_name from tbl_cart_item c1_0 join tbl_cart c2_0 on c1_0.cart_cno=c2_0.cno left join tbl_product p1_0 on c1_0.product_pno=p1_0.pno left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno where c2_0.cno=? and i1_0.ord=0 order by c1_0.cino desc
        
        
    }


}