package com.mangobits.startupkit.social.postInfo;

import com.mangobits.startupkit.core.dao.AbstractDAO;
import com.mangobits.startupkit.core.exception.DAOException;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import com.mangobits.startupkit.social.comment.Comment;
import com.mangobits.startupkit.social.post.Post;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PostInfoDAO extends AbstractDAO<PostInfo> {

    public PostInfoDAO(){
        super(PostInfo.class);
    }


    @Override
    public Object getId(PostInfo obj) {
        return obj.getId();
    }

    public List<Comment> listActiveComments(String idPost, int count) throws DAOException {

        List<DBObject> listaResultados = new ArrayList<>();
        List<Comment> list = new ArrayList<>();


        try {

            DB db = getDBMongo();

            DBCollection myColl = db.getCollection("postInfo");


            DBObject matchObj = new BasicDBObject("_id", idPost);
            DBObject match = new BasicDBObject( "$match", matchObj);

            DBObject unwindObj = new BasicDBObject( "path", "$listActiveComments");
            DBObject unwind = new BasicDBObject( "$unwind", unwindObj);

            DBObject sortObj = new BasicDBObject( "listActiveComments.creationDate", -1);
            DBObject sort = new BasicDBObject( "$sort", sortObj);

            DBObject limit = new BasicDBObject( "$limit", count);


            AggregationOutput output = myColl.aggregate(Arrays.asList(match, unwind, sort, limit));
            listaResultados = (List<DBObject>) output.results();

            for(DBObject obj : listaResultados){

                Comment item = new Comment();
                BasicDBObject obj2 = (BasicDBObject) obj.get("listActiveComments");
                item.setIdPost((String) obj2.get("idPost"));
                item.setId((String) obj2.get("id"));
                item.setIdUser((String) obj2.get("idUser"));
                item.setNameUser((String) obj2.get("nameUser"));
                item.setText((String) obj2.get("text"));
                item.setStatus(SimpleStatusEnum.ACTIVE);
                item.setCreationDate((Date) obj2.get("creationDate"));

                list.add(item);

            }

            mongoClient.close();

        }
        catch (Exception e) {

            throw new DAOException("Houve um erro ao efetuar uma listagem de comentários", e);
        }

        return list;
    }
}
