package com.mangobits.startupkit.social.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.Date;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name= "group")
@Indexed
public class Group {


    @Id
    @DocumentId
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;


    @Field
    private Date creationDate;

    @Field
    private String category; //userPreference

    @Field
    private String title;

    @Field
    private String desc;

    @Transient
    private Integer numberOfMembers;



    @Field
    @Enumerated(EnumType.STRING)
    private SimpleStatusEnum status;

    public Group(){
    }

    public Group(String id){
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


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SimpleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SimpleStatusEnum status) {
        this.status = status;
    }


    public Integer getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(Integer numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }

}
