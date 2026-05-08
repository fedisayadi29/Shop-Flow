package com.shopflow.dto.response;

import java.util.List;

public class PageResponse<T> {

    private List<T> content; private int page; private int size;
    private long totalElements; private int totalPages; private boolean last; private boolean first;

    public PageResponse() {}

    public PageResponse(List<T> content, int page, int size, long totalElements,
                        int totalPages, boolean last, boolean first) {
        this.content = content; this.page = page; this.size = size;
        this.totalElements = totalElements; this.totalPages = totalPages;
        this.last = last; this.first = first;
    }

    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }

    public static <T> Builder<T> builder() { return new Builder<>(); }

    public static class Builder<T> {
        private List<T> content; private int page; private int size;
        private long totalElements; private int totalPages; private boolean last; private boolean first;

        public Builder<T> content(List<T> content) { this.content = content; return this; }
        public Builder<T> page(int page) { this.page = page; return this; }
        public Builder<T> size(int size) { this.size = size; return this; }
        public Builder<T> totalElements(long totalElements) { this.totalElements = totalElements; return this; }
        public Builder<T> totalPages(int totalPages) { this.totalPages = totalPages; return this; }
        public Builder<T> last(boolean last) { this.last = last; return this; }
        public Builder<T> first(boolean first) { this.first = first; return this; }

        public PageResponse<T> build() {
            return new PageResponse<>(content, page, size, totalElements, totalPages, last, first);
        }
    }
}
