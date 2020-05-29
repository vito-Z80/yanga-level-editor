package ru.serdjuk.editor.layer.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import ru.serdjuk.editor.mouse

open class UniversalUnit : Values(), Unit {

    override fun update(delta: Float) {
        if (boundingRectangle.contains(mouse.x, mouse.y)) {
            if (Gdx.input.isButtonJustPressed(1)) {
                menu()
            }
        }
    }

    override fun install(region: TextureRegion, bounds: Rectangle) {
        setRegion(region)
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height)
        setOriginCenter()
    }

    override fun draw(batch: SpriteBatch) {
        super.draw(batch)
    }

    private fun menu() {

    }

}


interface Unit {

    fun install(region: TextureRegion, bounds: Rectangle)
    fun update(float: Float = 0.016f)
    fun draw(batch: SpriteBatch)

}

open class Values : Sprite() {
    var preX = 0
    var preY = 0
    var dstX = 0
    var dstY = 0
    var dstX2 = 0
    var dstY2 = 0

    var dangerous = 0
    var isToggle = false

    var through = false
    var driven = false
    /*
    параметр отвечает за перемещение объекта такого как враг
        -1 - не шагающий объект
         0 - по горизонтали
         1 - по вертикали
         2 - рандомно
         3 - умный
     */
    var walking = -1
    var vulnerable = false
    var isVisible = true

    var id = -1
    var name = ""
}


class Wall : UniversalUnit() {

}