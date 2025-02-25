package com.phasmidsoftware.dsaipg.benchmark;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FibonacciHeapTest {
    public static void main(String[] args) {
        // 设定堆的最大大小
        final int M = 4095;
        final int numInserts = 16000;
        final int numDeletes = 4000;

        // 创建一个基准测试工具实例
        Benchmark_Timer<Integer> benchmark = new Benchmark_Timer<>(
                "Fibonacci Heap Benchmark",
                (x) -> new FibonacciHeap(),
                (heap) -> {
                    Random rand = new Random();
                    for (int i = 0; i < numInserts; i++) {
                        heap.insert(rand.nextInt(100000));
                    }
                    for (int i = 0; i < numDeletes; i++) {
                        heap.removeMin();
                    }
                }
        );

        // 进行基准测试
        double averageTime = benchmark.runFromSupplier(
                (Supplier<Integer>) () -> new FibonacciHeap(),
                M
        );

        System.out.println("Average time: " + averageTime + " ms");
    }
}
