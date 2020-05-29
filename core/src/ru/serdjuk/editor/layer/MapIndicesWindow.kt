package ru.serdjuk.editor.layer

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisWindow
import ru.serdjuk.editor.canvas.Canvas
import ru.serdjuk.editor.imitationZXObjects.ZXObject

@ExperimentalUnsignedTypes
class MapIndicesWindow(private val canvas: Canvas) {


    val textObjects = StringBuilder("Objects: ")

    var isVisible = true
    val stage = canvas.stage
    val width = canvas.width
    val height = canvas.height

    val window = VisWindow("")
    val table = VisTable()

    val cells = Array<VisLabel>(width * height) { VisLabel("#") }


    fun install() {
        createTable()
        window.add(table)
        window.centerWindow()
        window.pack()
        stage.addActor(window)
    }

    fun update(layer: ArrayList<ZXObject>) {
        if (!isVisible) return
        window.titleLabel.setText("$textObjects${layer.size}")

    }


    private fun indicesCounter() = cells.count { !it.text.equalsIgnoreCase("#") }

    private fun createTable() {
        repeat(height) { row ->
            repeat(width) { column ->
                val label = VisLabel("#")
                label.setAlignment(Align.center)
                label.color = Color.RED
                val cell = table.add(label).align(Align.center).width(28f).height(14f).pad(2f)
                cells[row * width + column] = cell.actor as VisLabel
            }
            table.row()
        }
    }

}