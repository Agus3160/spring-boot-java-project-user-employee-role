package project.main.exception;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import project.main.utils.response.FailedResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalApiExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<FailedResponseDto> handleGenericErrors(ApiException ex) {

        FailedResponseDto errorRes = new FailedResponseDto(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                null
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(errorRes);
    }

    // Handle 404 errors (NoHandlerFoundException)
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ModelAndView handleNotFound(NoResourceFoundException ex) {

//        FailedResponseDto errorResponse = new FailedResponseDto(
//                HttpStatus.NOT_FOUND.value(),
//                "Resource not found",
//                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//                ,null
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        return new ModelAndView("error/404");
    }

    // No valid arguments exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailedResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        FailedResponseDto errorResponse = new FailedResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Bad Request",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                ,errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<FailedResponseDto> handleTypeMismatchExceptions(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Type mismatch: " + ex.getName() + " should be of type " + ex.getRequiredType().getSimpleName();
        FailedResponseDto errorResponse = new FailedResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
