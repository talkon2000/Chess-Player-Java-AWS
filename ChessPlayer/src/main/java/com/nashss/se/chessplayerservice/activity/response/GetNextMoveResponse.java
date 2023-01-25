package com.nashss.se.chessplayerservice.activity.response;

import java.util.List;

public class GetNextMoveResponse {
    private final List<String> validMoves;
    private final String bestMove;
    private final String winner;

    private GetNextMoveResponse(List<String> validMoves, String bestMove, String winner) {
        this.validMoves = validMoves;
        this.bestMove = bestMove;
        this.winner = winner;
    }

    public List<String> getValidMoves() {
        return List.copyOf(validMoves);
    }

    public String getBestMove() {
        return bestMove;
    }

    public String getWinner() {
        return winner;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> validMoves;
        private String bestMove;
        private String winner;

        public Builder withValidMoves(final List<String> validMoves) {
            this.validMoves = List.copyOf(validMoves);
            return this;
        }

        public Builder withMove(final String move) {
            this.bestMove = move;
            return this;
        }

        public Builder withWinner(final String winner) {
            this.winner = winner;
            return this;
        }

        public GetNextMoveResponse build() {
            return new GetNextMoveResponse(validMoves, bestMove, winner);
        }
    }
}
