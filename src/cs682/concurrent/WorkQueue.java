package cs682.concurrent;

import java.util.ArrayDeque;
import java.util.Queue;

public class WorkQueue {

    private volatile boolean open = true;
    private Queue<MyThread> workersPool;
    private Queue<Runnable> runningPool;
    private Integer capacity;
    private final static Integer WAITING_QUEUE_NUMBER = 1024;

    private class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                Runnable work;
                synchronized (runningPool) {
                    while (runningPool.size() == 0 && open) {
                        try {
                            runningPool.wait();
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    if (!open && runningPool.size() == 0) {
                        break;
                    }
                    work = runningPool.poll();
                }
                if (work != null) {
                    work.run();
                }
            }
        }
    }


    /**
     * Construct a WorkQueue with 10 default workersPool.
     */
    public WorkQueue() {
        initQueue(10);
    }


    public WorkQueue(int nThreads) {
        initQueue(nThreads);
    }

    private void initQueue(Integer nThreads) {
        capacity = nThreads;
        workersPool = new ArrayDeque<>(capacity);
        runningPool = new ArrayDeque<>(WAITING_QUEUE_NUMBER);
        for (int i = 0; i < capacity; i++) {
            MyThread thread = new MyThread();
            thread.setName("magic thread No. " + i);
            thread.start();
            workersPool.add(thread);
        }
    }

    /**
     * Execute a new Runnable job.
     * if this method is going to be used in multi threads, then, running pool and waiting pool need to be locked
     *
     * @param r
     */
    public void execute(Runnable r) {
        synchronized (runningPool) {
            if (open && runningPool.size() <= WAITING_QUEUE_NUMBER) {
                runningPool.offer(r);
                runningPool.notifyAll();
            }
        }
    }

    /**
     * Stop accepting new jobs.
     * This method should not block until work is complete.
     */
    public void shutdown() {
        open = false;
        synchronized (runningPool) {
            runningPool.notifyAll();
        }
    }

    /**
     * Block until all jobs in the queue are complete.
     */
    public void awaitTermination() {
        for (MyThread myThread : workersPool) {
            try {
                myThread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
