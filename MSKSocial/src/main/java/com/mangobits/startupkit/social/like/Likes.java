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
    private String idOjbect;


	@Field
	private String typeObject;


	@ElementCollection(fetch = FetchType.EAGER)
	private List<Like> listLikesMe;


	@ElementCollection(fetch = FetchType.EAGER)
	private List<Like> listILike;
		
	
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


	public List<Like> getListLikesMe() {
		return listLikesMe;
	}

	public void setListLikesMe(List<Like> likesMe) {
		this.listLikesMe = likesMe;
	}

	public List<Like> getListILike() {
		return listILike;
	}

	public void setListILike(List<Like> likesMyself) {
		this.listILike = likesMyself;
	}

	public String getTypeObject() {
		return typeObject;
	}

	public void setTypeObject(String typeObject) {
		this.typeObject = typeObject;
	}
}
