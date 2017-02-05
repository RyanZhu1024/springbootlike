package cs682.concurrent;


import java.util.HashMap;
import java.util.Map;

/**
 * A read/write lock that allows multiple readers, disallows multiple writers, and allows a writer to
 * acquire a read lock while holding the write lock.
 */
public class ReentrantLock {

    private Map<Long, Integer> readLockMap;
    private volatile Long writeLockId;
    private volatile Integer writeLockCount;
    private final Integer readLockMaxNumber = Integer.MAX_VALUE;

    /**
     * Construct a new ReentrantLock.
     */
    public ReentrantLock() {
        readLockMap = new HashMap<>();
        writeLockId = -1L;
        writeLockCount = 0;
    }

    private boolean hasReadLock() {
        Integer readLockNum = readLockMap.get(Thread.currentThread().getId());
        if (readLockNum == null || readLockNum == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns true if the invoking thread holds a read lock.
     *
     * @return
     */
    public synchronized boolean hasRead() {
        return hasReadLock();
    }

    /**
     * Returns true if the invoking thread holds a write lock.
     *
     * @return
     */
    public synchronized boolean hasWrite() {
        return writeLockId.equals(Thread.currentThread().getId());
    }

    /**
     * Non-blocking method that attempts to acquire the read lock.
     * Returns true if successful.
     *
     * @return
     */
    public synchronized boolean tryLockRead() {
        // when write lock is taken by a thread rather than the current thread
        if ((writeLockCount > 0 && !hasWrite()) || readLockMap.size() == readLockMaxNumber) {
            return false;
        } else {
            Integer lockCount = readLockMap.get(Thread.currentThread().getId()) == null ? 0 : readLockMap.get(Thread.currentThread().getId());
            readLockMap.put(Thread.currentThread().getId(), ++lockCount);
            return true;
        }
    }


    /**
     * Non-blocking method that attempts to acquire the write lock.
     * Returns true if successful.
     *
     * @return
     */
    public synchronized boolean tryLockWrite() {
        if (hasWrite()) {
            ++writeLockCount;
            return true;
        } else if (readLockMap.size() > 0 || writeLockCount > 0) {
            return false;
        } else {
            writeLockId = Thread.currentThread().getId();
            ++writeLockCount;
            return true;
        }
    }

    /**
     * Blocking method that will return only when the read lock has been
     * acquired.
     */
    public synchronized void lockRead() {
        while (!tryLockRead()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Releases the read lock held by the calling thread. Other threads may continue
     * to hold a read lock.
     */
    public synchronized void unlockRead() {
        if (hasReadLock()) {
            Integer lockCount = readLockMap.get(Thread.currentThread().getId());
            --lockCount;
            if (lockCount == 0) {
                readLockMap.remove(Thread.currentThread().getId());
                if (readLockMap.size() == 0) {
                    notifyAll();
                }
            } else {
                readLockMap.put(Thread.currentThread().getId(), --lockCount);
            }
        }
    }

    /**
     * Blocking method that will return only when the write lock has been
     * acquired.
     */
    public synchronized void lockWrite() {
        while (!tryLockWrite()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Releases the write lock held by the calling thread. The calling thread may continue to hold
     * a read lock.
     */

    public synchronized void unlockWrite() {
        if (hasWrite()) {
            --writeLockCount;
            if (writeLockCount == 0) {
                writeLockId = -1L;
                notifyAll();
            }
        }
    }

}
