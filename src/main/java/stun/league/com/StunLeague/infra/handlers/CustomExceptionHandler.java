package stun.league.com.StunLeague.infra.handlers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import stun.league.com.StunLeague.domain.exceptions.ExceptionEntity;
import stun.league.com.StunLeague.domain.exceptions.FailedToSaveUserException;
import stun.league.com.StunLeague.domain.exceptions.FailedToUpdateUserException;
import stun.league.com.StunLeague.domain.exceptions.InvalidCredentailsException;

import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler {

    private final String ERROR_MESSAGE_REQUEST = "Failed request";


    @ExceptionHandler(FailedToSaveUserException.class)
    public ResponseEntity<ExceptionEntity> failedToSaveUser(FailedToSaveUserException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        ExceptionEntity exceptionEntity = new ExceptionEntity(
                new Date(),
                ERROR_MESSAGE_REQUEST,
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                path
        );
        return org.springframework.http.ResponseEntity.status(exceptionEntity.getErrorstatus()).body(exceptionEntity);
    }

    @ExceptionHandler(FailedToUpdateUserException.class)
    public ResponseEntity<ExceptionEntity> failedToUpdateUser(FailedToUpdateUserException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        ExceptionEntity exceptionEntity = new ExceptionEntity(
                new Date(),
                ERROR_MESSAGE_REQUEST,
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                path
        );

        return org.springframework.http.ResponseEntity.status(exceptionEntity.getErrorstatus()).body(exceptionEntity);
    }

        @ExceptionHandler(InvalidCredentailsException.class)
        public ResponseEntity<ExceptionEntity> failedLogin(InvalidCredentailsException e, HttpServletRequest request) {
            String path = request.getRequestURI();
            ExceptionEntity exceptionEntity = new ExceptionEntity(
                    new Date(),
                    ERROR_MESSAGE_REQUEST,
                    e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value(),
                    path
            );
        return org.springframework.http.ResponseEntity.status(exceptionEntity.getErrorstatus()).body(exceptionEntity);
    }
}
