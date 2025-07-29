package  com.idealizer.review_x.infra.http;

import com.idealizer.review_x.common.exceptions.BadRequestException;
import com.idealizer.review_x.common.exceptions.DuplicatedException;
import com.idealizer.review_x.infra.http.dto.FieldError;
import com.idealizer.review_x.infra.http.dto.ResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("Validation error", e.getMessage());
        List<org.springframework.validation.FieldError> fieldErrors = e.getFieldErrors();
        List<FieldError> errors = fieldErrors
                .stream()
                .map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error", errors);
    }

    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleDuplicatedException(DuplicatedException e) {
        logger.error("Duplicated error", e.getMessage());
        return ResponseError.conflict("Conflict Error");
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleBadRequestException(BadRequestException e) {
        logger.error("Bad request error", e.getMessage());
        return ResponseError
                .badRequest("Bad Request Error");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleBadRequestException(IllegalArgumentException e) {
        logger.error("Illegal argument exception", e.getMessage());
        return ResponseError
                .badRequest("Bad Request Error");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleInternalServerErrorException(RuntimeException e) {
        logger.error("Internal server error", e);
        return new ResponseError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                List.of());
    }

}
