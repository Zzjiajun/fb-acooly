package com.acooly.showcase.daliy;


import java.util.List;

public class Pagination<T> {
    private int totalItems;
    private int currentPage;
    private int itemsPerPage;
    private List<T> currentPageItems;

    public Pagination(int totalItems, int currentPage, int itemsPerPage,List<T> list) {
        this.totalItems = totalItems;
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
        this.currentPageItems = list;
    }

    public void calculatePagination() {
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);
        currentPageItems=currentPageItems.subList(startIndex,endIndex);
//        for (int i = startIndex; i < endIndex; i++) {
//            // 这里可以根据实际需求从数据库或其他数据源获取数据
//            currentPageItems.add(new Object()); // 示例数据
//        }
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public List<T> getCurrentPageItems() {
        return currentPageItems;
    }
}
