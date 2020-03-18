package com.mangobits.startupkit.social.post;

import com.mangobits.startupkit.core.dao.AbstractDAO;
import com.mangobits.startupkit.core.exception.DAOException;
import com.mangobits.startupkit.social.comment.Comment;
import com.mongodb.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PostDAO extends AbstractDAO<Post> {

    public PostDAO(){
        super(Post.class);
    }


    @Override
    public Object getId(Post obj) {
        return obj.getId();
    }


}
