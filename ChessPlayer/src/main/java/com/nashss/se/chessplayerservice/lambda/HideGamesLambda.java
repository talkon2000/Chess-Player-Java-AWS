package com.nashss.se.chessplayerservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.chessplayerservice.activity.request.HideGamesRequest;
import com.nashss.se.chessplayerservice.activity.response.HideGamesResponse;

public class HideGamesLambda extends LambdaActivityRunner<HideGamesRequest, HideGamesResponse>
        implements RequestHandler<LambdaRequest<HideGamesRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(LambdaRequest<HideGamesRequest> input, Context context) {
        return null;
    }
}
