package com.nashss.se.chessplayerservice.lambda;

import com.nashss.se.chessplayerservice.activity.request.GetPublicUserRequest;
import com.nashss.se.chessplayerservice.activity.response.GetUserResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPublicUserLambda extends LambdaActivityRunner<GetPublicUserRequest, GetUserResponse>
        implements RequestHandler<LambdaRequest<GetPublicUserRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetPublicUserRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path -> GetPublicUserRequest.builder()
                    .withUsername(path.get("username"))
                    .build()),
            (request, serviceComponent) -> serviceComponent.provideGetPublicUserActivity().handleRequest(request)
        );
    }
}
