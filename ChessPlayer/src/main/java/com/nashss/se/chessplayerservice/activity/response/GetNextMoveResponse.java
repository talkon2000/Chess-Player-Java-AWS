package com.nashss.se.chessplayerservice.activity.response;

import java.util.List;

public class GetNextMoveResponse {
    private final List<String> validMoves;
    private final String move;

    private GetNextMoveResponse(List<String> validMoves, String move) {
        this.validMoves = validMoves;
        this.move = move;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> validMoves;
        private String move;

        public Builder withValidMoves(List<String> validMoves) {
            this.validMoves = validMoves;
            return this;
        }

        public Builder withMove(String move) {
            this.move = move;
            return this;
        }

        public GetNextMoveResponse build() {
            return new GetNextMoveResponse(validMoves, move);
        }
    }
}
