package com.mycompany.PythonFlaskAPI;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

// Manages a pool of worker threads and a shared task queue.
public class APIQueue {

    // Shared queue for tasks submitted to the pool
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    // List of worker threads that will continuously execute tasks from the queue
    private final ArrayList<APIWorker> workers = new ArrayList<>();

    // Creates a specified number of worker threads and starts them.
    public APIQueue(int numberOfThreads) {
        for (int i = 0; i < numberOfThreads; i++) {
            // Create a new worker with access to the shared task queue
            APIWorker worker = new APIWorker(taskQueue);
            worker.start();
            workers.add(worker);
        }
    }

    // For Procedures
    public void submitTask(Runnable task) {
        try {
            // Add the task to the queue
            taskQueue.put(task);
        } 
        catch (Exception ex) {
            // Interrupt the current thread if an exception occurs
            Thread.currentThread().interrupt();
            ex.printStackTrace();
        }
    }

    // For Functions
    public <V> Future<V> submitTask(Callable<V> task) {
        FutureTask<V> futureTask = new FutureTask<>(task);
        try {
            taskQueue.put(futureTask);
        } 
        
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        return futureTask;
    }

    // Shuts down all worker threads
    public void shutdown() {
        for (APIWorker worker : workers) {
            worker.shutdown();
        }
    }
}