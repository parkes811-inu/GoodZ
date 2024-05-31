package com.springproject.goodz.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.goodz.post.dto.Post;
import com.springproject.goodz.post.mapper.PostMapper;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private FileService fileService;

    /**
     * 게시글 조회
     */
    @Override
    public Post select(int no) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'select'");
    }

    /**
     * 게시글 조회 - id 기준
     */
    @Override
    public List<Post> selectById(String userId) throws Exception {

        List<Post> postList = postMapper.selectById(userId);

        return postList;
    }

    /**
     * 마지막 게시글번호 조회 - 첨부파일 등록 시 필요함
     */
    @Override
    public int maxNo() throws Exception {

        int maxNo = postMapper.maxNo();

        return maxNo;
    }

    /**
     * 게시글 등록
     */
    @Override
    public int insert(Post post) throws Exception {

        /* ⬇️ '글' 등록 처리⬇️ */
        int result = postMapper.insert(post);

        /* ⬇️ '첨부파일' 등록 처리⬇️ */
        String parentTable = "post";
        int parentNo = postMapper.maxNo(); // 방금 등록처리된 게시글 번호를 가져옴

        List<MultipartFile> attachedFiles = post.getAttachedFiles();

        // 대표 이미지 업로드
        // 필요정보: 부모테이블, 부모번호, 멀티파트파일, 대표이미지 인덱스
        int mainImgIndex = post.getMainImgIndex();
        MultipartFile mainImg = attachedFiles.get(mainImgIndex); // 대표이미지

        // 깡통인지 체크
        if (!attachedFiles.isEmpty()) {
            for (int i = 0; i < attachedFiles.size(); i++) {

                MultipartFile attachedFile = attachedFiles.get(i);

                // 빈 파일인지 체크
                if (attachedFile.isEmpty()) {
                    continue;
                }

                // fileService에 매개변수로 넘길 file 객체 세팅
                Files uploadFile = new Files();
                uploadFile.setParentTable(parentTable);
                uploadFile.setParentNo(parentNo);

                // 대표이미지 파일코드: 1
                if (i == mainImgIndex) {
                    uploadFile.setFileCode(1);
                }

                boolean uploadcheck = fileService.upload(uploadFile, parentTable);

                if (uploadcheck) {
                    log.info((i+1) + "번째 파일 업로드 성공...");
                }
            }
        }


        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public int update(Post post) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public int delete(int no) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
