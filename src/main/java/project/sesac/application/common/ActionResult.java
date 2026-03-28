package project.sesac.application.common;

public record ActionResult(boolean succeeded, String message) {

    public static ActionResult success() {
        return new ActionResult(true, null);
    }

    public static ActionResult failure(String message) {
        return new ActionResult(false, message);
    }
}
