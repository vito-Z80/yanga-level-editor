package ru.serdjuk.editor.layer.tools

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Tooltip
import com.kotcrab.vis.ui.widget.VisImage
import com.kotcrab.vis.ui.widget.VisImageButton
import com.kotcrab.vis.ui.widget.VisWindow
import ru.serdjuk.editor.Tools
import ru.serdjuk.editor.selectedTool

class ToolsPanel : VisWindow("") {

    private val textures = listOf(
            "tools/instrumentPanel_0001_stamp.png",
            "tools/instrumentPanel_0003_selection.png",
            "tools/instrumentPanel_0000_moving.png",
            "tools/instrumentPanel_0002_fill.png"
    )

    private val toolTip = listOf(
            "Инструмент: \"Кисть\"\nCTRL+B",
            "Инструмент: \"Выделение\"\nCTRL+S",
            "Инструмент: \"Перемещение\"\nCTRL+M",
            "Инструмент: \"Заливка\"\nCTRL+F"
    )

    fun install(stage: Stage) {
        createTools()
        setPosition(0f, 0f)
        pack()
        stage.addActor(this)
    }


    override fun act(delta: Float) {
        super.act(delta)
        children.forEach {
            if (it is VisImageButton) {
                val b = it as VisImageButton
                if (b.name != selectedTool.name) {
                    b.image.setScale(0.8f)
                } else {
                    b.image.setScale(1f)
                    selectedTool = Tools.valueOf(b.name)
                    titleLabel.setText(b.name)
                }
            }
        }
    }

    private fun createTools() {
        Tools.values().forEachIndexed { id: Int, tool: Tools ->
            val button = createToolButton(VisImage(Texture(textures[id])).drawable, toolTip[id])
            button.addListener(listener(tool, button))
            button.name = tool.name
        }

        println(children.size)
    }

    private fun createToolButton(drawable: Drawable, toolText: String): VisImageButton {
        val button = VisImageButton(drawable)
        button.center()
        button.color = Color.YELLOW
        button.image.setScale(0.8f)
        button.image.setOrigin(32f, 32f)
        button.pack()
        add(button)
        row()
        Tooltip.Builder(toolText, Align.left).target(button).build()
        return button
    }

    private val listener = { tool: Tools, button: VisImageButton ->
        object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                selectedTool = tool
                titleLabel.setText(tool.name)
            }
        }
    }



}