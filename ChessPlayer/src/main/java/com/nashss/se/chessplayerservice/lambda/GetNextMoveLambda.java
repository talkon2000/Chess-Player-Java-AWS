package com.nashss.se.chessplayerservice.lambda;

import com.nashss.se.chessplayerservice.activity.request.GetNextMoveRequest;
import com.nashss.se.chessplayerservice.activity.response.GetNextMoveResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetNextMoveLambda extends LambdaActivityRunner<GetNextMoveRequest, GetNextMoveResponse>
        implements RequestHandler<LambdaRequest<GetNextMoveRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetNextMoveRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPathAndQuery((path, query) -> GetNextMoveRequest.builder()
                    .withMove(path.get("move"))
                    .withGameId(query.get("gameId"))
                    .build()),
            (request, serviceComponent) -> serviceComponent.provideGetNextMoveActivity().handleRequest(request)
        );
    }
}
