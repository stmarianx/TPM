package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final int n = 3;
    private static final int LIMIT = 120000;
    private static final int BATCH_SIZE = 100;
    final private static AtomicInteger sharedCounter = new AtomicInteger(0);
    final private static AtomicInteger[] level = new AtomicInteger[n];
    final private static AtomicInteger[] victim = new AtomicInteger[n];
    final private static AtomicInteger[] accessFrequency = new AtomicInteger[n];
    final private static int[] localCounter = new int[n];

    static {
        for (int i = 0; i < n; i++) {
            level[i] = new AtomicInteger(0);
            victim[i] = new AtomicInteger(-1);
            accessFrequency[i] = new AtomicInteger(0);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[n];
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < n; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                while (sharedCounter.get() < LIMIT) {
                    lock(threadId);

                    for (int j = 0; j < BATCH_SIZE && sharedCounter.get() < LIMIT; j++) {
                        sharedCounter.incrementAndGet();
                        localCounter[threadId]++;
                        accessFrequency[threadId].incrementAndGet();
                    }

                    unlock(threadId);
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Timp total de executie: " + (endTime - startTime) + " ms");
        System.out.println("Valoare finala a contorului partajat: " + sharedCounter.get());
        for (int i = 0; i < n; i++) {
            System.out.println("Thread " + i + " - numar accese: " + localCounter[i]);
        }
    }

    private static void lock(int i) {
        for (int L = 1; L < n; L++) {
            level[i].set(L);
            victim[L].set(i);

            while ((existsThread(i, L) && victim[L].get() == i) || !canAccess(i)) {
                Thread.yield();
            }
        }
    }

    private static void unlock(int i) {
        level[i].set(0);
    }

    private static boolean existsThread(int i, int L) {
        for (int k = 0; k < n; k++) {
            if (k != i && level[k].get() >= L) {
                return true;
            }
        }
        return false;
    }

    private static boolean canAccess(int i) {
        int threadAccessCount = accessFrequency[i].get();
        for (int k = 0; k < n; k++) {
            if (k != i && accessFrequency[k].get() < threadAccessCount) {
                return false;
            }
        }
        return true;
    }
}
