package com.nashss.se.chessplayerservice.activity;

import com.nashss.se.chessplayerservice.activity.request.CreateUserRequest;
import com.nashss.se.chessplayerservice.activity.response.CreateUserResponse;
import com.nashss.se.chessplayerservice.dynamodb.dao.UserDao;
import com.nashss.se.chessplayerservice.dynamodb.models.User;
import com.nashss.se.chessplayerservice.exceptions.InvalidRequestException;
import com.nashss.se.chessplayerservice.utils.ChessUtils;

import javax.inject.Inject;

/**
 * Implementation of the CreateUserActivity for the ChessPlayerClient's CreateUser API.
 *
 * This API allows the user to create a {@link User} in the database, and returns that user.
 */
public class CreateUserActivity {

    private final UserDao dao;

    /**
     * Instantiates a new CreateUserActivity object.
     *
     * @param dao DAO to access the users table.
     */
    @Inject
    public CreateUserActivity(UserDao dao) {
        this.dao = dao;
    }

    /**
     * This method handles the incoming request by creating a {@link User} object in the database.
     * <p>
     * It then returns the user object.
     * <p>
     * If the username is null or already taken, this should throw an InvalidRequestException.
     * <p>
     * If the email is null or not a valid email, this should throw an InvalidRequestException.
     *
     * @param request request object containing the username and email of the player.
     * @return CreateUserResponse object containing the {@link User}.
     */
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
