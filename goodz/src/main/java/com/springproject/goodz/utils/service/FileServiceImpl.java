package com.springproject.goodz.utils.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.mapper.FileMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService{
    
    @Autowired
    private FileMapper fileMapper;

    @Value("${upload.path}")    // application.properties에 설정한 업로드 경로
    private String uploadPath;

    /**
     * 전체파일 조회
     */
    @Override
    public List<Files> list() throws Exception {

        List<Files> fileList = fileMapper.list();

        return fileList;
    }

    /**
     * 파일 조회
     */
    @Override
    public Files select(int no) throws Exception {
        log.info("::::::::::fileService::::::::::");
        log.info("조회할 파일 번호: " + no);

        Files file = fileMapper.select(no);

        log.info("조회된 파일 정보: " + file);
        
        return file;
    }

    /**
     * 파일 등록
     */
    @Override
    public int insert(Files file) throws Exception {
        int result = fileMapper.insert(file);

        return result;
    }

    /**
     * 파일 수정
     */
    @Override
    public int update(Files file) throws Exception {
        int result = fileMapper.update(file);

        return result;
    }

    /**
     * 파일 삭제 (시스템 상 실제파일 삭제)
     */
    @Override
    public int delete(int no) throws Exception {

        // 파일 정보 조회 (파일시스템에서 삭제할 때, 경로랑 이름이 필요해서 조회한거임)
        Files file = fileMapper.select(no);

        // DB 에 있는 파일 정보 삭제
        int result = fileMapper.delete(no);

        // 파일 시스템의 파일 삭제
        if (result > 0) {
            String filePath = file.getFilePath();
            File deleteFile = new File(filePath);

            // 파일 존재 확인
            if ( !deleteFile.exists() ) {
                return result;
            }

            // 파일 삭제
            if (deleteFile.delete()) {
                log.info("파일이 정상적으로 삭제되었습니다.");
                log.info("file: " + filePath);
            } else {
                log.info("파일 삭제에 실패하였습니다.");
            }
        }
        return result;
    }

    
    /**
     * 게시글에 종속된 첨부파일 리스트 불러오기
     */
    @Override
    public List<Files> listByParent(Files file) throws Exception {
        List<Files> fileList = fileMapper.listByParent(file);

        return fileList;
    }

    /**
     * 게시글에 종속된 첨부파일 모두 삭제 (실제 시스템 상 삭제)
     */
    @Override
    public int deleteByParent(Files file) throws Exception {

        List<Files> fileList = fileMapper.listByParent(file);

        int result = fileMapper.deleteByParent(file);

        for (Files deleteFile : fileList) {
            int no = deleteFile.getNo();
            delete(no);
        }

        log.info(result + "개의 파일을 삭제하였습니다.");
        return result;
    }

    /**
     * 파일 업로드
     */
    @Override
    public boolean upload(Files file) throws Exception {
        uploadPath = "C:/upload";

        log.info("file: " + file);

        String dir = file.getParentTable();     

        MultipartFile mf = file.getFile();
        // 파일 정보 : 원본 파일명, 파일 용량, 파일 데이터
        String originName = mf.getOriginalFilename();
        long fileSize = mf.getSize();
        byte[] fileData = mf.getBytes();
        
        // ⭐파일 업로드
        // - 파일 시스템의 해당 파일을 복사
        // - 파일 정보를 DB에 등록

        // ✅ 업로드 경로 - application.properties ( upload.path )
        // ✅ 파일명
        // - 파일명 중복 방지를 위해 "UID_파일명.확장자" 형식으로 지정
        // - 업로드 파일명 : UID_원본파일명.확장자
        String fileName = UUID.randomUUID().toString() + "_" + originName;
        uploadPath +=  "/" + dir;

        log.info("업로드 경로: " + uploadPath);
        // File 객체 생성 => new File(업로드 경로, 설정할 파일명);
        File uploadFile = new File(uploadPath, fileName);

        log.info("최종 업로드 경로:" + uploadFile);

        // 파일 업로드 (유저가 서버에 요청한 파일을 복사해서 경로에 넣음)
        FileCopyUtils.copy(fileData, uploadFile);

        file.setFileName(fileName);
        file.setOriginName(originName);
        // filePath = C:/uploade/UID_원본파일명.확장자

        String filePath = uploadPath + "/" + fileName;
        log.info("파일패쓰:" + filePath);
        file.setFilePath(filePath);
        file.setFileSize(fileSize);
        
        fileMapper.insert(file);

        return true;
    }

    /**
     * 파일 다운로드
     */
    @Override
    public Files download(int no) throws Exception {

        Files file = fileMapper.select(no);

        // 다운로드 시, 추가 작업
        // ...

        return file;
    }

}
