package com.sd.lib.compose.core.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

open class FDecayIndexLooper(
    /** 初始位置 */
    initialIndex: Int = 0,
    /** 匀速间隔 */
    private val linearInterval: Long = 100,
    /** 当减速间隔大于[maxDecayInterval]时停止循环 */
    private val maxDecayInterval: Long = linearInterval * 10,
    /** 计算减速增加的间隔 */
    private val decayIncreasedInterval: (interval: Long) -> Long = { (it * 0.25f).toLong() },
) {
    init {
        require(initialIndex >= 0)
        require(linearInterval > 0)
        require(maxDecayInterval > linearInterval)
    }

    /** 当前位置 */
    var currentIndex by mutableIntStateOf(initialIndex)
        private set

    /** 是否已经开始 */
    private val _started = AtomicBoolean(false)

    /** 要停止的位置 */
    private val _stopIndex = AtomicInteger(-1)

    /** 循环大小 */
    private var _size = 0

    /**
     * 开始循环[size]并挂起
     */
    suspend fun startLoop(size: Int) {
        if (size <= 0) return
        if (_started.compareAndSet(false, true)) {
            _stopIndex.set(-1)
            _size = size
            performLinear()
            performDecay()
            _started.set(false)
        }
    }

    /**
     * 开始减速，并停留在[stopIndex]位置
     */
    fun startDecay(stopIndex: Int) {
        if (_started.get()) {
            val legalIndex = stopIndex.coerceIn(0, _size - 1)
            _stopIndex.compareAndSet(-1, legalIndex)
        }
    }

    /**
     * 匀速
     */
    private suspend fun performLinear() {
        loop(
            loop = { _started.get() && _stopIndex.get() < 0 },
            delay = { delay(linearInterval) }
        )
    }

    /**
     * 减速
     */
    private suspend fun performDecay() {
        val stopIndex = _stopIndex.get()
        check(stopIndex >= 0)

        val list = calculateIntervalList()
        if (list.isEmpty()) {
            notifyCurrentIndex(stopIndex)
            return
        }

        var step = calculateStartDecayStep(
            decayCount = list.size,
            stopIndex = stopIndex,
        ).also { check(it >= 0) }

        loop(
            loop = { step > 0 },
            delay = {
                step--
                delay(linearInterval)
            }
        )

        loop(
            loop = { list.isNotEmpty() },
            delay = { delay(list.removeAt(0)) },
        )
    }

    /**
     * 计算减速间隔
     */
    private fun calculateIntervalList(): MutableList<Long> {
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
        decayCount: Int,
        stopIndex: Int,
    ): Int {
        val size = _size
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
            val nextIndex = (currentIndex + 1).takeIf { it < _size } ?: 0
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