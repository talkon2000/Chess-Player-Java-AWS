package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetPrivateUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.Game;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

/**
 * Implementation of the GetPrivateUserActivity for the ChessPlayerClient's GetPrivateUser API.
 *
 * This API allows the user to get private user details based on the auth token passed with the HTTP request.
 */
public class GetPrivateUserActivity {

    private final UserDao dao;

    /**
     * Instantiates a new GetPrivateUserActivity object.
     *
     * @param dao DAO to access the users table.
     */
    @Inject
    public GetPrivateUserActivity(UserDao dao) {
        this.dao = dao;
    }

    /**
     * This method handles the incoming request by getting the {@link User} object from the database.
     * <p>
     * It then returns the user object.
     * <p>
     * If the username is null or does not exist in the database, this should throw an InvalidRequestException.
     *
     * @param request request object containing the username
     * @return GetUserResponse object containing the {@link User}
     */
    public GetUserResponse handleRequest(GetPrivateUserRequest request) {
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username can not be null");
        }

        User user = dao.load(request.getUsername());
        if (user == null) {
            throw new InvalidRequestException("There is no user with that username");
        }
        return GetUserResponse.builder()
                .withUser(user)
                .build();
    }
}
