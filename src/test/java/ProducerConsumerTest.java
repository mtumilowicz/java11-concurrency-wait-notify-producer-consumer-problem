import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by mtumilowicz on 2019-02-16.
 */
public class ProducerConsumerTest {

    @Test
    public void producer_consumer() {
        var buffer = new Buffer();

        var produced = startProducer(buffer);
        var consumed = startConsumer(buffer);

        Delay.millis(200);
        
        assertThat(ListUtils.longestCommonSubsequence(consumed, produced), is(consumed));
    }

    private LinkedList<String> startProducer(Buffer buffer) {
        var produced = new LinkedList<String>();
        var producer = new Thread(() -> {
            while (true) {
                Delay.millis(25);
                var word = RandomStringUtils.randomAlphabetic(10);
                buffer.produce(word);
                produced.add(word);
            }
        });

        producer.start();

        return produced;
    }

    private LinkedList<String> startConsumer(Buffer buffer) {
        var consumed = new LinkedList<String>();
        var consumer = new Thread(() -> {
            while (true) {
                Delay.millis(30);
                consumed.add(buffer.consume());
            }
        });

        consumer.start();
        
        return consumed;
    }


}
