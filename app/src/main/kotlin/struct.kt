package learning

import org.lwjgl.opengl.GL11C.glClearColor


interface InputState {
    val forward: Boolean
}

interface Window {
    val handle: Long
    val inputState: InputState
}


interface Component {
    val window: Window

    fun render() {}
    fun updateComponent() {}
}

class Background(override val window: Window) : Component {
    override fun render() {
        glClearColor(0.1f, 0.1f, 0.1f, 0.0f)
    }
}