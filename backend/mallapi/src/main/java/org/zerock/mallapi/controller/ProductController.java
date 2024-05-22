package org.zerock.mallapi.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.service.ProductService;
import org.zerock.mallapi.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    @PostMapping("/")  // 등록용 컨트롤러 http://localhost:8080/api/products
    public Map<String, String> register(ProductDTO productDTO){

        log.info("rgister: " + productDTO);

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        log.info(uploadFileNames);

        //서비스 호출
        Long pno = productService.register(productDTO);

        return Map.of("result", "SUCCESS");
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        return fileUtil.getFile(fileName);

    }

    // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')") // 사용자와 관리자에게 권한 부여
    @PreAuthorize("hasRole('ROLE_USER')") // 관리자만 가능 -> 일반 사용자로 변경
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("list............." + pageRequestDTO);

        return productService.getList(pageRequestDTO);
    // postman으로 테스트 http://localhost:8080/api/products/list?page=1&size=5
    }

    @GetMapping("/{pno}") // http://localhost:8080/api/products/12
    public ProductDTO read(@PathVariable(name="pno") Long pno) {

        return productService.get(pno);

    }


    // 기존의 상품정보를 얻어오고 상품 이미지 정보들을 미리 파악해 두고 나중에 삭제해야 하는 파일들을 파악할 때 사용
    // ProductDTO의 files는 새롭게 업로드해야 하는 파일들이므로 저장하고 업로드된 파일의 이름들을 파약해 두어야 함
    // ProductDTO의 uploadFileNames의 내용물은 기존에 업로드된 파일들의 이름들이므로 새로 업로드된 파일의 이름들을 추가
    // 서비스 계층에 파일 관련 처리가 완료된 ProductDTO를 전달하고 처리/
    // 기존 파일 중에서 더이상 사용되지 않는 파일들을 찾아서 삭제
    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable(name="pno")Long pno, ProductDTO productDTO) {

        productDTO.setPno(pno);

        ProductDTO oldProductDTO = productService.get(pno);

        //기존의 파일들 (데이터베이스에 존재하는 파일들 - 수정 과정에서 삭제되었을 수 있음)
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();

        //새로 업로드 해야 하는 파일들
        List<MultipartFile> files = productDTO.getFiles();

        //새로 업로드되어서 만들어진 파일 이름들
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        //화면에서 변화 없이 계속 유지된 파일들
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        //유지되는 파일들  + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이 됨
        if(currentUploadFileNames != null && currentUploadFileNames.size() > 0) {

            uploadedFileNames.addAll(currentUploadFileNames);

        }
        //수정 작업
        productService.modify(productDTO);

        if(oldFileNames != null && oldFileNames.size() > 0){

            //지워야 하는 파일 목록 찾기
            //예전 파일들 중에서 지워져야 하는 파일이름들
            List<String> removeFiles =  oldFileNames
                    .stream()
                    .filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

            //실제 파일 삭제
            fileUtil.deleteFiles(removeFiles);
        }
        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {

        //삭제해야할 파일들 알아내기
        List<String> oldFileNames =  productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");

    }

}
