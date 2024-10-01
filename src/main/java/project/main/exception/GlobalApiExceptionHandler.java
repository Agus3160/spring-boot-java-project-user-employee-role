package project.main.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalApiExceptionHandler {

    private String extractDetailMessage(Throwable ex) {
        Throwable cause = ex.getCause();

        // Si la causa es una SQLException con violación de llave foránea
        if (cause instanceof SQLException sqlException) {
            // Verificar el código SQLState para violaciones de llave foránea (23503)
            if ("23503".equals(sqlException.getSQLState())) {
                String detailedMessage = sqlException.getMessage();

                // Procesar el mensaje para extraer nombres de tablas involucradas
                String foreignTable = extractForeignTable(detailedMessage);
                String primaryTable = extractPrimaryTable(detailedMessage);

                return String.format(
                        "No se puede eliminar la entidad de la tabla '%s' porque está referenciada por la tabla '%s'.",
                        primaryTable, foreignTable
                );
            }
            return "Error de base de datos: " + sqlException.getMessage();
        }

        return "Ha ocurrido un error inesperado.";
    }

    private String extractForeignTable(String detailedMessage) {
        // Extraer la tabla foránea del mensaje detallado (puedes ajustar la lógica según el formato del mensaje)
        Pattern pattern = Pattern.compile("«(.+?)»");
        Matcher matcher = pattern.matcher(detailedMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "otra entidad";
    }

    private String extractPrimaryTable(String detailedMessage) {
        // Extraer la tabla primaria del mensaje detallado
        Pattern pattern = Pattern.compile("table «(.+?)»");
        Matcher matcher = pattern.matcher(detailedMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "otra entidad";
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
