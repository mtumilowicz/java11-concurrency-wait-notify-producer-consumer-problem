import java.util.concurrent.TimeUnit;

/**
 * Created by mtumilowicz on 2019-02-16.
 */
class Delay {
    static void millis(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            // not used
        }
    }
}
