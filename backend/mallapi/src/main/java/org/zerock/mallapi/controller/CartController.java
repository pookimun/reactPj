package org.zerock.mallapi.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.*;

import org.zerock.mallapi.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PreAuthorize("#itemDTO.email == authentication.name")
    // 현재 로그인한 사용자의 이메일과 파라미터로 전달된 이메일 주소가 같아야 호출 가능 (접근 권한이 없다 : Access Denied)
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart( @RequestBody CartItemDTO itemDTO){

        log.info(itemDTO);

        if(itemDTO.getQty() <= 0) {
            return cartService.remove(itemDTO.getCino());
        }  // 만일 수량이 0보다 작은 상태가 되면 실제로 삭제 처리하고 장바구니 아이템 목록을 반환


        return cartService.addOrModify(itemDTO);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER')")  // 시큐리티 인증
    @GetMapping("/items") // 사용자의 장바구니 목록 조회
    public List<CartItemListDTO> getCartItems(Principal principal) {

        String email = principal.getName();
        log.info("--------------------------------------------");
        log.info("email: " + email );

        return cartService.getCartItems(email);

    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cino}")  // 장바구니 아이템 삭제
    public List<CartItemListDTO> removeFromCart( @PathVariable("cino") Long cino){

        log.info("cart item no: " + cino);

        return cartService.remove(cino);
    }


}