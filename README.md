# java11-concurrency-wait-notify-producer-consumer-problem

# preface
## producer consumer
`Buffer` should be synchronized in such way that `Producer` produces data only when the `Buffer` does not exceed 
its capacity and the `Consumer` consumes the buffer's data only when `Buffer` is not empty.

## synchronized
At the start and at the end of a synchronized method/block, the values of the shared variables in thread’s working
memory and the main memory are synchronized.
    * **The thread’s working copy of the shared variables is updated with the values of those
    variables in the main memory just after the thread gets the lock**. 
    * **The values of the shared variables in the main memory are updated with the thread’s working copy
    value just before the thread releases the lock.** 

# project description
We will provide simple solution to producer - consumer problem.
* buffer
    ```
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
    ```