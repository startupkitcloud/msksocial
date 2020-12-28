package org.startupkit.social.post;

import java.util.List;

public class PostResultSearch {

    private List<Post> listPosts;

    private long totalAmount;

    private long pageQuantity;

    public List<Post> getListPosts() {
        return listPosts;
    }

    public void setListPosts(List<Post> listPosts) {
        this.listPosts = listPosts;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getPageQuantity() {
        return pageQuantity;
    }

    public void setPageQuantity(long pageQuantity) {
        this.pageQuantity = pageQuantity;
    }
}
