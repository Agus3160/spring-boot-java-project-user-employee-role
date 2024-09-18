package project.main.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalApiExceptionHandler {

    private String extractDetailMessage(Throwable ex) {
        Throwable cause = ex.getCause();

        // Si la causa es una excepción de Hibernate, que envuelve una SQLException
        if (cause instanceof ConstraintViolationException constraintViolationException) {
            String constraintName = constraintViolationException.getConstraintName();
            return "Constraint violation: " + constraintName + " - " + constraintViolationException.getMessage();
        }

        // Si la causa es directamente una SQLException (caso de DataIntegrityViolationException)
        if (cause instanceof SQLException) {
            SQLException sqlException = (SQLException) cause;
            return "SQL Error: " + sqlException.getMessage() +
                    " (Error Code: " + sqlException.getErrorCode() +
                    ", SQL State: " + sqlException.getSQLState() + ")";
        }

        // Para cualquier otro caso, retornar el mensaje básico
        return ex.getMessage();
    }

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
    public ResponseEntity<FailedResponseDto> handleNotFound(NoResourceFoundException ex) {

        FailedResponseDto errorResponse = new FailedResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                ,null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<FailedResponseDto> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "Database constraint violation: " + extractDetailMessage(ex);
        FailedResponseDto errorResponse = new FailedResponseDto(
                HttpStatus.CONFLICT.value(),
                errorMessage,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);


    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<FailedResponseDto> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = "Constraint violation: " + extractDetailMessage(ex);
        FailedResponseDto errorResponse = new FailedResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
