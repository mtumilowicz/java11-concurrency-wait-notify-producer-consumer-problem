import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import java.util.LinkedList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by mtumilowicz on 2019-02-16.
 */
public class ProducerConsumerTest {

    @Test
    public void producer_consumer() {
        var buffer = new Buffer();
        
        var produced = new LinkedList<String>();
        var producer = new Thread(() -> {
            while (true) {
                Delay.millis(25);
                var word = RandomStringUtils.randomAlphabetic(10);
                buffer.produce(word);
                produced.add(word);
            }
        });

        var consumed = new LinkedList<String>();
        var consumer = new Thread(() -> {
            while (true) {
                Delay.millis(30);
                consumed.add(buffer.consume());
            }
        });

        producer.start();
        consumer.start();
        
        Delay.millis(120);
        
        assertThat(produced, is(consumed));
    }
}
