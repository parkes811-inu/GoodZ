package com.springproject.goodz.product.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.goodz.product.dto.Brand;
import com.springproject.goodz.product.mapper.BrandMapper;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BrandServiceImpl implements BrandService{

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private FileService fileService;

    @Value("${upload.path}")    // application.properties에 설정한 업로드 경로
    private String uploadPath;

    /**
     * 브랜드 목록 조회
     */
    @Override
    public List<Brand> list() throws Exception {
        
        List<Brand> brandList = brandMapper.list();

        return brandList; 
    }

    // 마지막 브랜드 번호 가져오기 (첨부파일 등록시 사용됨)
    // @Override
    // public int maxNo() throws Exception {

    //     int maxNo = brandMapper.maxNo();

    //     return maxNo; 
    // }

    /**
     * 브랜드 등록 처리
     */
    @Override
    public int insert(Brand brand) throws Exception {

        log.info("::::::::::brandService::::::::::");

        /* ⬇️ 브랜드 등록 처리 ⬇️ */ 
        int result = brandMapper.insert(brand); // 성공 -> 1

        if (result == 1) {
            log.info("브랜드 등록 성공");
        }

        /* ⬇️ 첨부파일 (로고) 등록처리 ⬇️ */ 
        String dir = "brand";
        int parentNo = brandMapper.maxNo(); // 방금 등록된 브랜드 번호를 가져옴
        log.info("브랜드 번호: " + parentNo + "종속테이블: " + dir);

        // 브랜드 로고 업로드
        MultipartFile requestFile = brand.getLogoFile();
        
        // 깡통인지 체크
        if (requestFile != null && !requestFile.isEmpty()) {
            
            Files logoFile = new Files();
            
            // 필요정보: 부모테이블, 부모번호, 멀티파트 파일, 파일코드 0(일반)
            logoFile.setFile(requestFile);          // 첨부했던 파일을 dto에 담음
            logoFile.setParentNo(parentNo);         // maxNo으로 받아온 결과값
            logoFile.setFileCode(0);       // 일반파일 처리 (fileCode는 상품 대표이미지때 쓰임)
            boolean isUploaded = fileService.upload(logoFile, dir);           // 파일 업로드 요청

            if (isUploaded) {
                log.info("로고파일 등록 완료");
            }
        }

        if (result > 0) {
            log.info("브랜드 등록 처리 완료");
        }




        // int result = 0;


        // MultipartFile logoFile = brand.getLogoFile();

        // long fileSize = logoFile.getSize();
        // byte[] fileData = logoFile.getBytes();

        // // 깡통인지 체크
        // if (logoFile != null && !logoFile.isEmpty()) {
        //     log.info("브랜드 이미지 처리 진행중...");
        //     // - 파일명 중복 방지를 위해 "UID_파일명.확장자" 형식으로 지정
        //     // - 업로드 파일명 : UID_원본파일명.확장자
        //     String fileName = UUID.randomUUID().toString() + "_" + logoFile.getOriginalFilename();

        //     // File 객체 생성 => new File(업로드 경로, 설정할 파일명);
        //     File uploadFile = new File(uploadPath, fileName);

        //     // 파일 업로드 (유저가 서버에 요청한 파일을 복사해서 경로에 넣음)
        //     FileCopyUtils.copy(fileData, uploadFile);

        //     // filePath = C:/uploade/UID_브랜드명.확장자
        //     String filePath = uploadPath + "/" + fileName;
        //     brand.setImageUrl(filePath);


        //     result = brandMapper.insert(brand);

        //     if (result > 0) {
        //         log.info("브랜드 등록 처리 완료");
        //     }
        // }

        return result;
    }


    
}
