# java11-concurrency-wait-notify-producer-consumer-problem

_Reference_: https://www.amazon.com/Java-Language-Features-Modules-Expressions/dp/1484233476

The thread must obtain the monitor lock of the object on which the method/block is
synchronized.
• The thread’s working copy of the shared variables is updated with the values of those
variables in the main memory just after the thread gets the lock. The values of the
shared variables in the main memory are updated with the thread’s working copy
value just before the thread releases the lock. That is, at the start and at the end of a
synchronized method/block, the values of the shared variables in thread’s working
memory and the main memory are synchronized.