package com.springproject.goodz.product.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.mapper.ProductMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;

    @Value("${upload.path}")    // application.properties에 설정한 업로드 경로
    private String uploadPath;

    /**
     * 상품 목록 조회
     */
    @Override
    public List<Product> list() throws Exception {

        List<Product> productList = productMapper.list();

        return productList;
    }

    /**
     * 메인화면에 최근 상품 4개 띄우기
     */
    @Override
    public List<Product> newArrivals() throws Exception {
        
        List<Product> newArrivalsList = productMapper.newArrivals();

        return newArrivalsList;
    }

    /**
     *  상품 등록 처리
     */
    @Override
    public int insert(Product product) throws Exception {
        log.info("상품 등록 처리 진행중...");

        int result = 0;
        List<MultipartFile> productFiles = product.getProductFiles();

        if (productFiles != null && !productFiles.isEmpty()) {
            // 파일 개수 제한
            if (productFiles.size() > 10) {
                throw new Exception("최대 10개의 이미지만 업로드할 수 있습니다.");
            }

            // 파일 경로들을 저장할 StringBuilder 객체를 초기화
            StringBuilder filePaths = new StringBuilder();

            for (MultipartFile productFile : productFiles) {
                if (productFile != null && !productFile.isEmpty()) {
                    log.info("상품 이미지 처리 진행중...");
                    
                    String fileName = UUID.randomUUID().toString() + "_" + productFile.getOriginalFilename();
                    File uploadFile = new File(uploadPath, fileName);
                    FileCopyUtils.copy(productFile.getBytes(), uploadFile);

                    String filePath = uploadPath + "/products/" + fileName;
                    if (filePaths.length() > 0) {
                        // StringBuilder에 파일 경로를 추가하기 전에 구분자(;)를 추가
                        filePaths.append(";");
                    }
                    filePaths.append(filePath);
                }
            }

            // 모든 파일 경로를 ;로 구분된 문자열로 설정하여 Product 객체의 imageUrl 속성에 저장
            product.setImageUrl(filePaths.toString());
        }

        result = productMapper.insert(product);

        if (result > 0) {
            log.info("상품 등록 처리 완료");
        }

        return result;
    }

    /**
     * 상의만 보기
     */
    @Override
    public List<Product> top() throws Exception {

        List<Product> topList = productMapper.top();
        
        return topList;
    }

    /**
     * 하의만 보기
     */
    @Override
    public List<Product> pants() throws Exception {
        
        List<Product> pantsList = productMapper.pants();
        
        return pantsList;
    }

    /**
     * 신발만 보기
     */
    @Override
    public List<Product> shoes() throws Exception {

        List<Product> shoesList = productMapper.shoes();
        
        return shoesList;
    }

    /**
     * 악세사리만 보기
     */
    @Override
    public List<Product> accessory() throws Exception {

        List<Product> accessoryList = productMapper.accessory();
        
        return accessoryList;
    }

    

}
