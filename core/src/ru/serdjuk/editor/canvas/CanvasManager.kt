package ru.serdjuk.editor.canvas

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import ru.serdjuk.editor.*
import ru.serdjuk.editor.layer.Layer
import ru.serdjuk.editor.layer.tools.ToolWorking

@ExperimentalUnsignedTypes
class CanvasManager(private val canvas: Canvas, private val stage: Stage) {

    val layers = ArrayList<Layer>()
    private val toolWorking = ToolWorking(stage)

    fun update(delta: Float) {
        layers.forEach {
            if (it.isVisible) {
                toolWorking.update(it)
                it.update(stage)
            }
        }
        cameraDrag()
        smartScale()
    }


    private fun cameraDrag() {
        if (Gdx.input.isButtonPressed(2)) {
            refreshMouse(canvas.viewport)
            val x = mouse.x - preMouse.x
            val y = mouse.y - preMouse.y
            canvas.camera.translate(-x, -y)
            canvas.camera.update()
        }
    }

    private fun smartScale() {
        if (mouseWheelAmount == 0) return
        canvas.cameraScale = when (mouseWheelAmount) {
            1 -> MathUtils.clamp(canvas.cameraScale + 0.04f, 0.02f, 5f)
            -1 -> MathUtils.clamp(canvas.cameraScale - 0.04f, 0.02f, 5f)
            else -> {
                mouseWheelAmount = 0
                return
            }
        }
        canvas.camera.zoom = canvas.cameraScale
        canvas.camera.update()
        refreshMouse(canvas.viewport)
        val x = mouse.x - preMouse.x
        val y = mouse.y - preMouse.y
        canvas.camera.translate(-x, -y)
        canvas.camera.update()
        mouseWheelAmount = 0
    }


}