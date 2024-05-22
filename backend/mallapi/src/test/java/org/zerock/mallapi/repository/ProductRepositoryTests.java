package org.zerock.mallapi.repository;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Product;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void testInsert() {

        for (int i = 0; i < 10; i++) {

            Product product = Product.builder()
                    .pname("상품"+i)
                    .price(100*i)
                    .pdesc("상품설명 " + i)
                    .build();

            //2개의 이미지 파일 추가
            product.addImageString("IMAGE1.jpg");
            product.addImageString("IMAGE2.jpg");

            productRepository.save(product);

            log.info("-------------------");
        }
    }

    // 엔티티로 Product라는 하나의 엔티티 객체지만 테이블에서 2개의 테이블로 구성되 있기 때문에
    // jpa에서 이를 처리할 때 한번에 모든 테이블을 같이 로딩해서 처리 할 것인지 (eager loading)
    // 필요한 테이블만 먼저 조회할 것인지(lazy loading)을 결정 해야 한다.
    // @ElementCollection은 기본적으로 지연 로딩으로 동작하기 때문에 tbl_product 테이블만 접근해서 데이터를 처리하고 첨부 파일이 필요한 product_image_list에 접근 한다.
    @Transactional // 위와 같이 2개의 테이블을 두번 접근 해야 함으로 트렌젝션 처리를 함.
    @Test
    public void testRead() { // 트렌젝션 처리용 테스트 코드

        Long pno = 1L;

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info(product); // 1 (상품 테이블만 접근)
        log.info(product.getImageList()); // 2 (이미지 테이블까지 접근)

    }

    @Test
    public void testRead2() {  // 조인 처리용 코드

        Long pno = 1L;

        Optional<Product> result = productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        log.info(product);
        log.info(product.getImageList());
        // select p1_0.pno,p1_0.del_flag,i1_0.product_pno,i1_0.file_name,i1_0.ord,p1_0.pdesc,p1_0.pname,p1_0.price
        // from tbl_product p1_0 left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno
        // where p1_0.pno=?
    }

    @Commit
    @Transactional
    @Test
    public void testDelte() {

        Long pno = 2L; // 2번 상품을 삭제 상태로 변경

        productRepository.updateToDelete(pno, true);
        //Hibernate: update tbl_product set del_flag=? where pno=?
    }

    @Test
    public void testUpdate(){

        Long pno = 10L;

        Product product = productRepository.selectOne(pno).get();

        product.changeName("10번 상품");
        product.changeDesc("10번 상품 설명입니다.");
        product.changePrice(5000);

        //첨부파일 수정
        product.clearList();

        product.addImageString(UUID.randomUUID().toString()+"_"+"NEWIMAGE1.jpg");
        product.addImageString(UUID.randomUUID().toString()+"_"+"NEWIMAGE2.jpg");
        product.addImageString(UUID.randomUUID().toString()+"_"+"NEWIMAGE3.jpg");

        productRepository.save(product);

        //Hibernate: select p1_0.pno,p1_0.del_flag,i1_0.product_pno,i1_0.file_name,i1_0.ord,p1_0.pdesc,p1_0.pname,p1_0.price
        //  from tbl_product p1_0 left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno
        //      where p1_0.pno=?
        //Hibernate: select p1_0.pno,p1_0.del_flag,p1_0.pdesc,p1_0.pname,p1_0.price
        //  from tbl_product p1_0
        //      where p1_0.pno=?
        //Hibernate: select i1_0.product_pno,i1_0.file_name,i1_0.ord
        //  from product_image_list i1_0
        //      where i1_0.product_pno=?
        //Hibernate: update tbl_product set del_flag=?,pdesc=?,pname=?,price=? where pno=?
        //Hibernate: delete from product_image_list where product_pno=?
        //Hibernate: insert into product_image_list (product_pno,file_name,ord) values (?,?,?)
        //Hibernate: insert into product_image_list (product_pno,file_name,ord) values (?,?,?)
        //Hibernate: insert into product_image_list (product_pno,file_name,ord) values (?,?,?)
    }

    @Test  // 0번 이미지 대표이미지로 설정 리스트
    public void testList() {

        //org.springframework.data.domain 패키지
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        //java.util
        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));

        //Hibernate: select p1_0.pno,p1_0.del_flag,p1_0.pdesc,p1_0.pname,p1_0.price,i1_0.file_name,i1_0.ord
        //  from tbl_product p1_0 left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno
        //      where i1_0.ord=0 and p1_0.del_flag=0 order by p1_0.pno desc limit ?,?

        // [Product(pno=10, pname=10번 상품, price=5000, pdesc=10번 상품 설명입니다., delFlag=false), ProductImage(fileName=79a0c32b-2495-40ab-be4d-c1e0d2c58302_NEWIMAGE1.jpg, ord=0)]
        // [Product(pno=9, pname=상품8, price=800, pdesc=상품설명 8, delFlag=false), ProductImage(fileName=IMAGE1.jpg, ord=0)]
        // [Product(pno=8, pname=상품7, price=700, pdesc=상품설명 7, delFlag=false), ProductImage(fileName=IMAGE1.jpg, ord=0)]
        // [Product(pno=7, pname=상품6, price=600, pdesc=상품설명 6, delFlag=false), ProductImage(fileName=IMAGE1.jpg, ord=0)]
        // [Product(pno=6, pname=상품5, price=500, pdesc=상품설명 5, delFlag=false), ProductImage(fileName=IMAGE1.jpg, ord=0)]
        // [Product(pno=5, pname=상품4, price=400, pdesc=상품설명 4, delFlag=false), ProductImage(fileName=IMAGE1.jpg, ord=0)]
        // [Product(pno=4, pname=상품3, price=300, pdesc=상품설명 3, delFlag=false), ProductImage(fileName=IMAGE1.jpg, ord=0)]
        // [Product(pno=3, pname=상품2, price=200, pdesc=상품설명 2, delFlag=false), ProductImage(fileName=IMAGE1.jpg, ord=0)]
        // [Product(pno=1, pname=상품0, price=0, pdesc=상품설명 0, delFlag=false), ProductImage(fileName=IMAGE1.jpg, ord=0)]
    }
}
