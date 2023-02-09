package com.nashss.se.chessplayerservice.dependency;

import com.nashss.se.chessplayerservice.activity.*;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DaoModule.class, StockfishModule.class})
public interface ServiceComponent {

    GetNextMoveActivity provideGetNextMoveActivity();

    GetGameActivity provideGetGameActivity();

    GetPublicUserActivity provideGetPublicUserActivity();

    GetPrivateUserActivity provideGetPrivateUserActivity();

    CreateUserActivity provideCreateUserActivity();

    CreateNewGameActivity provideCreateNewGameActivity();

    GetAllGamesActivity provideGetAllGamesActivity();
}
