package com.pginfo.datacollect.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class UserDao {
    private static MongoTemplate mongoTemplate;
    private static String PREFIX = "UserAuthentic";

    private static Map<String, Map<String, String>> data = new HashMap<>();
    private static Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    public UserDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<User> getData(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        List<User> users = mongoTemplate.find(query, User.class, PREFIX);
        return  users;
    }

    public void updateUser(User user){
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update();

        if(!StringUtils.isEmpty(user.getName())){
            update.set("name", user.getName());
        }

        if(!StringUtils.isEmpty(user.getPassword())){
            update.set("password", user.getPassword());
        }

        if(!StringUtils.isEmpty(user.getRole())){
            update.set("role", user.getRole());
        }

        mongoTemplate.upsert(query, update, User.class, PREFIX);
    }

    public void deleteUser(String userid){
        Query query = new Query(Criteria.where("id").is(userid));
        mongoTemplate.remove(query,User.class,PREFIX);
    }

    public void addUser(User user){
        mongoTemplate.insert(user, PREFIX);
    }

    public List<User> queryAllUser() {
        return mongoTemplate.findAll(User.class,PREFIX);
    }

    public List<User> queryByFilter(String id, String role) {
        Query query = new Query();
        if(!StringUtils.isEmpty(id)){
            query.addCriteria(Criteria.where("id").regex(id));
        }
        if(!StringUtils.isEmpty(role)){
            query.addCriteria(Criteria.where("role").is(role));
        }
        return mongoTemplate.find(query, User.class, PREFIX);
    }
}