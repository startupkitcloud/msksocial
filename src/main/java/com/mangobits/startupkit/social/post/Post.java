package com.mangobits.startupkit.social.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mangobits.startupkit.core.address.AddressInfo;
import com.mangobits.startupkit.core.photo.GalleryItem;
import com.mangobits.startupkit.social.comment.Comment;
import com.mangobits.startupkit.social.spider.InfoUrl;
import com.mangobits.startupkit.social.survey.Survey;
import com.mangobits.startupkit.social.survey.SurveyOption;
import com.mangobits.startupkit.user.UserCard;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.*;

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
    @SortableField
    private Date creationDate;


    @Field(indexNullAs = "_null_")
    private String idGroup;

    @Field
    private String desc;

    @Field
    private String title;


    // aonde o post vai estar, por exemplo, na home ou em outra tela
    @Field
    private String section;


    @IndexedEmbedded
    private UserCard userCreator;


    @IndexedEmbedded
    private Survey survey;


    private Integer likes;


    @Transient
    private Boolean fgSurveyAnswered;



    @Transient
    private Boolean fgFormAnswered;

    @Transient
    private Boolean fgFormRegistered;



    private Boolean fgNotification;



    @Transient
    private Double distance;


    private Integer comments;


    @Transient
    private Boolean fgLiked;

    @Transient
    private Boolean fgFavorite;

    @Field
    @IndexedEmbedded
    @ElementCollection(fetch=FetchType.EAGER)
    private List<String> listTags;


    @Transient
    private List<Comment> lastComments;


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

    @SortableField
    @Spatial
    @IndexedEmbedded
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

    public PostStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PostStatusEnum status) {
        this.status = status;
    }


    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
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

    public List<Comment> getLastComments() {
        return lastComments;
    }

    public Boolean getFgLiked() {
        return fgLiked;
    }

    public void setFgLiked(Boolean fgLiked) {
        this.fgLiked = fgLiked;
    }

    public Boolean getFgFavorite() {
        return fgFavorite;
    }

    public void setFgFavorite(Boolean fgFavorite) {
        this.fgFavorite = fgFavorite;
    }

    public void setLastComments(List<Comment> lastComments) {
        this.lastComments = lastComments;
    }


    public List<String> getListTags() {
        return listTags;
    }

    public void setListTags(List<String> listTags) {
        this.listTags = listTags;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Boolean getFgNotification() {
        return fgNotification;
    }

    public void setFgNotification(Boolean fgNotification) {
        this.fgNotification = fgNotification;
    }

    public Boolean getFgSurveyAnswered() {
        return fgSurveyAnswered;
    }

    public void setFgSurveyAnswered(Boolean fgSurveyAnswered) {
        this.fgSurveyAnswered = fgSurveyAnswered;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Boolean getFgFormAnswered() {
        return fgFormAnswered;
    }

    public void setFgFormAnswered(Boolean fgFormAnswered) {
        this.fgFormAnswered = fgFormAnswered;
    }

    public Boolean getFgFormRegistered() {
        return fgFormRegistered;
    }

    public void setFgFormRegistered(Boolean fgFormRegistered) {
        this.fgFormRegistered = fgFormRegistered;
    }

}