package ru.serdjuk.editor.canvas

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ru.serdjuk.editor.mouseInActor
import ru.serdjuk.editor.play.Play
import ru.serdjuk.editor.refreshMouse
import ru.serdjuk.editor.sprites.SpriteSheet
import ru.serdjuk.editor.worldHeight
import ru.serdjuk.editor.worldWidth

@ExperimentalUnsignedTypes
class Canvas(val stage: Stage, val spriteSheet: SpriteSheet) {

    val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()).also { it.setToOrtho(true) }
    val viewport = ExtendViewport(worldWidth.toFloat(), worldHeight.toFloat(), camera)
    val batch = SpriteBatch()
    val shape = ShapeRenderer().also { it.setAutoShapeType(true) }
    val manager = CanvasManager(this, stage)
    val width = 15
    val height = 10
    val spriteSize = 16f
    var cameraScale = 1.3f
    val bounds = Rectangle(0f, 0f, worldWidth, worldHeight)

    val play = Play(this)


    fun update(delta: Float) {
        if (!mouseInActor)
            manager.update(delta)
//        play.update(manager.layers)
    }

    fun draw() {
        batch.projectionMatrix = camera.combined
        batch.begin()
        manager.layers.forEach { it.draw(batch) }
        batch.end()
        refreshMouse(viewport)
        drawShapes()
    }

    private fun drawShapes() {
        Gdx.gl20.glEnable(GL20.GL_BLEND)
        shape.projectionMatrix = camera.combined
        shape.begin()
        manager.layers[1].drawShapes(shape)    //  FIXME all layers draw grid
        shape.color = Color.WHITE
        shape.rect(bounds.x,bounds.y,bounds.width,bounds.height)
        shape.end()
        Gdx.gl20.glDisable(GL20.GL_BLEND)
    }

    fun resize(width: Int, height: Int) {
        camera.zoom = cameraScale
        camera.update()
        viewport.update(width, height, true)
    }

}

