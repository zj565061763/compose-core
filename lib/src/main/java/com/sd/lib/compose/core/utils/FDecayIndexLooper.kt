package com.sd.lib.compose.core.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

open class FDecayIndexLooper(
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

    /** 是否已经开始 */
    private val _started = AtomicBoolean(false)

    /** 循环大小 */
    private var _size = 0

    /** 要停止的位置，null-未开始减速 */
    private val _stopIndex = AtomicReference<Int?>(null)

    /**
     * 开始从[0-(size-1)]循环并挂起
     */
    suspend fun startLoop(
        /** 循环大小 */
        size: Int,
        /** 初始位置 */
        initialIndex: Int = 0,
        /** 开始回调 */
        onStart: () -> Unit = {},
    ) {
        if (size <= 0) return
        if (_started.compareAndSet(false, true)) {
            _size = size
            _stopIndex.set(null)
            try {
                currentIndex = initialIndex.coerceIn(0, size - 1)
                looping = true
                onStart()
                delay(linearInterval)

                performLinear()
                performDecay()
            } finally {
                looping = false
                _started.set(false)
            }
        }
    }

    /**
     * 开始减速，并停留在[stopIndex]位置
     */
    fun startDecay(stopIndex: Int) {
        if (_started.get()) {
            val legalIndex = stopIndex.coerceIn(0, _size - 1)
            _stopIndex.compareAndSet(null, legalIndex)
        }
    }

    /**
     * 匀速
     */
    private suspend fun performLinear() {
        loop(
            loop = { _started.get() && _stopIndex.get() == null },
            delay = { delay(linearInterval) }
        )
    }

    /**
     * 减速
     */
    private suspend fun performDecay() {
        val stopIndex = _stopIndex.get() ?: return

        val list = calculateIntervalList()
        if (list.isEmpty()) {
            notifyCurrentIndex(stopIndex)
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

    private suspend fun loop(
        loop: () -> Boolean,
        delay: suspend () -> Unit,
    ) {
        while (loop()) {
            val nextIndex = (checkNotNull(currentIndex) + 1).takeIf { it < _size } ?: 0
            notifyCurrentIndex(nextIndex)
            delay()
        }
    }

    private fun notifyCurrentIndex(index: Int) {
        if (currentIndex != index) {
            currentIndex = index
            onIndexChange(index)
        }
    }

    protected open fun onIndexChange(index: Int) = Unit
}