package com.mangobits.startupkit.social.spider;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.List;

@Indexed
@Embeddable
public class InfoUrl {

	@Field
	private String url;
	
	
	private String urlPhoto;
	
	
	private String title;
	
	
	private String desc;


	private String siteName;


	@Transient
	@JsonIgnore
	private List<String> listTags;

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlPhoto() {
		return urlPhoto;
	}

	public void setUrlPhoto(String urlFoto) {
		this.urlPhoto = urlFoto;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titulo) {
		this.title = titulo;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String descricao) {
		this.desc = descricao;
	}


	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public List<String> getListTags() {
		return listTags;
	}

	public void setListTags(List<String> listTags) {
		this.listTags = listTags;
	}
}
