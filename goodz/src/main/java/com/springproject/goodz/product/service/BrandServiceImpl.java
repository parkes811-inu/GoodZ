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

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class BrandServiceImpl implements BrandService{

    @Autowired
    private BrandMapper brandMapper;

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

    /**
     * 브랜드 등록 처리
     */
    @Override
    public int insert(Brand brand) throws Exception {

        log.info("브랜드 등록 처리 진행중...");

        int result = 0;


        MultipartFile logoFile = brand.getLogoFile();

        long fileSize = logoFile.getSize();
        byte[] fileData = logoFile.getBytes();

        // 깡통인지 체크
        if (logoFile != null && !logoFile.isEmpty()) {
            log.info("브랜드 이미지 처리 진행중...");
            // - 파일명 중복 방지를 위해 "UID_파일명.확장자" 형식으로 지정
            // - 업로드 파일명 : UID_원본파일명.확장자
            String fileName = UUID.randomUUID().toString() + "_" + logoFile.getOriginalFilename();

            // File 객체 생성 => new File(업로드 경로, 설정할 파일명);
            File uploadFile = new File(uploadPath, fileName);

            // 파일 업로드 (유저가 서버에 요청한 파일을 복사해서 경로에 넣음)
            FileCopyUtils.copy(fileData, uploadFile);

            // filePath = C:/uploade/UID_브랜드명.확장자
            String filePath = uploadPath + "/" + fileName;
            brand.setImageUrl(filePath);


            result = brandMapper.insert(brand);

            if (result > 0) {
                log.info("브랜드 등록 처리 완료");
            }
        }

        return result;
    }


    
}
