package ru.serdjuk.editor.layer.abilityWindow

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.*
import ru.serdjuk.editor.cellSize
import ru.serdjuk.editor.imitationZXObjects.ZXObject

class AbilityWindow {

    companion object {
        const val width = 200f
    }

    fun listener(window: VisWindow) = object : InputListener() {
        val bounds = Rectangle()
        override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            bounds.set(window.x, window.y, window.width, window.height)
            val vec = Vector2()
            vec.set(Gdx.input.x.toFloat(), (Gdx.graphics.height - Gdx.input.y).toFloat())
            if (!bounds.contains(vec)) {
                window.fadeOut()
            }
            return super.touchDown(event, x, y, pointer, button)
        }
    }

    val window = object : VisWindow("") {
        override fun closeOnEscape() {
            super.closeOnEscape()
            addListener(listener(this))
        }

    }

    val table = VisTable()


    fun show(stage: Stage, objects: List<ZXObject>) {
        window.titleLabel.setText("Selected objects: ${objects.size}")
        window.add(fillTable(stage, objects))
        window.closeOnEscape()
        window.addCloseButton()
        val pos = window.localToStageCoordinates(Vector2(Gdx.input.x.toFloat(), Gdx.graphics.height - Gdx.input.y.toFloat()))
        window.setPosition(pos.x - 10f, pos.y - 10f)
        window.isModal = true
        window.pack()
        window.width = AbilityWindow.width
        stage.addActor(window)
    }

    private fun fillTable(stage: Stage, objects: List<ZXObject>): VisTable {
        val objectsSize = objects.size
        through(objects)
        AbilityDriven().install(stage, this, objects)
        if (objectsSize == 1) AbilityWalk().install(stage, this, objects)
        chooseColor(objects)
        return table
    }

    //------------------------------------------------------------------------------------------------------------------
    fun setCheckBoxValues(checkBox: VisCheckBox, objects: List<ZXObject>, function: () -> Unit) {
        checkBox.setText(if (objects.size > 1) " : ?" else "")
        checkBox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                function()
            }
        })
    }


    //------------------------------------------------------------------------------------------------------------------
    private fun through(objects: List<ZXObject>) {
        val t = VisLabel("Through: ")
        t.setAlignment(Align.left)
        table.add(t).align(Align.left)
        table.add(VisCheckBox("").also {
            setCheckBoxValues(it, objects) { objects.forEach { obj -> obj.through = it.isChecked } }
            it.isChecked = if (objects.size > 1) objects.all { obj -> obj.through } else objects[0].through ?: false
        }).align(Align.left)
        table.row()
    }

    //------------------------------------------------------------------------------------------------------------------
    private fun chooseColor(objects: List<ZXObject>) {
        val color = VisLabel("Color:")
        color.setAlignment(Align.left)
        val horizontal = VisTable()
        horizontal.add(createItemColor(Color.BLUE, objects))
        horizontal.add(createItemColor(Color.RED, objects))
        horizontal.add(createItemColor(Color.YELLOW, objects))
        horizontal.add(createItemColor(Color.WHITE, objects))
        horizontal.add(createItemColor(Color.PURPLE, objects))
        horizontal.add(createItemColor(Color.GREEN, objects))
        horizontal.add(createItemColor(Color.CYAN, objects))
        val scroll = VisScrollPane(horizontal)
        scroll.setupOverscroll(0f, 0.1f, 3f)
        scroll.setupFadeScrollBars(0f, 0f)
        scroll.setScrollingDisabled(false, true)
        table.add(color).width(96f)
        table.add(scroll).align(Align.left)
        table.row()
    }

    private fun createItemColor(color: Color, objects: List<ZXObject>): VisImageButton {
        val image = Image(Texture(Pixmap(cellSize, cellSize, Pixmap.Format.RGBA8888).also {
            it.setColor(color)
            it.fill()
        }))
        val button = VisImageButton(image.drawable)
        button.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                objects.forEach { it.color = color }
            }

        })
        return button
    }

}