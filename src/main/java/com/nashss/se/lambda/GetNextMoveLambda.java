package com.nashss.se.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.nashss.se.activity.request.GetNextMoveRequest;
import com.nashss.se.activity.response.GetNextMoveResponse;

public class GetNextMoveLambda extends LambdaActivityRunner<GetNextMoveRequest, GetNextMoveResponse>
        implements RequestHandler<LambdaRequest<GetNextMoveRequest>, LambdaResponse> {


    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetNextMoveRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path -> GetNextMoveRequest.builder()
                        .withMove(path.get("move"))
                        .withGameId(path.get("gameId"))
                        .build()),
                (request, serviceComponent) -> serviceComponent.provideGetNextMoveActivity().handleRequest(request)
        );
    }
}
