package com.poliana.users;

import com.poliana.web.error.ForbiddenException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author David Gilmore
 * @date 3/18/14
 */
public class RESTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private static final String API_KEY_PARAMETER_NAME = "apikey";

    /**
     * @param defaultFilterProcessesUrl the default value for <tt>filterProcessesUrl</tt>.
     */
    protected RESTAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        String apiKeyValue = obtainAPIKeyValue(request);

        RESTAuthenticationToken authRequest = new RESTAuthenticationToken(apiKeyValue, null, null);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private String decodeParameterValue(HttpServletRequest request, String requestParameterName) throws UnsupportedEncodingException {
        //This is basically to avoid the weird RFC spec when it comes to spaces in the URL and how they are encoded
        return getParameterValue(request, requestParameterName);
    }

    private String getParameterValue(HttpServletRequest request, String requestParameterName) {

        Map<String, String[]> params = request.getParameterMap();

        if (params.containsKey(requestParameterName))
            return params.get(requestParameterName)[0];
        else
            throw new ForbiddenException();
    }


    private String obtainAPIKeyValue(HttpServletRequest request) throws UnsupportedEncodingException {

        try {
            return decodeParameterValue(request, API_KEY_PARAMETER_NAME);
        }
        catch (NullPointerException e) {
            throw new ForbiddenException();
        }
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication request's details
     * property.
     *
     * @param request that an authentication request is being created for

     */
    protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    @Override
    /**
     * Because we require the API client to send credentials with every request, we must authenticate on every request
     */
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }
}