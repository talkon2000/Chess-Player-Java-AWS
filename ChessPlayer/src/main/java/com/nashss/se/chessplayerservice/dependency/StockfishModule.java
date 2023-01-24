package com.nashss.se.chessplayerservice.dependency;

import com.nashss.se.chessplayerservice.engine.Stockfish;
import dagger.Provides;
import dagger.Module;

import javax.inject.Singleton;

@Module
public class StockfishModule {

    @Singleton
    @Provides
    Stockfish provideStockfish() {
        return new Stockfish();
    }
}
