package com.mangobits.startupkit.social.post;


import com.mangobits.startupkit.core.configuration.ConfigurationEnum;
import com.mangobits.startupkit.core.configuration.ConfigurationService;
import com.mangobits.startupkit.core.dao.SearchBuilder;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.core.photo.GalleryItem;
import com.mangobits.startupkit.core.photo.PhotoUpload;
import com.mangobits.startupkit.core.photo.PhotoUtils;
import com.mangobits.startupkit.core.utils.BusinessUtils;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PostServiceimpl implements PostService {

    @New
    @Inject
    private PostDAO postDAO;


    @EJB
    private ConfigurationService configurationService;

    @Override
    public void changeStatus(String idPost) throws Exception {

        Post post = retrieve(idPost);

        if(post.getStatus().equals(PostStatusEnum.ACTIVE)){
            post.setStatus(PostStatusEnum.BLOCKED);
        }
        else{
            post.setStatus(PostStatusEnum.ACTIVE);
        }

        save(post);


    }

    @Override
    public void save(Post post) throws Exception {

        if(post.getStatus() == null){
            post.setStatus(PostStatusEnum.ACTIVE);
        }

        if(post.getId() == null){
            post.setCreationDate(new Date());
            postDAO.insert(post);
        }else {
            new BusinessUtils<>(postDAO).basicSave(post);
        }

    }


    @Override
    public List<Post> listAll() throws Exception {

        List<Post> list = this.postDAO.search((new SearchBuilder()).appendParam("status", PostStatusEnum.ACTIVE).build());

        return list;
    }


    @Override
    public List<Post> listPending() throws Exception {

        List<Post> list = this.postDAO.search((new SearchBuilder()).appendParam("status", PostStatusEnum.PENDING).build());

        return list;
    }

    @Override
    public Post retrieve(String idPost) throws Exception {

        Post post =  postDAO.retrieve(new Post(idPost));

        return post;
    }

    @Override
    public void saveImage(PhotoUpload photoUpload) throws Exception{

        Post post = retrieve(photoUpload.getIdObject());

        if(post == null){
            throw new BusinessException("post_not_found");
        }

        //get the final size
        int finalWidth = configurationService.loadByCode("SIZE_DETAIL_MOBILE").getValueAsInt();
        photoUpload.setFinalWidth(finalWidth);

        GalleryItem gi = new GalleryItem();
        gi.setId(photoUpload.getIdSubObject());

        if(post.getGallery() == null){
            post.setGallery(new ArrayList<>());
        }

        GalleryItem item = post.getGallery().stream()
                .filter(p -> p.getId().equals(gi.getId()))
                .findFirst()
                .orElse(null);


        if(item == null){
            post.getGallery().add(gi);
            postDAO.update(post);
        }

        String path = configurationService.loadByCode(ConfigurationEnum.PATH_BASE).getValue() + "/post/" + post.getId();

        new PhotoUtils().saveImage(photoUpload, path, gi.getId());
    }

    @Override
    public List<Post> searchByNewsUrl(String newsUrl) throws Exception {

        return postDAO.search(new SearchBuilder()
                .appendParam("infoUrl.url", newsUrl)
                .build());
    }

    @Override
    public Post load(String id) throws Exception {
        return postDAO.retrieve(new Post(id));
    }
}