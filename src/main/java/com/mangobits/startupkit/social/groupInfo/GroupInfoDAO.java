package com.mangobits.startupkit.social.groupInfo;

import com.mangobits.startupkit.core.dao.AbstractDAO;
import com.mangobits.startupkit.core.exception.DAOException;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import com.mangobits.startupkit.social.comment.Comment;
import com.mangobits.startupkit.social.group.Group;
import com.mangobits.startupkit.social.group.UserGroup;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GroupInfoDAO extends AbstractDAO<GroupInfo> {

    public GroupInfoDAO() {
        super(GroupInfo.class);
    }


    @Override
    public Object getId(GroupInfo obj) {
        return obj.getId();
    }

    public List<UserGroup> listActiveUsers(String idGroup) throws DAOException {

        List<DBObject> listaResultados = new ArrayList<>();
        List<UserGroup> list = new ArrayList<>();


        try {

            DB db = getDBMongo();

            DBCollection myColl = db.getCollection("groupInfo");


            DBObject matchObj = new BasicDBObject("_id", idGroup);
            DBObject match = new BasicDBObject( "$match", matchObj);

            DBObject unwindObj = new BasicDBObject( "path", "$listUsers");
            DBObject unwind = new BasicDBObject( "$unwind", unwindObj);

            DBObject sortObj = new BasicDBObject( "listUsers.nameUser", 1);
            DBObject sort = new BasicDBObject( "$sort", sortObj);

           // DBObject limit = new BasicDBObject( "$limit", count);


//            AggregationOutput output = myColl.aggregate(Arrays.asList(match, unwind, sort, limit));
            AggregationOutput output = myColl.aggregate(Arrays.asList(match, unwind, sort));

            listaResultados = (List<DBObject>) output.results();

            for(DBObject obj : listaResultados){

                UserGroup item = new UserGroup();
                BasicDBObject obj2 = (BasicDBObject) obj.get("listUsers");
                item.setIdUser((String) obj2.get("idUser"));
                item.setNameUser((String) obj2.get("nameUser"));
                item.setIdGroup((String) obj2.get("idGroup"));
                item.setFgAdmin((Boolean) obj2.get("fgAdmin"));

                list.add(item);

            }

            mongoClient.close();

        }
        catch (Exception e) {

            throw new DAOException("Houve um erro ao efetuar uma listagem de membros do grupo", e);
        }

        return list;
    }





}
