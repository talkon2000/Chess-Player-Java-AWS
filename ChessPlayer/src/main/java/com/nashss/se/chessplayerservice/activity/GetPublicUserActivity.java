package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.GetPublicUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

/**
 * Implementation of the GetPublicUserActivity for the ChessPlayerClient's GetPublicUser API.
 *
 * This API allows the user to access public user information based on a username.
 */
public class GetPublicUserActivity {

    private final UserDao dao;

    /**
     * Instantiates a new GetPublicUserActivity object.
     *
     * @param dao DAO to access the users table.
     */
    @Inject
    public GetPublicUserActivity(UserDao dao) {
        this.dao = dao;
    }

    /**
     * This method handles the incoming request by getting the {@link User} object from the database.
     * This differs from the {@link GetPrivateUserActivity} in that it does not return the user's email.
     * <p>
     * It then returns the user object.
     * <p>
     * If the username is null or does not exist in the database, this should throw an InvalidRequestException.
     *
     * @param request request object containing the username
     * @return GetUserResponse object containing the {@link User}
     */
    public GetUserResponse handleRequest(GetPublicUserRequest request) {
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username can not be null");
        }

        User user = dao.loadPublicUser(request.getUsername());
        if (user == null) {
            throw new InvalidRequestException("There is no user with that username");
        }
        return GetUserResponse.builder()
                .withUser(user)
                .build();
    }
}
