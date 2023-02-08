package com.nashss.se.chessplayerservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.chessplayerservice.activity.request.CreateNewGameRequest;
import com.nashss.se.chessplayerservice.activity.response.CreateNewGameResponse;

public class CreateNewGameLambda extends LambdaActivityRunner<CreateNewGameRequest, CreateNewGameResponse>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateNewGameRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateNewGameRequest> input, Context context) {
        CreateNewGameRequest unauthorizedRequest = input.fromBody(CreateNewGameRequest.class);
        return super.runActivity(
                () -> input.fromUserClaims(claims -> CreateNewGameRequest.builder()
                        .withAuthPlayerUsername(claims.get("cognito:username"))
                        .withOtherPlayerUsername(unauthorizedRequest.getOtherPlayerUsername())
                        .withAuthUserWhite(unauthorizedRequest.getAuthUserWhite())
                        .withBotDifficulty(unauthorizedRequest.getBotDifficulty())
                        .build()),
                (request, serviceComponent) -> serviceComponent.provideCreateNewGameActivity().handleRequest(request)
        );
    }
}
