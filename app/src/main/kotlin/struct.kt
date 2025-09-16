package learning

import org.lwjgl.opengl.GL11C.glClearColor
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


interface InputState {
    val forward: Boolean
}

interface Window {
    val handle: Long
    val inputState: InputState
}

class BaseWindow(
    override val handle: Long
) : Window {
    class InputStateImpl: InputState {
        override val forward: Boolean = true
    }
    override var inputState: InputState = InputStateImpl()
}

interface RenderAble {
    suspend fun render() {}
}

interface UpdateAble {
    suspend fun update(duration: Duration) {}
}

interface ComponentContext {
    val window: Window
}

abstract class Component : ComponentContext, UpdateAble, RenderAble {
    open val parent: Component? = null
    open val sonList: MutableList<Component> = mutableListOf()

    override suspend fun update(duration: Duration) {
        sonList.forEach { it.update(duration) }
    }
    override suspend fun render() {
        sonList.forEach { it.render() }
    }

    suspend fun updateAndRender(duration: Duration) {
        sonList.forEach {
            it.updateAndRender(duration)
        }
        this.update(duration = duration)
        this.render()
    }
}



class Scene(override val window: Window): Component() {
    override val parent: Component? = null
    override val sonList: MutableList<Component> = mutableListOf()
}

class FlashBackground(
    override val parent: Component,
    override val window: Window,
) : Component() {
    var red = 0.0
    // 每 0.01 秒 变化 0.01f
    var dRedPerDRate = 0.01f
    var dRate = 0.01.seconds
    override suspend fun update(duration: Duration) {
        val dRed = duration / dRate * dRedPerDRate
        red += dRed
        when {
            red < 0 -> {
                red = 0.0
                dRedPerDRate = -dRedPerDRate
            }
            red > 1 -> {
                red = 1.0
                dRedPerDRate = -dRedPerDRate
            }
        }
    }

    override suspend fun render() {
        glClearColor(red.toFloat(), 0f, 0f, 0f)
    }
}

fun Component.flashBackground() {
    val parent = this
    val back = FlashBackground(
        parent = parent,
        window = window,
    )
    parent.sonList.add(back)
}