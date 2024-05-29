package com.springproject.goodz.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.post.dto.Post;
import com.springproject.goodz.product.dto.Brand;
import com.springproject.goodz.product.mapper.BrandMapper;

import groovy.util.logging.Slf4j;
import groovyjarjarantlr4.v4.parse.ANTLRParser.ruleEntry_return;

@Slf4j
@Service
public class BrandServiceImpl implements BrandService{

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 브랜드 목록 조회
     */
    @Override
    public List<Brand> list() throws Exception {
        
        List<Brand> brandList = brandMapper.list();

        return brandList; 
    }

    /**
     * 브랜드 등록 처리
     */
    @Override
    public int insert(Brand brand) throws Exception {

        int result = brandMapper.insert(brand);

        return result;
    }


    
}
