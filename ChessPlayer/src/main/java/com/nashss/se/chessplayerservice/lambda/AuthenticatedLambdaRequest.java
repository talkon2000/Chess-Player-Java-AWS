package com.nashss.se.chessplayerservice.lambda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a generic, authenticated "APIGateway" request made to a lambda function.
 * @param <T> The type of the concrete request that should be created from this LambdaRequest
 */
public class AuthenticatedLambdaRequest<T> extends LambdaRequest<T> {

    /**
     * Use the given converter to create an instance of T from the claims included in the request's JWT token.
     * @param converter Contains the conversion code
     * @return An instance of T that contains data from the request's claims.
     */
    public T fromUserClaims(Function<Map<String, String>, T> converter) {
        try {
            return converter.apply(getClaims());
        } catch (Exception e) {
            throw new RuntimeException("Unable to get user information from request.", e);
        }
    }

    private Map<String, String> getClaims() throws JsonProcessingException {
        // If we are running locally using SAM, we have to manually decode claims from the JWT Token.
        return System.getenv().get("AWS_SAM_LOCAL") == null ?
                (Map<String, String>) super.getRequestContext().getAuthorizer().get("claims") :
                getClaimsFromAuthHeader(super.getHeaders().get("Authorization"));
    }

    private Map<String, String> getClaimsFromAuthHeader(final String authorizationHeader)
            throws JsonProcessingException {
        String jwt = getJWTFromAuthHeader(authorizationHeader);
        return getClaimsFromJWT(jwt);
    }

    private String getJWTFromAuthHeader(final String authorizationHeader) {
        return authorizationHeader.split("\\s")[1];
    }

    private Map<String, String> getClaimsFromJWT(final String jwt) throws JsonProcessingException {
        Base64.Decoder decoder = Base64.getDecoder();

        String[] sections = jwt.split("\\.");
        String payload = new String(decoder.decode(sections[1]));

        return MAPPER.readValue(payload, new TypeReference<HashMap<String, String>>() {
        });
    }
}
