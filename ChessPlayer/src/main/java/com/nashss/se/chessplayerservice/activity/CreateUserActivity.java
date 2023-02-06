package com.nashss.se.chessplayerservice.activity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.nashss.se.chessplayerservice.activity.request.CreateUserRequest;
import com.nashss.se.chessplayerservice.activity.response.CreateUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.utils.RegexHelper;

import javax.inject.Inject;

public class CreateUserActivity {

    private final UserDao dao;

    @Inject
    public CreateUserActivity(UserDao dao) {
        this.dao = dao;
    }

    public CreateUserResponse handleRequest(CreateUserRequest request) {
        if (request.getUserId() == null) {
            throw new InvalidRequestException("User ID can not be null");
        }
        if (dao.load(request.getUserId()) != null) {
            throw new InvalidRequestException("A user with that ID already exists");
        }
        if (request.getEmail() == null) {
            throw new InvalidRequestException("Email can not be null");
        }
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username can not be null");
        }

        if (!RegexHelper.isValidEmail(request.getEmail())) {
            throw new InvalidRequestException("Email is not valid");
        }

        User user = new User();
        user.setUserId(request.getUserId());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setActive("true");
        user.setRating(900);
        dao.createUser(user);
        return CreateUserResponse.builder().withUser(user).build();
    }
}
