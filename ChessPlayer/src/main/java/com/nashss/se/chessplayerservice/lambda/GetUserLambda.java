package com.nashss.se.chessplayerservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.chessplayerservice.activity.request.GetUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;

public class GetUserLambda extends LambdaActivityRunner<GetUserRequest, GetUserResponse>
        implements RequestHandler<AuthenticatedLambdaRequest<GetUserRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetUserRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    GetUserRequest unauthenticatedRequest = input.fromPath(path -> GetUserRequest.builder()
                            .withUserId(path.get("userId"))
                            .build());
                    try {
                        return input.fromUserClaims(claims -> GetUserRequest.builder()
                                .withRequesterId(claims.get("userId"))
                                .withUserId(unauthenticatedRequest.getUserId())
                                .build());
                    } catch (Exception ignored) {
                        return unauthenticatedRequest;
                    }
                },
                (request, serviceComponent) -> serviceComponent.provideGetUserActivity().handleRequest(request)
        );
    }
}
