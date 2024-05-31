package com.springproject.goodz.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.post.dto.Post;
import com.springproject.goodz.post.mapper.PostMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostMapper postMapper;

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
     * 게시글 등록
     */
    @Override
    public int insert(Post post) throws Exception {
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
