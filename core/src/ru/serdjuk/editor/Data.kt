package ru.serdjuk.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport

var currentInc = Color.WHITE
var currentPaper = Color.WHITE

val worldWidth = 15f
val worldHeight = 10f

val cellSize = 16

var mouseWheelAmount = 0
var mouseButtonUp = false
var mouseInActor = false

val mouse = Vector3()
val preMouse = Vector3()
val refreshMouse = { viewport: Viewport ->
    val vec = Vector3(
            Gdx.input.x.toFloat(),
            Gdx.input.y.toFloat(),
            100f
    )
//    viewport.camera.update()
    mouse.set(viewport.camera.unproject(vec))
    mouse
}

fun isButtonJustPressed(b: Int) = Gdx.input.isButtonJustPressed(b)
fun isButtonPressed(b: Int) = Gdx.input.isButtonPressed(b)

private var buttonUpValue = false
private var just = false

val isButtonUp = { button: Int ->
    if (buttonUpValue) {
        just = false
        buttonUpValue = just
    }
    if (Gdx.input.justTouched()) just = true
    if (just) {
        buttonUpValue = !Gdx.input.isTouched
    }
    buttonUpValue
}
/*
stage.viewport = worldPosition
canvas.viewport = screenPosition
null = worldPosition, as mousePosition = worldPosition
buttonUp(?)
 */
val buttonUp = { viewport: Viewport? ->
    if (buttonUpValue) {
        just = false
        buttonUpValue = just
    }
    if (Gdx.input.justTouched()) just = true
    if (just) {
        buttonUpValue = !Gdx.input.isTouched
    }
    if (viewport == null) mouse
    else Vector3(viewport.project(mouse))
}

//  isKeysPressed(Input.Keys.CONTROL_LEFT, Input.Keys.SHIFT_LEFT, Input.Keys.D)
fun isKeysPressed(vararg k: Int): Boolean {
    var result = true
    fun pressed(key: Int) = Gdx.input.isKeyPressed(key)
    fun justPressed(key: Int) = Gdx.input.isKeyJustPressed(key)
    k.forEach { key ->
        if (!result) return false
        result = when (key) {
            Input.Keys.ALT_LEFT,
            Input.Keys.ALT_RIGHT,
            Input.Keys.CONTROL_LEFT,
            Input.Keys.CONTROL_RIGHT,
            Input.Keys.SHIFT_LEFT,
            Input.Keys.SHIFT_RIGHT -> pressed(key)
            else -> justPressed(key)
        }
    }
    return result
}

var selectedTool = Tools.PENCIL

enum class Tools {
    PENCIL,SELECTION, MOVING,FILL
}


fun Boolean.asInt(): Int {
    return if (this) 0 else 1
}
