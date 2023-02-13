package com.nashss.se.chessplayerservice.lambda;

import com.nashss.se.chessplayerservice.activity.request.ResignRequest;
import com.nashss.se.chessplayerservice.activity.response.ResignResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ResignLambda extends LambdaActivityRunner<ResignRequest, ResignResponse>
        implements RequestHandler<AuthenticatedLambdaRequest<ResignRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<ResignRequest> input, Context context) {
        ResignRequest unauthenticatedRequest =
                input.fromPath(path -> ResignRequest.builder().withGameId(path.get("gameId")).build());
        return super.runActivity(
            () -> input.fromUserClaims(claims -> ResignRequest.builder()
                    .withUsername(claims.get("cognito:username"))
                    .withGameId(unauthenticatedRequest.getGameId())
                    .build()),
            (request, serviceComponent) -> serviceComponent.provideResignActivity().handleRequest(request)
        );
    }
}
