package com.mangobits.startupkit.social.post;

import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.List;

public class PostResultSearch {


    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<Post> listPosts;

    // numero total de itens encontrados
    private int totalAmount;

    // numero de paginas
    private int pageQuantity;


    public List<Post> getListPosts() {
        return listPosts;
    }

    public void setListPosts(List<Post> listPosts) {
        this.listPosts = listPosts;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPageQuantity() {
        return pageQuantity;
    }

    public void setPageQuantity(int pageQuantity) {
        this.pageQuantity = pageQuantity;
    }
}
