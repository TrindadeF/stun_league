package stun.league.com.StunLeague.domain.exceptions;

public class QueueFullException extends RuntimeException {
    public QueueFullException(String string) {
        super(string);
    }
}
