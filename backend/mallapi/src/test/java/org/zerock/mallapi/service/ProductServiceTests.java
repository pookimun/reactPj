package org.zerock.mallapi.service;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Test
    public void testList() {

        //1 page, 10 size
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDTO> result = productService.getList(pageRequestDTO);

        result.getDtoList().forEach(dto -> log.info(dto));
        //Hibernate: select p1_0.pno,p1_0.del_flag,p1_0.pdesc,p1_0.pname,p1_0.price,i1_0.file_name,i1_0.ord from tbl_product p1_0 left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno where i1_0.ord=0 and p1_0.del_flag=0 order by p1_0.pno desc limit ?,?
        //Hibernate: select count(p1_0.pno) from tbl_product p1_0 left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno where i1_0.ord=0 and p1_0.del_flag=0

        //  : ProductDTO(pno=10, pname=10번 상품, price=5000, pdesc=10번 상품 설명입니다., delFlag=false, files=[], uploadFileNames=[79a0c32b-2495-40ab-be4d-c1e0d2c58302_NEWIMAGE1.jpg])
        //  : ProductDTO(pno=9, pname=상품8, price=800, pdesc=상품설명 8, delFlag=false, files=[], uploadFileNames=[IMAGE1.jpg])
        //  : ProductDTO(pno=8, pname=상품7, price=700, pdesc=상품설명 7, delFlag=false, files=[], uploadFileNames=[IMAGE1.jpg])
        //  : ProductDTO(pno=7, pname=상품6, price=600, pdesc=상품설명 6, delFlag=false, files=[], uploadFileNames=[IMAGE1.jpg])
        //  : ProductDTO(pno=6, pname=상품5, price=500, pdesc=상품설명 5, delFlag=false, files=[], uploadFileNames=[IMAGE1.jpg])
    }

    @Test  // 서비스 등록 테스트
    public void testRegister() {

        ProductDTO productDTO = ProductDTO.builder()
                .pname("새로운 상품")
                .pdesc("신규 추가 상품입니다.")
                .price(1000)
                .build();

        //uuid가 있어야 함
        productDTO.setUploadFileNames(
                java.util.List.of(
                        UUID.randomUUID()+"_" +"Test1.jpg",
                        UUID.randomUUID()+"_" +"Test2.jpg"));

        productService.register(productDTO);

        //Hibernate: insert into tbl_product (del_flag,pdesc,pname,price) values (?,?,?,?)
        //Hibernate: insert into product_image_list (product_pno,file_name,ord) values (?,?,?)
        //Hibernate: insert into product_image_list (product_pno,file_name,ord) values (?,?,?)
    }

    @Test
    public void testRead() {

        //실제 존재하는 번호로 테스트 
        Long pno = 12L;

        ProductDTO productDTO = productService.get(pno);

        log.info(productDTO);
        log.info(productDTO.getUploadFileNames());

        //Hibernate: select p1_0.pno,p1_0.del_flag,i1_0.product_pno,i1_0.file_name,i1_0.ord,p1_0.pdesc,p1_0.pname,p1_0.price
        //   from tbl_product p1_0 left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno
        //      where p1_0.pno=?
    }


}