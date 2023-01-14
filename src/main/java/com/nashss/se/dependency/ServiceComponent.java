package com.nashss.se.dependency;

import com.nashss.se.activity.GetNextMoveActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface ServiceComponent {

    GetNextMoveActivity provideGetNextMoveActivity();
}
