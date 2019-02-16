import java.util.*;
import java.util.function.Supplier;

/**
 * Created by mtumilowicz on 2019-02-15.
 */
class Buffer {
    private final Deque<String> words = new ArrayDeque<>();

    synchronized void produce(String word) {
        waitWhile(this::isNotFull, this);
        this.words.add(word);
        this.notify();
    }

    synchronized String consume() {
        waitWhile(this::isEmpty, this);
        this.notify();
        return words.poll();
    }

    private boolean isNotFull() {
        return words.size() < 1;
    }
    
    private boolean isEmpty() {
        return words.isEmpty();
    }

    private static void waitWhile(Supplier<Boolean> condition, Object object) {
        while (condition.get()) {
            try {
                object.wait();
            } catch (InterruptedException e) {
                // not used
            }
        }
    }
}
