package org.startupkit.social.groupInfo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.startupkit.core.dao.AbstractDAO;
import org.startupkit.social.group.UserGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupInfoDAO extends AbstractDAO<GroupInfo> {

    public GroupInfoDAO() {
        super(GroupInfo.class);
    }


    @Override
    public Object getId(GroupInfo obj) {
        return obj.getId();
    }

    public List<UserGroup> listActiveUsers(String idGroup) throws Exception {

        List<UserGroup> list = new ArrayList<>();
        MongoDatabase db = mongoClientProvider.getDb();

        MongoCollection<Document> myColl = db.getCollection("groupInfo");

        BasicDBObject matchObj = new BasicDBObject("_id", idGroup);
        Document match = new Document( "$match", matchObj);

        BasicDBObject unwindObj = new BasicDBObject( "path", "$listUsers");
        Document unwind = new Document( "$unwind", unwindObj);

        BasicDBObject sortObj = new BasicDBObject( "listUsers.nameUser", 1);
        Document sort = new Document( "$sort", sortObj);

        List<Document> listResults = new ArrayList<>();
        myColl.aggregate(Arrays.asList(match, unwind, sort)).into(listResults);

        for(Document obj : listResults){

            UserGroup item = new UserGroup();
            BasicDBObject obj2 = (BasicDBObject) obj.get("listUsers");
            item.setIdUser((String) obj2.get("idUser"));
            item.setNameUser((String) obj2.get("nameUser"));
            item.setIdGroup((String) obj2.get("idGroup"));
            item.setFgAdmin((Boolean) obj2.get("fgAdmin"));

            list.add(item);
        }

        return list;
    }
}