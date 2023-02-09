package com.nashss.se.chessplayerservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.chessplayerservice.activity.request.GetAllGamesRequest;
import com.nashss.se.chessplayerservice.activity.response.GetAllGamesResponse;

public class GetAllGamesLambda extends LambdaActivityRunner<GetAllGamesRequest, GetAllGamesResponse>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllGamesRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllGamesRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromUserClaims(claims -> GetAllGamesRequest.builder()
                        .withUsername(claims.get("cognito:username"))
                        .build()),
                (request, serviceComponent) -> serviceComponent.provideGetAllGamesActivity().handleRequest(request)
        );
    }
}
