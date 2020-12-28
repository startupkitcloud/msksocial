package org.startupkit.social.post;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.startupkit.core.address.AddressInfo;
import org.startupkit.core.annotation.MSKEntity;
import org.startupkit.core.annotation.MSKId;
import org.startupkit.core.photo.GalleryItem;
import org.startupkit.social.comment.Comment;
import org.startupkit.social.spider.InfoUrl;
import org.startupkit.social.survey.Survey;
import org.startupkit.user.UserCard;

import javax.json.bind.annotation.JsonbDateFormat;
import java.util.Date;
import java.util.List;

@MSKEntity(name = "post")
public class Post {


    @MSKId
    private String id;

    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    private String idGroup;

    private String desc;

    private String title;

    private String idObj;

    private String section;

    private UserCard userCreator;

    private Survey survey;

    private Integer likes;

    @BsonIgnore
    private Boolean fgSurveyAnswered;

    @BsonIgnore
    private Boolean fgFormAnswered;

    @BsonIgnore
    private Boolean fgFormRegistered;

    private Boolean fgNotification;

    @BsonIgnore
    private Double distance;

    private Integer comments;

    @BsonIgnore

    private Boolean fgLiked;

    @BsonIgnore
    private Boolean fgFavorite;

    private List<String> listTags;

    @BsonIgnore
    private List<Comment> lastComments;

    private List<GalleryItem> gallery;

    private PostStatusEnum status;

    private PostTypeEnum type;

    private InfoUrl infoUrl;

    private Integer totalViews;

    private AddressInfo address;

    public Post() {
    }

    public Post(String id) {
        this.id = id;
    }

    public String getIdObj() {
        return idObj;
    }

    public void setIdObj(String idObj) {
        this.idObj = idObj;
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

    public void setLastComments(List<Comment> lastComments) {
        this.lastComments = lastComments;
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
