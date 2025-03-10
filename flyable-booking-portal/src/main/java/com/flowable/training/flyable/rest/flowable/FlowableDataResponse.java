package com.flowable.training.flyable.rest.flowable;

import java.util.List;

/**
 * Response from most Flowable APIs will be paginated.
 * This class represents the response from Flowable APIs.
 * @param <T> The type of data in the response.
 */
public class FlowableDataResponse<T> {

    List<T> data;
    long total;
    int start;
    String sort;
    String order;
    int size;

    public List<T> getData() {
        return data;
    }

    public FlowableDataResponse<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
