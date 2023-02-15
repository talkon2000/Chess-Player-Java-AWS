package com.nashss.se.chessplayerservice.dependency;

import com.nashss.se.chessplayerservice.activity.CreateNewGameActivity;
import com.nashss.se.chessplayerservice.activity.CreateUserActivity;
import com.nashss.se.chessplayerservice.activity.GetAllGamesActivity;
import com.nashss.se.chessplayerservice.activity.GetGameActivity;
import com.nashss.se.chessplayerservice.activity.GetNextMoveActivity;
import com.nashss.se.chessplayerservice.activity.GetPrivateUserActivity;
import com.nashss.se.chessplayerservice.activity.GetPublicUserActivity;
import com.nashss.se.chessplayerservice.activity.HideGamesActivity;
import com.nashss.se.chessplayerservice.activity.ResetUserActivity;
import com.nashss.se.chessplayerservice.activity.ResignActivity;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Dagger component for providing dependency injection in the Chess Player Service.
 */
@Singleton
@Component(modules = {DaoModule.class, StockfishModule.class})
public interface ServiceComponent {

    /**
     * Provides the relevant activity.
     * @return GetNextMoveActivity
     */
    GetNextMoveActivity provideGetNextMoveActivity();

    /**
     * Provides the relevant activity.
     * @return GetGameActivity
     */
    GetGameActivity provideGetGameActivity();

    /**
     * Provides the relevant activity.
     * @return GetPublicUserActivity
     */
    GetPublicUserActivity provideGetPublicUserActivity();

    /**
     * Provides the relevant activity.
     * @return GetPrivateUserActivity
     */
    GetPrivateUserActivity provideGetPrivateUserActivity();

    /**
     * Provides the relevant activity.
     * @return CreateUserActivity
     */
    CreateUserActivity provideCreateUserActivity();

    /**
     * Provides the relevant activity.
     * @return CreateNewGameActivity
     */
    CreateNewGameActivity provideCreateNewGameActivity();

    /**
     * Provides the relevant activity.
     * @return GetAllGamesActivity
     */
    GetAllGamesActivity provideGetAllGamesActivity();

    /**
     * Provides the relevant activity.
     * @return ResignActivity
     */
    ResignActivity provideResignActivity();

    /**
     * Provides the relevant activity.
     * @return ResetUserActivity
     */
    ResetUserActivity provideResetUserActivity();

    /**
     * Provides the relevant activity.
     * @return HideGamesActivity
     */
    HideGamesActivity provideHideGamesActivity();
}
