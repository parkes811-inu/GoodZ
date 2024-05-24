package com.springproject.goodz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequestMapping("/styles")
public class StyleController {
    
    /**
     * 전체 게시글 목록
     * @return
     */
    @GetMapping("")
    public String list() {
        return "/style/list";
    }

    /**
     * 게시글 상세
     * @return
     */
    @GetMapping("/read")
    public String read() {
        return "/style/read";
    }
    
}
