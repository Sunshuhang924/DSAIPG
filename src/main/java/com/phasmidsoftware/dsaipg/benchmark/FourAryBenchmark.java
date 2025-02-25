package com.phasmidsoftware.dsaipg.benchmark;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;
import java.util.Random;

public class FourAryBenchmark {
    private static final int INSERTIONS = 16000;
    private static final int DELETIONS = 4000;
    private static final int MAX_SIZE = 4095;

    public static void main(String[] args) {
        benchmarkFourAry(false); // 4-ary 堆
        benchmarkFourAry(true);  // 4-ary 堆 + Floyd's trick
    }

    private static void benchmarkFourAry(boolean useFloydsTrick) {
        String description = useFloydsTrick ? "4-ary Heap with Floyd's trick" : "Basic 4-ary Heap";
        Benchmark_Timer<FourAry> benchmark = new Benchmark_Timer<>(
                description,
                FourAryBenchmark::prepareQueue,
                FourAryBenchmark::runBenchmark
        );

        double time = benchmark.runFromSupplier(() -> new FourAry(useFloydsTrick), 10);
        System.out.printf("%s: %.5f ms%n", description, time);
    }

    private static FourAry prepareQueue(FourAry heap) {
        heap.clear();
        return heap;
    }

    private static void runBenchmark(FourAry heap) {
        Random random = new Random();
        Integer highestSpilled = null;

        for (int i = 0; i < INSERTIONS; i++) {
            int value = random.nextInt();
            if (heap.size() < MAX_SIZE) {
                heap.offer(value);
            } else {
                int min = heap.peek();
                if (value > min) {
                    heap.poll();
                    heap.offer(value);
                    highestSpilled = (highestSpilled == null || min > highestSpilled) ? min : highestSpilled;
                } else {
                    highestSpilled = (highestSpilled == null || value > highestSpilled) ? value : highestSpilled;
                }
            }
        }

        for (int i = 0; i < DELETIONS; i++) {
            heap.poll();
        }

        System.out.println("Highest spilled element: " + highestSpilled);
    }
}
