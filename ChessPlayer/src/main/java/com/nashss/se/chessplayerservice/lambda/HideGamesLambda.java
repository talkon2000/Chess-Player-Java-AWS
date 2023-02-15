package com.nashss.se.chessplayerservice.lambda;

import com.nashss.se.chessplayerservice.activity.request.HideGamesRequest;
import com.nashss.se.chessplayerservice.activity.response.HideGamesResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class HideGamesLambda extends LambdaActivityRunner<HideGamesRequest, HideGamesResponse>
        implements RequestHandler<LambdaRequest<HideGamesRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(LambdaRequest<HideGamesRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromBody(HideGamesRequest.class),
            (request, serviceComponent) -> serviceComponent.provideHideGamesActivity().handleRequest(request)
        );
    }
}
