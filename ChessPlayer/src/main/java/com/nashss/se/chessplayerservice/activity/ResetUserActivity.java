package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.ResetUserRequest;
import com.nashss.se.chessplayerservice.activity.response.ResetUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;

import javax.inject.Inject;

/**
 * Implementation of the ResetUserActivity for the ChessPlayerClient's ResetUser API.
 *
 * This API allows the user to delete a user entry from the user table.
 */
public class ResetUserActivity {

    private final UserDao userDao;

    /**
     * Instantiates a new ResetUserActivity object.
     *
     * @param userDao DAO to access the users table.
     */
    @Inject
    public ResetUserActivity(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * This method handles the incoming request by deleting the {@link User} object from the database.
     * <p>
     * It then returns the user object.
     * <p>
     * If the username is null or does not exist in the database, this should throw an InvalidRequestException.
     *
     * @param request request object containing the username
     * @return ResetUserResponse object containing the {@link User}
     */
    public ResetUserResponse handleRequest(ResetUserRequest request) {
        if (request.getUsername() == null) {
            throw new InvalidRequestException("Username cannot be null");
        }

        User user = userDao.load(request.getUsername());
        if (user == null) {
            throw new InvalidRequestException("That user does not exist");
        }
        userDao.deleteUser(user);
        return ResetUserResponse.builder()
                .withUser(user)
                .build();
    }
}
