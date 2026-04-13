package cloudledger.requisition_system.exceptions;


import cloudledger.requisition_system.utils.ErrorMessage;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(RunTimeExceptionPlaceHolder.class)
  public ResponseEntity<ErrorResponse> handleCustomException(RunTimeExceptionPlaceHolder ex) {

    ErrorResponse errorResponse = populateErrorResponse(ex.getMessage(), "internal server error",HttpStatus.INTERNAL_SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR.value());
    log.error("Something went wrong, Exception : " + ex.getMessage());
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(InvalidFormatException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(InvalidFormatException ex) {

    ErrorResponse errorResponse = populateErrorResponse(ex.getMessage(), "invalid format exception",HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
    log.error("Something went wrong, Exception : " + ex.getMessage());
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleCustomException(Exception ex) {
    ErrorResponse errorResponse = populateErrorResponse(ex.getMessage(), "something went wrong",HttpStatus.INTERNAL_SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR.value());
    log.error("Something went wrong, Exception : " + ex.getMessage());
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

  }





  @ExceptionHandler(UnauthorisedException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(UnauthorisedException ex) {

    ErrorResponse errorResponse = populateErrorResponse(ex.getMessage(), ErrorMessage.TokenInvalid.DEVELOPER_MESSAGE,HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value());
    log.error("Invalid token , Exception : " + ex.getMessage());
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);

  }



  @ExceptionHandler(AccountAlreadyExistException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(AccountAlreadyExistException ex) {

    ErrorResponse errorResponse = populateErrorResponse(ex.getMessage(), "This member have an account",HttpStatus.FORBIDDEN,HttpStatus.FORBIDDEN.value());
    log.error("Account exist, exception : " + ex.getMessage());
    ex.printStackTrace();
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);

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
