package com.springproject.goodz.product.dto;

import lombok.Data;

@Data
public class Page {
    private static final int PAGE_NUM = 1;
    private static final int ROWS = 10;
    private static final int COUNT = 10;

    private int page;
    private int rows;
    private int count;
    private int total;

    private int start;
    private int end;
    private int first;
    private int last;

    private int prev;
    private int next;

    private int index;
    private int totalPages;

    public Page() {
        this(PAGE_NUM, 0);
    }

    public Page(int total) {
        this(PAGE_NUM, total);
    }

    public Page(int page, int total) {
        this(page, ROWS, COUNT, total);
    }

    public Page(int page, int rows, int count, int total) {
        this.page = (page <= 0) ? PAGE_NUM : page;
        this.rows = rows;
        this.count = count;
        this.total = total;
        calc();
    }

    public void setTotal(int total) {
        this.total = total;
        calc();
    }

    public void setPage(int page) {
        this.page = page;
        calc();
    }

    private void calc() {
        this.first = 1;
        this.last = (this.total - 1) / rows + 1;
        this.start = ((page - 1) / count) * count + 1;
        this.end = this.start + count - 1;
        if (this.end > this.last) this.end = this.last;

        this.prev = this.page > 1 ? this.page - 1 : 1;
        this.next = this.page < this.last ? this.page + 1 : this.last;
        this.index = (this.page - 1) * this.rows;

        this.totalPages = this.last;
    }
}
