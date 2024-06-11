package com.springproject.goodz.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.post.dto.Post;
import com.springproject.goodz.post.dto.Tag;
import com.springproject.goodz.post.mapper.TagMapper;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    private TagMapper tagMapper;

    // 상품태그 리스트 조회 - 상품번호 기준
    @Override
    public List<Post> taggedProduct(int productNo) throws Exception{
        return tagMapper.taggedProduct(productNo);
    }

    // 상품태그 추가 - 게시글 등록 시
    @Override
    public int insert(Tag tag) throws Exception {

        return tagMapper.insert(tag);

    }

    // 게시글에 종속된 상품태그 삭제 - 게시글 수정 시
    @Override
    public int delete(int postNo) throws Exception {
        return tagMapper.delete(postNo);
    }

    
}
