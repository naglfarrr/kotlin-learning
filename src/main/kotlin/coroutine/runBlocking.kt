package org.laughhallo.coroutine

import kotlinx.coroutines.*

fun main() {
//    demo1()
//    demo2()
//    demo3()
//    demo4()
    demo5()
}

/**
 * 简单的 runBlocking 示例
 * runBlocking 是一个顶层函数，用于启动一个新的协程并阻塞当前线程，直到协程执行完毕。
 */
private fun demo1() {
    println("============= demo1 ==============")
    val result = runBlocking {
        // 打印上下文
        println("runBlocking: I'm working in context $coroutineContext")  // BlockingCoroutine{Active} BlockingEventLoop
        println("runBlocking: I'm working in thread ${Thread.currentThread().name}")  // thread: main
        delay(2000)
        1
    }
    println("world $result")
}

/**
 * 为 runBlocking 指定上下文
 * runBlocking 可以接受一个 CoroutineContext 参数，用于指定协程的上下文。
 * 通过指定不同的上下文，可以让 runBlocking 在不同的线程池里执行。
 * 但是，runBlocking 本身是阻塞的，因此它会阻塞当前线程，直到协程执行完毕。
 */
private fun demo2() {
    println("============= demo2 ==============")
    // 指定 runBlocking 运行在 IO 线程池里
    val result = runBlocking(Dispatchers.IO) {
        // 打印上下文
        println("runBlocking: I'm working in context $coroutineContext")  // BlockingCoroutine{Active} Dispatchers.IO
        println("runBlocking: I'm working in thread ${Thread.currentThread().name}")  // thread: DefaultDispatcher-worker-1
        delay(2000)
        1
    }
    println("world $result")
}

/**
 * 在 runBlocking 里启动一个新的协程
 */
private fun demo3() {
    println("============= demo3 ==============")
    // 指定 runBlocking 运行在 IO 线程池里
    val result = runBlocking(Dispatchers.IO) {
        // 打印上下文
        println("runBlocking: I'm working in context $coroutineContext")  // BlockingCoroutine{Active} Dispatchers.IO
        println("runBlocking: I'm working in thread ${Thread.currentThread().name}")  // thread: DefaultDispatcher-worker-1

        // 在 runBlocking 里启动一个新的协程。
        // 这个新协程会继承 runBlocking 的上下文，因此也会运行在 IO 线程池里
        // 但是这个新协程的上下文是独立的，不会影响 runBlocking 里的协程
        // 线程也是 Dispatchers.IO 新分配的
        val job = launch {
            delay(3000)
            println("launch: I'm working in context $coroutineContext")  // StandaloneCoroutine{Active} Dispatchers.IO
            println("launch: I'm working in thread ${Thread.currentThread().name}")  // thread: DefaultDispatcher-worker-3
        }
        delay(2000)
        println("second")
        1
    }
    println("world $result")
}

/**
 * 取消一个协程，以及检查协程的状态
 */
private fun demo4() {
    println("============= demo4 ==============")
    val result = runBlocking(Dispatchers.IO) {

        val job = launch {
            delay(3000)
            println("launch: keep working!")  // launch 会被中途取消掉，因此不会打印！
        }

//        delay(4000)  // 注释这句，以观察不同的结果！

        println("job active: ${job.isActive}")  // true
        println(job.cancel())  // 取消它
        println("job active: ${job.isActive}")  // false
        println("second")
        1
    }
    println("world $result")
}

/**
 * 切换线程
 */
private fun demo5() {
    println("============= demo5 ==============")
    val result = runBlocking(Dispatchers.IO) {
        println("runBlocking: I'm working in context $coroutineContext")  // BlockingCoroutine{Active} Dispatchers.IO
        println("runBlocking: I'm working in thread ${Thread.currentThread().name}") // thread: DefaultDispatcher-worker-1
        request {
            // 切换前，使用的还是 runBlocking 的上下文
            println("request: I'm working in context $coroutineContext")  // BlockingCoroutine{Active} Dispatchers.IO
            println("request: I'm working in thread ${Thread.currentThread().name}")  // thread: DefaultDispatcher-worker-1
            // 切换线程！
            withContext(Dispatchers.Default) {
                // 切换后，使用的是 withContext 指定的上下文
                println("网络请求完成后，切换到 Default 线程执行操作！")
                println("withContext: I'm working in context $coroutineContext")  // DispatchedCoroutine{Active} Dispatchers.Default
                println("withContext: I'm working in thread ${Thread.currentThread().name}")  // thread: DefaultDispatcher-worker-3
            }
        }
    }
}

/**
 * 模拟一个网络请求。
 * 在一般的使用场景中，回调也会是在其他线程中执行的，因此回调需声明为 suspend
 * @param finish 网络请求完成后的回调
 */
private suspend fun request(finish: suspend () -> Unit) {
    delay(3000)
    println("网络请求完成！")
    finish()
}