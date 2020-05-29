package ru.serdjuk.editor.layer

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import ru.serdjuk.editor.canvas.Canvas
import ru.serdjuk.editor.sprites.SpriteSheet

@ExperimentalUnsignedTypes
object LayerMenu : VisWindow("Layers:") {

    val table = VisTable()
    var layerId = 0

    fun install(stage: Stage, canvas: Canvas, spriteSheet: SpriteSheet) {
        addLayer(canvas)
        canvas.manager.layers[0].isVisible = false
        addLayer(canvas)
        add(table)
        setPosition(0f, 300f)
        pack()
        stage.addActor(this)
    }


    //  TODO слои чет проебываются - решить нах !!!!

    private fun addLayer(canvas: Canvas) {
        if (layerId == 2) {     //fixme для спека и 2 слоя до жопы !!!!
            return
        }
        val textButton = VisTextButton("Layer $layerId")
        val plusButton = VisTextButton(" + ")
        textButton.pack()
        plusButton.pack()
        val layer = Layer((canvas))
        layerListener(canvas, textButton)
        plusListener(canvas, plusButton)
        table.add(textButton)
        table.add(plusButton)
        table.row()
        table.pack()
        pack()
        layer.id = layerId
        canvas.manager.layers.add(layer)
        layerId++
    }

    private fun layerListener(canvas: Canvas, textButton: VisTextButton) {
        val currentId = layerId
        textButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                canvas.manager.layers.forEach { layer ->
                    layer.isVisible = layer.id == currentId
                }
            }

        })
    }

    private fun plusListener(canvas: Canvas, plusButton: VisTextButton) {
        plusButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                plusButton.clearListeners()
                plusButton.remove()
                addLayer(canvas)
            }

        })
    }

}