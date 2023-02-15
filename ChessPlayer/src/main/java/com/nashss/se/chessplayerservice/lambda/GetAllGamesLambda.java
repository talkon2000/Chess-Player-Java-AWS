package com.nashss.se.chessplayerservice.lambda;

import com.nashss.se.chessplayerservice.activity.request.GetAllGamesRequest;
import com.nashss.se.chessplayerservice.activity.response.GetAllGamesResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetAllGamesLambda extends LambdaActivityRunner<GetAllGamesRequest, GetAllGamesResponse>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllGamesRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllGamesRequest> input, Context context) {
        return super.runActivity(
            () -> {
                GetAllGamesRequest request = input.fromQuery(query -> GetAllGamesRequest.builder()
                        .withHiddenGames(Boolean.parseBoolean(query.get("returnHiddenGames")))
                        .build());
                return input.fromUserClaims(claims -> GetAllGamesRequest.builder()
                        .withUsername(claims.get("cognito:username"))
                        .withHiddenGames(request.getReturnHiddenGames())
                        .build());
            },
            (request, serviceComponent) -> serviceComponent.provideGetAllGamesActivity().handleRequest(request)
        );
    }
}
