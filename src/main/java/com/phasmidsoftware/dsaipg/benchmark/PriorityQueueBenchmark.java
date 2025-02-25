package com.phasmidsoftware.dsaipg.benchmark;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;

import java.util.PriorityQueue;
import java.util.Random;

public class PriorityQueueBenchmark {
    private static final int INSERTIONS = 16000;
    private static final int DELETIONS = 4000;
    private static final int MAX_SIZE = 4095;

    public static void main(String[] args) {
        benchmarkPriorityQueue(false); // 基本二叉堆
        benchmarkPriorityQueue(true);  // 使用 Floyd's trick
    }

    private static void benchmarkPriorityQueue(boolean useFloydsTrick) {
        String description = useFloydsTrick ? "PriorityQueue with Floyd's trick" : "Basic PriorityQueue";
        Benchmark_Timer<PriorityQueue<Integer>> benchmark = new Benchmark_Timer<>(
                description,
                PriorityQueueBenchmark::prepareQueue,
                pq -> runBenchmark(pq, useFloydsTrick)
        );

        double time = benchmark.runFromSupplier(() -> new PriorityQueue<>(), 10);
        System.out.printf("%s: %.5f ms%n", description, time);
    }

    private static PriorityQueue<Integer> prepareQueue(PriorityQueue<Integer> pq) {
        pq.clear();
        return pq;
    }

    private static void runBenchmark(PriorityQueue<Integer> pq, boolean useFloydsTrick) {
        Random random = new Random();
        Integer highestSpilled = null;

        for (int i = 0; i < INSERTIONS; i++) {
            int value = random.nextInt();
            if (pq.size() < MAX_SIZE) {
                pq.offer(value);
            } else {
                int min = pq.peek();
                if (value > min) {
                    pq.poll();
                    pq.offer(value);
                    if (highestSpilled == null || min > highestSpilled) {
                        highestSpilled = min;
                    }
                } else {
                    if (highestSpilled == null || value > highestSpilled) {
                        highestSpilled = value;
                    }
                }
            }
        }

        for (int i = 0; i < DELETIONS; i++) {
            pq.poll();
        }

        System.out.println("Highest spilled element: " + highestSpilled);
    }
}

