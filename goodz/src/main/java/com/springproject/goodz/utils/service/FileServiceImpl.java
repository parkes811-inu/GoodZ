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
public class FileServiceImpl implements FileService {
    
    @Autowired
    private FileMapper fileMapper;

    @Value("${upload.path}")    // application.properties에 설정한 업로드 경로
    private String uploadPath;

    @Override
    public List<Files> list() throws Exception {
        return fileMapper.list();
    }

    @Override
    public Files select(int no) throws Exception {
        return fileMapper.select(no);
    }

    @Override
    public int insert(Files file) throws Exception {
        return fileMapper.insert(file);
    }

    @Override
    public int update(Files file) throws Exception {
        return fileMapper.update(file);
    }

    @Override
    public int delete(int no) throws Exception {
        Files file = fileMapper.select(no);
        int result = fileMapper.delete(no);

        if (result > 0) {
            String filePath = file.getFilePath();
            File deleteFile = new File(filePath);

            if (deleteFile.exists() && deleteFile.delete()) {
                log.info("파일이 정상적으로 삭제되었습니다. file: " + filePath);
            } else {
                log.info("파일 삭제에 실패하였습니다. file: " + filePath);
            }
        }
        return result;
    }

    @Override
    public List<Files> listByParent(Files file) throws Exception {
        return fileMapper.listByParent(file);
    }

    @Override
    public int deleteByParent(Files file) throws Exception {
        List<Files> fileList = fileMapper.listByParent(file);
        int result = fileMapper.deleteByParent(file);

        for (Files deleteFile : fileList) {
            delete(deleteFile.getNo());
        }

        log.info(result + "개의 파일을 삭제하였습니다.");
        return result;
    }

    @Override
    public boolean upload(Files file, String dir) throws Exception {
        log.info("file: " + file);

        MultipartFile mf = file.getFile();
        if (mf == null || mf.isEmpty()) {
            throw new Exception("파일이 없습니다.");
        }

        String originName = mf.getOriginalFilename();
        long fileSize = mf.getSize();
        byte[] fileData = mf.getBytes();

        String fileDirPath = uploadPath + File.separator + dir;
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + originName;
        String filePath = fileDirPath + File.separator + fileName;

        File uploadFile = new File(filePath);
        FileCopyUtils.copy(fileData, uploadFile);

        file.setFileName(fileName);
        file.setOriginName(originName);
        file.setParentTable(dir);
        file.setFilePath(filePath);
        file.setFileSize(fileSize);

        fileMapper.insert(file);

        return true;
    }

    @Override
    public Files download(int no) throws Exception {
        return fileMapper.select(no);
    }
}
