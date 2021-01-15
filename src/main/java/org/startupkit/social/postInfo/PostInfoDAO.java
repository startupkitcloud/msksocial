package org.startupkit.social.postInfo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.startupkit.core.dao.AbstractDAO;
import org.startupkit.core.status.SimpleStatusEnum;
import org.startupkit.social.comment.Comment;

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

    public List<Comment> listActiveComments(String idPost, int count) throws Exception {

        List<DBObject> listaResultados = new ArrayList<>();
        List<Comment> list = new ArrayList<>();


        MongoDatabase db = mongoClientProvider.getDb();

        MongoCollection<Document> myColl = db.getCollection("postInfo");

        DBObject matchObj = new BasicDBObject("_id", idPost);
        Document match = new Document( "$match", matchObj);

        DBObject unwindObj = new BasicDBObject( "path", "$listActiveComments");
        Document unwind = new Document( "$unwind", unwindObj);

        DBObject sortObj = new BasicDBObject( "listActiveComments.creationDate", 1);
        Document sort = new Document( "$sort", sortObj);

        Document limit = new Document( "$limit", count);

        List<Document> listResults = new ArrayList<>();
        myColl.aggregate(Arrays.asList(match, unwind, sort, limit)).into(listResults);

        for(Document obj : listResults){

            Comment item = new Comment();
            Document obj2 = (Document) obj.get("listActiveComments");
            item.setIdPost((String) obj2.get("idPost"));
            item.setId((String) obj2.get("id"));
            item.setIdUser((String) obj2.get("idUser"));
            item.setNameUser((String) obj2.get("nameUser"));
            item.setText((String) obj2.get("text"));
            item.setStatus(SimpleStatusEnum.ACTIVE);
            item.setCreationDate((Date) obj2.get("creationDate"));

            list.add(item);

        }

        return list;
    }
}
