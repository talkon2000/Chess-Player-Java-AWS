package com.nashss.se.chessplayerservice.dependency;

import com.nashss.se.chessplayerservice.engine.Stockfish;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class StockfishModule {

    /**
     * Provides the stockfish engine.
     * @return Stockfish
     */
    @Singleton
    @Provides
    Stockfish provideStockfish() {
        return new Stockfish();
    }
}
