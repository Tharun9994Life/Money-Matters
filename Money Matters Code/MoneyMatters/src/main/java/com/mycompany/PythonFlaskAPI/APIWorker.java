// https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html

package com.mycompany.PythonFlaskAPI;

import java.util.concurrent.BlockingQueue;

// Worker Threads that continuously processes tasks from a shared queue. Each worker runs in its own thread and executes tasks until it is shut down.
public class APIWorker extends Thread {

    // Shared queue of tasks to be executed by this worker
    private final BlockingQueue<Runnable> taskQueue;

    // Flag to indicate whether the worker should keep running
    private volatile boolean running = true;

    // Assigns the shared task queue to the worker.
    public APIWorker(BlockingQueue<Runnable> taskQueue) {
        this.taskQueue = taskQueue;
    }

    // Continuously takes tasks from the queue and executes them.
    @Override
    public void run() {
        while (running) {
            try {
                // Take a task from the queue
                Runnable task = taskQueue.take();
                task.run();
            } 

            catch (Exception ex) {
                // Interrupt the thread if an exception occurs
                Thread.currentThread().interrupt();
                ex.printStackTrace();
                break;
            }
        }
    }

    //Shuts down the worker
    public void shutdown() {
        running = false;
        this.interrupt();
    }
}