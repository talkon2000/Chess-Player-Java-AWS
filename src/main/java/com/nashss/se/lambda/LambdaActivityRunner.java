package com.nashss.se.lambda;

import com.nashss.se.dependency.DaggerServiceComponent;
import com.nashss.se.dependency.ServiceComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LambdaActivityRunner<TRequest, TResult> {
    private ServiceComponent service;
    private final Logger log = LogManager.getLogger();

    /**
     * Handles running the activity and returning a LambdaResponse (either success or failure).
     * @param requestSupplier Provides the activity request.
     * @param handleRequest Runs the activity and provides a response.
     * @return A LambdaResponse
     */
    protected LambdaResponse runActivity(
            Supplier<TRequest> requestSupplier,
            BiFunction<TRequest, ServiceComponent, TResult> handleRequest) {
        log.info("runActivity");
        try {
            TRequest request = requestSupplier.get();
            ServiceComponent serviceComponent = getService();
            TResult result = handleRequest.apply(request, serviceComponent);
            return LambdaResponse.success(result);
        } catch (Exception e) {
            return LambdaResponse.error(e);
        }
    }

    private ServiceComponent getService() {
        log.info("getService");
        if (service == null) {
            service = DaggerServiceComponent.create();
        }
        return service;
    }
}
