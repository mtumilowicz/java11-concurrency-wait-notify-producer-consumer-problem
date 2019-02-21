[![Build Status](https://travis-ci.com/mtumilowicz/java11-concurrency-wait-notify-producer-consumer-problem.svg?branch=master)](https://travis-ci.com/mtumilowicz/java11-concurrency-wait-notify-producer-consumer-problem)

# java11-concurrency-wait-notify-producer-consumer-problem

# preface
## producer consumer
`Buffer` should be synchronized in such way that `Producer` produces data only when the `Buffer` does not exceed 
its capacity and the `Consumer` consumes the buffer's data only when `Buffer` is not empty.

## synchronized
* At the start and at the end of a synchronized method/block, the values of the shared variables in thread’s working
memory and the main memory are synchronized.
* The thread’s working copy of the shared variables is updated with the values of those
variables in the main memory just after the thread gets the lock. 
* The values of the shared variables in the main memory are updated with the thread’s working copy
value just before the thread releases the lock. 
* The built-in mechanisms for efficiently waiting for a condition to become
  true—wait and notify—are tightly bound to intrinsic locking, and can be difficult
  to use correctly. To create operations that wait for a precondition to become
  true before proceeding, it is often easier to use existing library classes, such as
  blocking queues or semaphores, to provide the desired state-dependent behavior.
  Blocking library classes such as `BlockingQueue`, `Semaphore`, and other synchronizers, 
  take a look at: https://github.com/mtumilowicz/java11-concurrency-blockingqueue-producer-consumer-problem

# project description
We will provide simple solution to producer - consumer problem.
* buffer
    ```
    class Buffer {
        private final Deque<String> words = new ArrayDeque<>();
        private final int capacity = 1;
    
        synchronized void produce(String word) {
            waitWhile(this::isFull, this);
            this.words.add(word);
            this.notify();
        }
    
        synchronized String consume() {
            waitWhile(this::isEmpty, this);
            this.notify();
            return words.poll();
        }
    
        private boolean isFull() {
            return words.size() == capacity;
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
    ```
* tests
    ```
    var buffer = new Buffer();
    
    var produced = startProducer(buffer);
    var consumed = startConsumer(buffer);
    
    Delay.millis(200);
    
    assertThat(ListUtils.longestCommonSubsequence(consumed, produced), is(consumed));
    ```
    where:
    * starting producer
        ```
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
        ```
    * starting consumer
        ```
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
        ```
    * and delay is simply:
        ```
        class Delay {
            static void millis(int millis) {
                try {
                    TimeUnit.MILLISECONDS.sleep(millis);
                } catch (InterruptedException e) {
                    // not used
                }
            }
        }
        ```