package com.nashss.se.chessplayerservice.dependency;

import com.nashss.se.chessplayerservice.activity.GetNextMoveActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface ServiceComponent {

    GetNextMoveActivity provideGetNextMoveActivity();
}
