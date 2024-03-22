package org.laughhallo.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Math.random

fun main() {
    demo1()
//    demo2()
}

private fun demo1() {
    println("============= demo1 ==============")

    val flow = flow {
        for (i in 1..30) {
            emit(i)  // 将 i 发射出去
        }
    }

    runBlocking {
        flow.collect {
            // 对于每个发射出来的元素，启动一个协程处理
            launch(Dispatchers.Default) {
                delay((random() * 1000).toLong())  // 模拟耗时操作，0-1 秒
                println("$it on ${Thread.currentThread().name}")
            }
        }
    }
}