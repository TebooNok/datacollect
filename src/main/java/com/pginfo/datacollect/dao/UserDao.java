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
        Query query = new Query(Criteria.where("id").is(user.getid()));
        Update update = new Update().set("name", user.getname()).set("password", user.getpassword()).set("role", user.getrole());
        mongoTemplate.upsert(query, update, User.class, PREFIX);
    }

    public void deleteUser(String username){
        Query query = new Query(Criteria.where("name").is(username));
        mongoTemplate.remove(query,User.class,PREFIX);
    }

    public void addUser(User user){
        mongoTemplate.insert(User.class,PREFIX);
    }

    public List<User> queryAllUser() {
        return mongoTemplate.findAll(User.class,PREFIX);
    }

    public List<User> queryByFilter(String id, String name) {
        Query query = new Query();
        if(!StringUtils.isEmpty(id)){
            query.addCriteria(Criteria.where("id").is(id));
        }
        if(!StringUtils.isEmpty(name)){
            query.addCriteria(Criteria.where("name").is(name));
        }
        return mongoTemplate.find(query, User.class, PREFIX);
    }
}