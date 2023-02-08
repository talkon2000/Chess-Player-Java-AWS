package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.CreateUserRequest;
import com.nashss.se.chessplayerservice.activity.response.CreateUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.utils.ChessUtils;

import javax.inject.Inject;

public class CreateUserActivity {

    private final UserDao dao;

    @Inject
    public CreateUserActivity(UserDao dao) {
        this.dao = dao;
    }

    public CreateUserResponse handleRequest(CreateUserRequest request) {
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username can not be null");
        }
        if (dao.load(request.getUsername()) != null) {
            throw new InvalidRequestException("A user with that username already exists");
        }
        if (request.getEmail() == null) {
            throw new InvalidRequestException("Email can not be null");
        }

        if (!ChessUtils.isValidEmail(request.getEmail())) {
            throw new InvalidRequestException("Email is not valid");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setActive("true");
        user.setRating(900);
        dao.saveUser(user);
        return CreateUserResponse.builder().withUser(user).build();
    }
}
