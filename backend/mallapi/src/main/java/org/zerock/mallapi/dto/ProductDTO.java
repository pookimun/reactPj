package org.zerock.mallapi.dto;

import lombok.*;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    private boolean delFlag;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>(); // 새롭게 서버에 보내지는 실제 파일 데이터

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>(); // 업로드가 완료된 파일의 이름만 문자열로 보관
}
