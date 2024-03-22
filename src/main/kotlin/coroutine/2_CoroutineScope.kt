package org.laughhallo.coroutine

import kotlinx.coroutines.*

fun main() {
//    demo1()
    demo2()
}

@OptIn(DelicateCoroutinesApi::class)
private fun demo1() {
    println("============= demo1 ==============")

    val globalScope = GlobalScope  // GlobalScope 是一个单例

    GlobalScope.launch {
        delay(3000)
        println("Hello, World!")
    }

//    globalScope.cancel()   // 取消 这里会报错，因为 GlobalScope 的生命周期是整个应用程序的生命周期
    while (true);
}

private fun demo2() {
    println("============= demo2 ==============")

    val coroutineScope = CoroutineScope(Dispatchers.Default)

    coroutineScope.launch {
        println("launch 1: context=${coroutineContext}")  // StandaloneCoroutine{Cancelling}@4bd4ba08, Dispatchers.Default
        println("launch 1: thread=${Thread.currentThread().name}")  // DefaultDispatcher-worker-1
        delay(3000)
        println("Hello, World 1!")
    }

    // 注意到，launch2 的 context 和 thread 与 launch1 不同
    coroutineScope.launch {
        println("launch 2: context=${coroutineContext}")  // StandaloneCoroutine{Cancelling}@13196c4, Dispatchers.Default
        println("launch 2: thread=${Thread.currentThread().name}")  // DefaultDispatcher-worker-2
//        delay(3000)
        println("Hello, World 2!")
    }

    coroutineScope.cancel()  // 取消 coroutineScope 中的所有协程
    while (true);
}