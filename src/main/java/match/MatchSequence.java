package match;

import java.util.concurrent.atomic.AtomicLong;

public class MatchSequence {
    private static final AtomicLong sequence = new AtomicLong(0);

    public static long generateSequenceNumber() {
        return sequence.getAndIncrement();
    }
}
