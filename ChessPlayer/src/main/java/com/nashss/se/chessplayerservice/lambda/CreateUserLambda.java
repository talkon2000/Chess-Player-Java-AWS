package com.nashss.se.chessplayerservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.chessplayerservice.activity.request.CreateUserRequest;
import com.nashss.se.chessplayerservice.activity.response.CreateUserResponse;

public class CreateUserLambda extends LambdaActivityRunner<CreateUserRequest, CreateUserResponse>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateUserRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateUserRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromUserClaims(claims -> CreateUserRequest.builder()
                        .withEmail(claims.get("email"))
                        .withUsername(claims.get("cognito:username"))
                        .build()),
                (request, serviceComponent) -> serviceComponent.provideCreateUserActivity().handleRequest(request)
        );
    }
}
