package com.nashss.se.chessplayerservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.chessplayerservice.activity.request.GetGameRequest;
import com.nashss.se.chessplayerservice.activity.response.GetGameResponse;

public class GetGameLambda extends LambdaActivityRunner<GetGameRequest, GetGameResponse>
        implements RequestHandler<LambdaRequest<GetGameRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetGameRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path -> GetGameRequest.builder()
                        .withGameId(path.get("gameId"))
                        .build()),
                (request, serviceComponent) -> serviceComponent.provideGetGameActivity().handleRequest(request)
        );
    }
}
