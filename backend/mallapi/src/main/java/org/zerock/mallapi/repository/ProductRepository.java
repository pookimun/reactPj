package org.zerock.mallapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.Product;
public interface ProductRepository extends JpaRepository<Product, Long>{

    @EntityGraph(attributePaths = "imageList") // 쿼리 실행 횟수를 줄여주는 코드(해당 속성을 조인 처리함)
    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> selectOne(@Param("pno") Long pno);

    // select p1_0.pno,p1_0.del_flag,i1_0.product_pno,i1_0.file_name,i1_0.ord,p1_0.pdesc,p1_0.pname,p1_0.price
    // from tbl_product p1_0 left join product_image_list i1_0 on p1_0.pno=i1_0.product_pno
    // where p1_0.pno=?

    @Modifying // 상품레코드를 삭제하는 것이 아니라 delFlage 값으로 삭제된 것처럼 처리
    @Query("update Product p set p.delFlag = :flag where p.pno = :pno")
    void updateToDelete(@Param("pno") Long pno , @Param("flag") boolean flag);

    // 상품 이미지의 ord값이 0인 상품의 이미지 대표 처리
    @Query("select p, pi  from Product p left join p.imageList pi  where pi.ord = 0 and p.delFlag = false ")
    Page<Object[]> selectList(Pageable pageable);
}
