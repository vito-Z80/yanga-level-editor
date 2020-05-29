package ru.serdjuk.editor.play

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ru.serdjuk.editor.canvas.Canvas
import ru.serdjuk.editor.imitationZXObjects.ZXObject
import ru.serdjuk.editor.layer.Layer

@ExperimentalUnsignedTypes
class Play(private val canvas: Canvas) {


    var player = ZXObject().also {
        it.setRegion(canvas.spriteSheet.textures["hero_01"])
        it.setBounds(0f, 0f, 16f, 16f)
        it.name = "hero"
        it.dangerous = 2
        it.through = false
        it.isToggle = false
        it.id = 0
        it.setFlip(false, true)
    }

    var preId = 0

    var isPlaying = false
    var direction = 0


    fun update(layers: ArrayList<Layer>) {
        isPlaying = canvas.manager.layers[1].getLayer().any { it.name == "hero" }
        if (!isPlaying && canvas.manager.layers[1].isVisible) return
        player = canvas.manager.layers[1].getLayer().first { it.name == "hero" }
        control()
        move()
        calculate(layers[1].getLayer())

    }


    private fun move() {
        if (player.dstX > player.x) {
            player.preX = player.x.toInt()
            player.x++
        }
        if (player.dstX < player.x) {
            player.preX = player.x.toInt()
            player.x--
        }

        if (player.dstY > player.y) {
            player.preY = player.y.toInt()
            player.y++
        }
        if (player.dstY < player.y) {
            player.preY = player.y.toInt()
            player.y--
        }

    }

    private fun calculate(layer: ArrayList<ZXObject>) {
        collisions(layer)
        mirrorEdges()
    }

    private fun mirrorEdges() {

    }

    private fun collisions(layer: ArrayList<ZXObject>) {
        if (!layer[getCell()].through) {
            if (direction == 1 || direction == 2) {
                player.x = player.preX.toFloat()
                player.dstX = player.preX
            }
            if (direction == 3 || direction == 4) {
                player.y = player.preY.toFloat()
                player.dstY = player.preY
            }
            direction = 0
            player.id = preId
        }
    }


    private fun getCell(): Int {
        val x: Int = (player.dstX / canvas.spriteSize).toInt()
        val y: Int = (player.dstY / canvas.spriteSize).toInt()
        return y * canvas.width + x
    }

    private fun control() {
        if (player.x.toInt() != player.dstX || player.y.toInt() != player.dstY) return
        if (Gdx.input.isKeyPressed(Input.Keys.W) && player.dstY == player.y.toInt()) {
            player.dstY -= player.height.toInt()
            preId = player.id
            player.id -= canvas.width
            direction = 3
            return
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) && player.dstY == player.y.toInt()) {
            player.dstY += player.height.toInt()
            preId = player.id
            player.id += canvas.width
            direction = 4
            return
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.dstX == player.x.toInt()) {
            player.dstX -= player.width.toInt()
            preId = player.id
            player.id--
            direction = 1
            return
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.dstX == player.x.toInt()) {
            player.dstX += player.width.toInt()
            preId = player.id
            player.id++
            direction = 2
            return
        }

    }

}