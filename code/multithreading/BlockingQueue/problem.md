# Bounded Blocking Queue (Producer-Consumer)

Implement a thread-safe bounded queue with a fixed capacity, supporting:

enqueue(x) — adds an element; if the queue is full, the calling thread blocks until space is available
dequeue() — removes and returns the front element; if empty, blocks until an element is available
size() — returns the current count

Multiple producer threads and consumer threads call these concurrently. It must be correct under contention — no lost items, no reading from an empty queue, no writing to a full one.
Think through before coding:

What's the shared state that needs protection?
What are the two distinct conditions threads wait on? (full vs empty)
Why do you need two separate condition variables (or careful notifyAll) rather than one?