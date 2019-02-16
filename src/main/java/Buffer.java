import java.util.*;

/**
 * Created by mtumilowicz on 2019-02-15.
 */
class Buffer {
    private final Deque<String> words = new ArrayDeque<>();

    synchronized void produce(String word) {
        while (!words.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // handle
            }
        }
        this.words.add(word);
        this.notify();
        System.out.println("Produced: " + word);
    }

    synchronized String consume() {
        while (words.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.notify();

        var word = words.poll();

        System.out.println("Consumed: " + word);
        return word;
    }


}
