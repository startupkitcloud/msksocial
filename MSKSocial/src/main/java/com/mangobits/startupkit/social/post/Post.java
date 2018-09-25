package com.mangobits.startupkit.social.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mangobits.startupkit.core.address.AddressInfo;
import com.mangobits.startupkit.core.photo.GalleryItem;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import com.mangobits.startupkit.social.spider.InfoUrl;
import com.mangobits.startupkit.user.UserCard;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name= "post")
@Indexed
public class Post {


    @Id
    @DocumentId
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;


    @Field
    private Date creationDate;


    private String desc;


    @IndexedEmbedded
    private UserCard userCreator;


    private Integer likes;


    private Integer views;


    private Integer time;


    private Integer distance;


    private Integer comments;




    @ElementCollection(fetch=FetchType.EAGER)
    private List<GalleryItem> gallery;


    @Field
    @Enumerated(EnumType.STRING)
    private PostStatusEnum status;


    @Field
    @Enumerated(EnumType.STRING)
    private PostTypeEnum type;


    @IndexedEmbedded
    private InfoUrl infoUrl;


    private Integer totalViews;


    private AddressInfo address;


    public Post(){
    }

    public Post(String id){
        this.id = id;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UserCard getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(UserCard userCard) {
        this.userCreator = userCard;
    }



    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public PostStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PostStatusEnum status) {
        this.status = status;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }


    public List<GalleryItem> getGallery() {
        return gallery;
    }

    public void setGallery(List<GalleryItem> gallery) {
        this.gallery = gallery;
    }

    public InfoUrl getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(InfoUrl infoUrl) {
        this.infoUrl = infoUrl;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public AddressInfo getAddress() {
        return address;
    }

    public void setAddress(AddressInfo address) {
        this.address = address;
    }


    public PostTypeEnum getType() {
        return type;
    }

    public void setType(PostTypeEnum type) {
        this.type = type;
    }
}
