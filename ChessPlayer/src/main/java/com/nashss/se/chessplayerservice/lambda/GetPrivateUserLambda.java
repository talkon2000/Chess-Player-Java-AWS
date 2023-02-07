package com.nashss.se.chessplayerservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.chessplayerservice.activity.request.GetPrivateUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;

public class GetPrivateUserLambda extends LambdaActivityRunner<GetPrivateUserRequest, GetUserResponse>
        implements RequestHandler<AuthenticatedLambdaRequest<GetPrivateUserRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetPrivateUserRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromUserClaims(claims -> GetPrivateUserRequest.builder()
                                .withUsername(claims.get("cognito:username"))
                                .build()),
                (request, serviceComponent) -> serviceComponent.provideGetPrivateUserActivity().handleRequest(request)
        );
    }
}
