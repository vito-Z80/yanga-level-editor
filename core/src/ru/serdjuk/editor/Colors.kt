package ru.serdjuk.editor

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.*

class Colors(stage: Stage) : VisWindow("Colors") {

    enum class Type {
        INC, PAPER
    }

    val inc = VisTable()
    val paper = VisTable()
    val brightness = VisCheckBox("Brightness")



    val colorArea = IntArray(150) { 7 }

    init {
        inc.add(button(Color.BLUE).also { it.addListener(listener(Type.INC, it)) }).pad(4f)
        inc.add(button(Color.RED).also { it.addListener(listener(Type.INC, it)) }).pad(4f)
        inc.add(button(Color.PURPLE).also { it.addListener(listener(Type.INC, it)) }).pad(4f)
        inc.add(button(Color.GREEN).also { it.addListener(listener(Type.INC, it)) }).pad(4f)
        inc.add(button(Color.CYAN).also { it.addListener(listener(Type.INC, it)) }).pad(4f)
        inc.add(button(Color.YELLOW).also { it.addListener(listener(Type.INC, it)) }).pad(4f)
        inc.add(button(Color.WHITE).also { it.addListener(listener(Type.INC, it)) }).pad(4f)
        inc.add(button(Color.BLACK).also { it.addListener(listener(Type.INC, it)) }).pad(4f)
        inc.pad(4f)

        paper.add(button(Color.BLUE).also { it.addListener(listener(Type.PAPER, it)) }).pad(4f)
        paper.add(button(Color.RED).also { it.addListener(listener(Type.PAPER, it)) }).pad(4f)
        paper.add(button(Color.PURPLE).also { it.addListener(listener(Type.PAPER, it)) }).pad(4f)
        paper.add(button(Color.GREEN).also { it.addListener(listener(Type.PAPER, it)) }).pad(4f)
        paper.add(button(Color.CYAN).also { it.addListener(listener(Type.PAPER, it)) }).pad(4f)
        paper.add(button(Color.YELLOW).also { it.addListener(listener(Type.PAPER, it)) }).pad(4f)
        paper.add(button(Color.WHITE).also { it.addListener(listener(Type.PAPER, it)) }).pad(4f)
        paper.add(button(Color.BLACK).also { it.addListener(listener(Type.PAPER, it)) }).pad(4f)
        paper.pad(4f)
        val currentColorTable = VisTable()
        currentColorTable.add("Current inc:").align(Align.left)
        currentColorTable.add(object : VisImage(setSprite(currentInc)) {
            override fun act(delta: Float) {
                color = currentInc
                super.act(delta)
            }
        }).pad(8f).row()
        currentColorTable.add("Current paper:").align(Align.left)
        currentColorTable.add(object : VisImage(setSprite(currentPaper)) {
            override fun act(delta: Float) {
                color = currentPaper
                super.act(delta)
            }
        }).pad(8f).row()
        currentColorTable.pack()
        add(currentColorTable)
        row()
        add(inc)
        row()
        add(paper)
        pack()
        stage.addActor(this)
    }

    fun listener(type: Type, btn: VisImageButton) = object : ChangeListener() {
        override fun changed(p0: ChangeEvent?, p1: Actor?) {
            when (type) {
                Type.INC -> currentInc = btn.color
                Type.PAPER -> currentPaper = btn.color
            }
        }
    }

    fun button(color: Color): VisImageButton {
        return VisImageButton(SpriteDrawable(Sprite(setTexture(color)
        ).also { it.setBounds(0f, 0f, 24f, 24f) })).also { it.color = color }
    }

    fun setSprite(color: Color) = SpriteDrawable(Sprite(setTexture(color)))

    fun setTexture(color: Color) = Texture(Pixmap(24, 24, Pixmap.Format.RGB565).also {
        it.setColor(color)
        it.fill()
    })


}