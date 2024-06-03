package com.springproject.goodz.utils.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springproject.goodz.utils.MediaUtil;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;


/*
 * 파일 컨트롤러
 * [GET]    /files/{file_no}    // 이미지 요청
 * 
 */
@Slf4j
@Controller
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private FileService fileService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    
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
     * 대표이미지 보이기
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

    /**
     * 이미지 로드
     * @param file_no
     * @return
     * @throws Exception
     */
    @GetMapping("/{file_no}")
    public ResponseEntity<byte[]> LoadMainImg(@PathVariable("file_no") int file_no) throws Exception {
        log.info("fileController");

        // 파일 경로로 전달받은 파일 번호로, 파일 정보 조회
        Files requestFile = fileService.select(file_no);

        // 파일이 Null인 경우 처리
        if (requestFile == null) {
            String filepath = uploadPath + "/no-img.jpg";

            File noImgFile = new File(filepath);    // 대체 이미지를 설정할 java 파일객체 생성
            byte[] noImgFileData = FileCopyUtils.copyToByteArray(noImgFile); // 이미지 업로드를 위해 서버 이미지 복제
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(noImgFileData, headers, HttpStatus.OK);
        }
        
        // 파일 정보 중, 파일 경로 가져오기
        String filePath = requestFile.getFilePath();

        // 실제 파일 객체 생성 (자바에 있는 파일 입출력 객체) -> 파일 경로를 넣어주면 해당 파일이 대입됨.
        File mainImg = new File(filePath);

        // 파일 데이터 -> 바이트 데이터를 가져와줌.
        byte[] fileData = FileCopyUtils.copyToByteArray(mainImg);

        // 이미지 컨텐츠 타입 지정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        // ResponseEntity<> (데이터, 헤더, 상태코드)
        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    
    
    
}