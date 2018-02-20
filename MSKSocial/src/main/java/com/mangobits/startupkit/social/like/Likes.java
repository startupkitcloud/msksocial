package com.mangobits.startupkit.social.like;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name="likes")
@Indexed
public class Likes {

	
	@Id
	@DocumentId
    protected String idOjbect;


	@Field
	protected String typeObject;


	@ElementCollection(fetch = FetchType.EAGER)
	protected List<Like> listLikesMe;



	@ElementCollection(fetch = FetchType.EAGER, targetClass = Like.class)
	protected List<Like> listILike;
		
	
	public Likes(){
		
	}
	
	
	public Likes(String idOjbect){
		this.idOjbect = idOjbect;
	}


	public String getIdOjbect() {
		return idOjbect;
	}

	public void setIdOjbect(String idOjbect) {
		this.idOjbect = idOjbect;
	}

	public String getTypeObject() {
		return typeObject;
	}

	public void setTypeObject(String typeObject) {
		this.typeObject = typeObject;
	}

	public List<Like> getListLikesMe() {
		return listLikesMe;
	}

	public void setListLikesMe(List<Like> listLikesMe) {
		this.listLikesMe = listLikesMe;
	}

	public List<Like> getListILike() {
		return listILike;
	}

	public void setListILike(List<Like> listILike) {
		this.listILike = listILike;
	}
}
