package project.sesac.application.auth;

import org.springframework.http.HttpStatus;

public record AuthResult(boolean success, HttpStatus status, String message, Long memberId) {

    public static AuthResult success(HttpStatus status, String message, Long memberId) {
        return new AuthResult(true, status, message, memberId);
    }

    public static AuthResult failure(HttpStatus status, String message) {
        return new AuthResult(false, status, message, null);
    }
}
