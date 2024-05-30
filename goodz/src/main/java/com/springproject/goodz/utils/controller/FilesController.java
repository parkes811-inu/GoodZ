package com.springproject.goodz.utils.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springproject.goodz.utils.MediaUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/files")
public class FilesController {

    // @Autowired
    // private FilesService filesService;

    @Value("${upload.path}")
    private String uploadPath;
    
    /**
     * 파일 삭제
     * @param no
     * @return
     * @throws Exception
     */
    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deleteFile(@PathVariable("id") String id) throws Exception {
    //     log.info("[DELETE] - /file/" + id);

    //     // 파일 삭제 요청
    //     int result = filesService.delete(id);

    //     // ✅ 삭제 성공
    //     if( result > 0 ) {
    //         return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    //     }

    //     // ❌ 삭제 실패
    //     return new ResponseEntity<>("FAIL", HttpStatus.OK);
    // }
    
    /**
     * 대표이미지
     * - /files/img?path=???Url
     * @param param
     * @return
     * @throws Exception 
     */
    @GetMapping("/img")
    public ResponseEntity<byte[]> thumbnailImg(@RequestParam("imgUrl") String imgUrl) throws Exception {
        // log.info("imgUrl : " + imgUrl);

        // 파일 번호로 파일 정보 조회
        // Files file = filesService.select(id);

        // Null 체크
        // if( file == null ) {
        if( imgUrl == null ) {
            String filePath = uploadPath + "/no-image.png";
            File noImageFile = new File(filePath);
            byte[] noImageFileData = FileCopyUtils.copyToByteArray(noImageFile);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(noImageFileData, headers, HttpStatus.OK);
        }

        // // 파일 정보 중에서 파일 경로 가져오기
        // String filePath = file.getPath();
        // String fileName = file.getName();
        // String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
        
        String filePath = imgUrl;
        String ext = filePath.substring(filePath.lastIndexOf('.') + 1);

        // // 파일 객체 생성
        File f = new File(filePath);
        
        // // 파일 데이터
        byte[] fileData = FileCopyUtils.copyToByteArray(f);

        
        // // 이미지 컨텐츠 타입 지정
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaUtil.getMediaType(ext);
        headers.setContentType(mediaType);        

        // new ResponseEntity<>( 데이터, 헤더, 상태코드 )
        return new ResponseEntity<>( fileData, headers, HttpStatus.OK );
    }
    
}