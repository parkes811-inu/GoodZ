package com.springproject.goodz.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.post.dto.Comment;
import com.springproject.goodz.post.mapper.CommentMapper;

import groovy.util.logging.Slf4j;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentMapper cmmtMapper;

    @Override
    public List<Comment> list(int postNo) throws Exception {

        List<Comment> cmmtList = cmmtMapper.list(postNo);

        return cmmtList;
    }

    @Override
    public Comment select(int no) throws Exception {

        Comment cmmt = cmmtMapper.select(no);

            return cmmt;
    }

    @Override
    public int insert(Comment comment) throws Exception {

        int result = cmmtMapper.insert(comment);

        return result;
    }

    @Override
    public int update(Comment comment) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public int delete(int no) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public int deleteByBoardNo(int boardNo) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteByBoardNo'");
    }

    @Override
    public int max() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'max'");
    }
    
}
