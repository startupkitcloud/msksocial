package org.startupkit.social.postInfo;

import org.startupkit.social.comment.Comment;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PostInfoServiceImpl implements PostInfoService{

    @New
    @Inject
    private PostInfoDAO postInfoDAO;

    @Override
    public PostInfo retrieve(String idPost) throws Exception {

        PostInfo postInfo =  postInfoDAO.retrieve(new PostInfo(idPost));

        if (postInfo == null) {
            postInfo = new PostInfo();
            postInfo.setId(idPost);
        }
        if (postInfo.getListActiveComments() == null){
            postInfo.setListActiveComments(new ArrayList<>());
        }
        if (postInfo.getListBlockedComments() == null){
            postInfo.setListBlockedComments(new ArrayList<>());
        }

        return postInfo;
    }

    @Override
    public List<Comment> listActiveComments(String idPost) throws Exception {
        List<Comment> list = postInfoDAO.listActiveComments(idPost, 70);
        return list;
    }
}
