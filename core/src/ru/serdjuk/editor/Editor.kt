package ru.serdjuk.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.PopupMenu
import ru.serdjuk.editor.canvas.Canvas
import ru.serdjuk.editor.layer.LayerMenu
import ru.serdjuk.editor.layer.tools.ToolsPanel
import ru.serdjuk.editor.sprites.SpriteSheet

@ExperimentalUnsignedTypes
class Editor : Screen {
    private val stage = Stage(ScreenViewport(OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())))
    private val vu = VisUI.load()
    private val spriteSheet = SpriteSheet().also { it.install(stage) }
    private val canvas = Canvas(stage, spriteSheet)
    private val s = LayerMenu.install(stage, canvas, spriteSheet)
    private val toolsPanel = ToolsPanel()


    override fun show() {
        Gdx.input.inputProcessor = stage
        toolsPanel.install(stage)
        stage.addListener(object : ClickListener() {
            override fun scrolled(event: InputEvent?, x: Float, y: Float, amount: Int): Boolean {
                if (!mouseInActor) mouseWheelAmount = amount
                return super.scrolled(event, x, y, amount)
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {

                super.touchUp(event, x, y, pointer, button)
            }

            override fun keyDown(event: InputEvent?, keycode: Int): Boolean {

                return super.keyDown(event, keycode)
            }
        })
        Colors(stage)
    }

    override fun render(delta: Float) {
        mouseInActor = stage.hit(Gdx.input.x.toFloat(), (Gdx.graphics.height - Gdx.input.y).toFloat(), true) != null
//        mouse.set(refreshMouse(canvas.viewport))
        canvas.update(delta)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        canvas.draw()
        mouse.set(refreshMouse(canvas.viewport))
        preMouse.set(mouse)
        stage.act(delta)
        stage.draw()
    }

    override fun hide() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        PopupMenu.removeEveryMenu(stage)
        stage.viewport.update(width, height, true)
        canvas.resize(width, height)
    }

    override fun dispose() {
    }

}