package cloudledger.requisition_system.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint  implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException,  java.io.IOException {

        ErrorResponse errorResponse = populateErrorResponse(authException.getMessage(),
                "Expired or invalid JWT token",HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, errorResponse);
        responseStream.flush();
    }
    public ErrorResponse populateErrorResponse(String message, String developerMessage,HttpStatus status,int code) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDeveloperMsg(developerMessage);
        errorResponse.setErrorMsg(message);
        errorResponse.setResponseCode(code);
        errorResponse.setResponseStatus(status);
        return errorResponse;
    }
}
