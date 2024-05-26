package com.sd.lib.compose.core.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class FDecayIndexLooper(
    private val coroutineScope: CoroutineScope = MainScope(),
    /** 匀速间隔 */
    private val linearInterval: Long = 100,
    /** 当减速间隔大于[maxDecayInterval]时停止循环 */
    private val maxDecayInterval: Long = linearInterval * 12,
    /** 计算减速增加的间隔 */
    private val decayIncreasedInterval: (interval: Long) -> Long = { (it * 0.3f).toLong() },
) {
    init {
        require(linearInterval > 0)
        require(maxDecayInterval > linearInterval)
    }

    /** 当前位置，null-未开始过 */
    var currentIndex by mutableStateOf<Int?>(null)
        private set

    /** 是否正在循环中 */
    var looping by mutableStateOf(false)
        private set

    /** 是否正在减速中 */
    var decaying by mutableStateOf(false)
        private set

    /** 是否已经开始 */
    private val _started = AtomicBoolean(false)
    /** 循环大小 */
    private var _size = 0
    /** 要停止的位置，null-未开始减速 */
    private val _stopIndex = AtomicReference<Int?>(null)

    /**
     * 开始从[0至(size-1)]之间无限循环，并通知[currentIndex]
     *
     * @param size 循环大小
     * @param initialIndex 开始的位置
     * @param onStart 开始回调
     * @return 本次调用是否有效
     */
    fun startLoop(
        /** 循环大小 */
        size: Int,
        /** 初始位置 */
        initialIndex: Int = 0,
        /** 开始回调 */
        onStart: () -> Unit = {},
        /** 结束回调 */
        onStop: () -> Unit = {},
    ): Boolean {
        if (size <= 0) return false
        if (!_started.compareAndSet(false, true)) return false

        _size = size
        _stopIndex.set(null)

        return coroutineScope.launch {
            try {
                currentIndex = initialIndex.coerceIn(0, _size - 1)
                looping = true
                onStart()
                delay(linearInterval)

                performLinear()
                performDecay()
            } finally {
                reset()
                onStop()
            }
        }.isActive.also { isActive ->
            if (!isActive) {
                // 如果启动失败，重置
                reset()
            }
        }
    }

    /**
     * 开始减速，并停留在[stopIndex]位置
     *
     * @return 本次调用是否有效
     */
    fun startDecay(stopIndex: Int): Boolean {
        if (_started.get()) {
            val legalIndex = stopIndex.coerceIn(0, _size - 1)
            if (_stopIndex.compareAndSet(null, legalIndex)) {
                decaying = true
                return true
            }
        }
        return false
    }

    private fun reset() {
        decaying = false
        looping = false
        _started.set(false)
    }

    /**
     * 匀速
     */
    private suspend fun performLinear() {
        loop(
            loop = { _stopIndex.get() == null },
            delay = { delay(linearInterval) }
        )
    }

    /**
     * 减速
     */
    private suspend fun performDecay() {
        val stopIndex = checkNotNull(_stopIndex.get())

        val list = calculateIntervalList()
        if (list.isEmpty()) {
            currentIndex = stopIndex
            return
        }

        var step = calculateStartDecayStep(
            size = _size,
            decayCount = list.size,
            currentIndex = checkNotNull(currentIndex),
            stopIndex = stopIndex,
        ).also { check(it >= 0) }

        loop(
            loop = { step > 0 },
            delay = {
                step--
                delay(linearInterval)
            }
        )

        var index = 0
        loop(
            loop = { index < list.size },
            delay = {
                val interval = list[index]
                index++
                delay(interval)
            },
        )
    }

    /**
     * 计算减速间隔
     */
    private fun calculateIntervalList(): List<Long> {
        val list = mutableListOf<Long>()
        var interval = linearInterval
        while (true) {
            interval += decayIncreasedInterval(interval).also { check(it > 0) }
            if (interval > maxDecayInterval) {
                break
            } else {
                list.add(interval)
            }
        }
        return list
    }

    /**
     * 计算可以开始减速的步数
     */
    private fun calculateStartDecayStep(
        size: Int,
        decayCount: Int,
        currentIndex: Int,
        stopIndex: Int,
    ): Int {
        val appendIndex = currentIndex + (decayCount % size)
        val futureIndex = appendIndex.takeIf { it < size } ?: (appendIndex % size)
        return if (futureIndex <= stopIndex) {
            stopIndex - futureIndex
        } else {
            size - (futureIndex - stopIndex)
        }
    }

    /**
     * 循环
     */
    private suspend fun loop(
        loop: () -> Boolean,
        delay: suspend () -> Unit,
    ) {
        while (loop()) {
            val nextIndex = (checkNotNull(currentIndex) + 1).takeIf { it < _size } ?: 0
            currentIndex = nextIndex
            delay()
        }
    }
}