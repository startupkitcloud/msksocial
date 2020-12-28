package org.startupkit.social.group;

import org.startupkit.core.annotation.MSKEntity;
import org.startupkit.core.annotation.MSKId;
import org.startupkit.core.status.SimpleStatusEnum;

import javax.json.bind.annotation.JsonbDateFormat;
import java.util.Date;

@MSKEntity(name= "group")
public class   Group {


    @MSKId
    private String id;

    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    private String category; //userPreference

    private String title;

    private String desc;

    private Integer numberOfMembers;

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
