package com.nashss.se.chessplayerservice.lambda;

import com.nashss.se.chessplayerservice.activity.request.ResetUserRequest;
import com.nashss.se.chessplayerservice.activity.response.ResetUserResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ResetUserLambda extends LambdaActivityRunner<ResetUserRequest, ResetUserResponse>
        implements RequestHandler<AuthenticatedLambdaRequest<ResetUserRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<ResetUserRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromUserClaims(claims -> ResetUserRequest.builder()
                    .withUsername(claims.get("cognito:username"))
                    .build()),
            (request, serviceComponent) -> serviceComponent.provideResetUserActivity().handleRequest(request)
        );
    }
}
