package com.idealizer.review_x.application.modules.games.controllers.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;

@JsonPropertyOrder({
        "pageNumber",
        "limit",
        "totalElements",
        "totalPages",
        "last",
        "data"
})
public class FindGamesResponse {
    private List<SimpleGameDTO> data;
    private int pageNumber;
    private int limit;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public FindGamesResponse() {
    }

    public FindGamesResponse(List<SimpleGameDTO> data, int pageNumber, int limit, long totalElements, boolean last,
            int totalPages) {
        this.pageNumber = pageNumber;
        this.limit = limit;
        this.totalElements = totalElements;
        this.last = last;
        this.totalPages = totalPages;
        this.data = data;
    }

    public List<SimpleGameDTO> getData() {
        return data;
    }

    public void setData(List<SimpleGameDTO> data) {
        this.data = data;
    }

    public int getpageNumber() {
        return pageNumber;
    }

    public void setpageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        FindGamesResponse that = (FindGamesResponse) o;
        return pageNumber == that.pageNumber && limit == that.limit && totalElements == that.totalElements
                && totalPages == that.totalPages && last == that.last && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, pageNumber, limit, totalElements, totalPages, last);
    }
}
