package com.springproject.goodz.product.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.goodz.product.dto.Page;
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.mapper.PriceHistoryMapper;
import com.springproject.goodz.product.mapper.ProductMapper;
import com.springproject.goodz.product.mapper.ProductOptionMapper;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductOptionMapper productOptionMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private PriceHistoryMapper priceHistoryMapper;

    @Override
    public List<Product> list() throws Exception {
        return productMapper.list();
    }

    @Override
    public List<Product> productList(Page page, String keyword) throws Exception {
        return productMapper.productList(page, keyword);
    }

    @Override
    public int getTotalCount(String keyword) throws Exception {
        return productMapper.getTotalCount(keyword);
    }

    @Override
    public List<Product> newArrivals() throws Exception {
        return productMapper.newArrivals();
    }

    @Override
    public int insert(Product product, int mainImgIndex) throws Exception {
        log.info("상품 등록 처리 진행중...");

        int result = productMapper.insert(product);

        int pNo = product.getPNo();
        if (result > 0 && pNo > 0) {
            log.info("상품 등록 처리 완료, pNo: " + pNo);

            List<ProductOption> options = product.getOptions();
            if (options != null && !options.isEmpty()) {
                for (ProductOption option : options) {
                    option.setPNo(pNo);
                    productOptionMapper.insertProductOption(option);
                }
            }

            List<MultipartFile> requestFiles = product.getProductFiles();
            if (requestFiles != null && !requestFiles.isEmpty()) {
                for (int i = 0; i < requestFiles.size(); i++) {
                    MultipartFile file = requestFiles.get(i);
                    if (file != null && !file.isEmpty()) {
                        Files fileInfo = new Files();
                        fileInfo.setParentTable(product.getCategory());
                        fileInfo.setParentNo(pNo);
                        fileInfo.setFile(file); // 파일 설정 추가

                        // 파일의 번호가 mainImgIndex와 같으면 file_code를 1로 설정
                        if (i == mainImgIndex) {
                            fileInfo.setFileCode(1);
                        } else {
                            fileInfo.setFileCode(0);
                        }

                        // 파일이 한 개이고, 대표 이미지로 설정이 안되어서 넘어오면 그 사진이 대표이미지
                        if (requestFiles.size() == 1 && mainImgIndex == -1) {
                            fileInfo.setFileCode(1);
                        }

                        try {
                            fileService.upload(fileInfo, product.getCategory());
                            log.info("상품 파일 처리 완료: " + file.getOriginalFilename());
                        } catch (Exception e) {
                            log.error("파일 업로드에 실패하였습니다.", e);
                            throw new Exception("파일 업로드에 실패하였습니다.", e);
                        }
                    } else {
                        log.warn("파일이 비어있거나 null입니다.");
                    }
                }
            } else {
                log.warn("파일 리스트가 비어있거나 null입니다.");
            }
        } else {
            throw new Exception("상품 등록에 실패하였습니다.");
        }

        return result;
    }

    

    @Override
    public void makeHistory(int pNo, String size, int initialPrice) throws Exception {
        log.info("가격 히스토리 등록 진행중...");
        priceHistoryMapper.makeHistory(pNo, size, initialPrice);
        log.info("가격 히스토리 등록 완료");
    }

    @Override
    public List<Product> top() throws Exception {
        return productMapper.top();
    }

    @Override
    public List<Product> pants() throws Exception {
        return productMapper.pants();
    }

    @Override
    public List<Product> shoes() throws Exception {
        return productMapper.shoes();
    }

    @Override
    public List<Product> accessory() throws Exception {
        return productMapper.accessory();
    }

    @Override
    public Product getProductBypNo(int pNo) throws Exception {
        return productMapper.getProductBypNo(pNo);
    }

    @Override
    public List<ProductOption> getProductOptionsByProductId(int pNo) throws Exception {
        return productOptionMapper.getProductOptionsByProductId(pNo);
    }

    @Override
    public int insertProductOption(ProductOption productOption) throws Exception {
        return productOptionMapper.insertProductOption(productOption);
    }

    // 제품과 최신 가격 변동 정보 조회
    @Override
    public List<Product> UsedInPay(int pNo) throws Exception {
        return productMapper.UsedInPay(pNo);
    }

    // 상세 페이지 내에서 같은 브랜드 상품 조회
    @Override
    public List<Product> findSameBrandProducts(@Param("brand") String brand, 
                                               @Param("category") String category, 
                                               @Param("pNo") int pNo,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit) throws Exception {

        return productMapper.findSameBrandProducts(brand, category, pNo, offset, limit);
    }

    @Override
    public void updateProduct(Product product) {
        try {
            // 상품 정보 업데이트
            productMapper.updateProduct(product);

            // 옵션 정보 업데이트
            List<ProductOption> options = product.getOptions();
            if (options != null) {
                for (ProductOption option : options) {
                    option.setStockQuantity(option.getAddedStockQuantity());
                    productMapper.updateOptionsByProductId(option);
                }
            }
        } catch (Exception e) {
            // 예외를 로그로 기록하고 재발생
            e.printStackTrace();
            throw new RuntimeException("Failed to update product", e);
        }
    }

    @Override
    public List<ProductOption> adminOptionsByProductId(int pNo) throws Exception {
        return productOptionMapper.adminOptionsByProductId(pNo);
    }


    // 제품 번호로 조회
    @Override
    public Product findUserWishList (int pNo) throws Exception {
        return productMapper.findUserWishList(pNo);
    }

    @Override
    public ProductOption getProductOptionByOptionId(int optionId) throws Exception {
        return productOptionMapper.getProductOptionByOptionId(optionId);
    }

    // 구매 업데이트 성공 시 재고 수량 - 1
    @Override
    public void minusQuantityByProductId(int optionId) throws Exception {
        productOptionMapper.minusQuantityByProductId(optionId);
    }

    // 옵션 아이디 별 재고 수량 확인
    public int checkStockQuantity(int optionId) throws Exception {
        return productOptionMapper.checkStockQuantity(optionId);
    }

    // 상품 상태 업데이트 
    public void changeStatus(int optionId) throws Exception {
        productOptionMapper.changeStatus(optionId);
    }

    // 조회수 증가
    public void updateViews(int pNo) throws Exception {
        productMapper.updateViews(pNo);
    }

}
