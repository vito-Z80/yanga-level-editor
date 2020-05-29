package ru.serdjuk.editor.layer.tools

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Cursor
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.util.dialog.ConfirmDialogListener
import com.kotcrab.vis.ui.util.dialog.Dialogs
import ru.serdjuk.editor.*
import ru.serdjuk.editor.imitationZXObjects.ZXObject
import ru.serdjuk.editor.layer.Layer
import kotlin.math.ceil
import kotlin.math.floor

@ExperimentalUnsignedTypes
class ToolWorking(private val stage: Stage) {


    fun upd() {

    }


    fun update(layer: Layer) {
        if (mouseInActor || !layer.canvas.bounds.contains(mouse.x, mouse.y)) return
        when (selectedTool) {
            Tools.PENCIL -> pencil(layer)
            Tools.SELECTION -> selection(layer)
            Tools.MOVING -> moving(layer.getLayer())
            Tools.FILL -> fill(layer)
        }
    }
//----------------------------------------------------------------------------------------------------------------------

    private fun fill(layer: Layer) {
        setColorMode(layer)

    }

    //----------------------------------------------------------------------------------------------------------------------
    private var movingLoner: ZXObject? = null
    private val startPosition = Vector2()

    private fun moving(layer: ArrayList<ZXObject>) {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand)
        if (Gdx.input.isButtonJustPressed(0)) {
            movingLoner = layer.find { it.boundingRectangle.contains(mouse.x, mouse.y) }
            startPosition.set(movingLoner?.x ?: -100f, movingLoner?.y ?: -100f)
        }
        if (Gdx.input.isButtonPressed(0) && movingLoner != null) {
            layer.remove(movingLoner!!)
            layer.add(movingLoner!!)
            movingLoner?.setPosition(floor(mouse.x), floor(mouse.y))
        }
        if (isButtonUp(0)) {
            val underObject = layer.find { it != movingLoner && it.x == movingLoner?.x && it.y == movingLoner?.y }
            if (underObject != null) {
                val dialog = Dialogs.showConfirmDialog(stage,
                        "Замена объекта:",
                        "Под перемещаемым объектом\nимеется другой объект.\n" +
                                "Заменить другой объект\nна текущий ?",
                        arrayOf("No", "Yes"),
                        arrayOf(false, true),
                        ConfirmDialogListener<Boolean> {
                            if (it) {
                                layer.remove(underObject)
                            } else {
                                movingLoner?.setPosition(startPosition.x, startPosition.y)
                                movingLoner = null
                            }
                        }
                )
            } else {
                movingLoner = null
            }

        }
    }
//----------------------------------------------------------------------------------------------------------------------

    private fun selection(layer: Layer) {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair)
        highlight(layer.selection)
        clear(layer.selection)
        removeSelectedObjects(layer)
    }

    private fun removeSelectedObjects(layer: Layer) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DEL)) {
            layer.getLayer().removeAll { layer.selection.overlaps(it.boundingRectangle) }
        }
    }


    private fun highlight(selection: Rectangle) {
        if (Gdx.input.isButtonJustPressed(0)) {
            selection.x = floor(mouse.x)
            selection.y = floor(mouse.y)
            selection.setSize(0f)
        }
        if (Gdx.input.isButtonPressed(0)) {
            selection.width = ceil(mouse.x) - selection.x
            selection.height = ceil(mouse.y) - selection.y
        }
    }

    private fun clear(selection: Rectangle) {
        if (Gdx.input.justTouched() && !selection.contains(mouse.x, mouse.y) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            selection.set(0f, 0f, 0f, 0f)
    }

    //----------------------------------------------------------------------------------------------------------------------
    private fun pencil(layer: Layer) {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow)
        clear(layer.selection)
        if (Gdx.input.isButtonPressed(0)) {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                removeObject(layer)
            } else {
                addObject(layer)
            }
        }
    }

    private fun removeObject(layer: Layer) {
        val existing = layer.getLayer().find { it.boundingRectangle.contains(mouse.x, mouse.y) }
        if (existing != null){
            layer.colors[(existing.y * 15 + existing.x).toInt()] = 7
            layer.getLayer().remove(existing)
        }
    }

    private fun addObject(layer: Layer) {
        removeObject(layer)
        if (layer.canvas.spriteSheet.selected == "empty") return
        val replacement = ZXObject()
        replacement.setBounds(floor(mouse.x), floor(mouse.y), 1f, 1f)
        replacement.setRegion(layer.canvas.spriteSheet.getSelectedTexture())
        replacement.textureName = layer.canvas.spriteSheet.selected
        layer.canvas.spriteSheet.setValues(replacement)
        layer.getLayer().add(replacement)
    }

    private fun setColorMode(layer: Layer) {
        if (!Gdx.input.isButtonPressed(0)) return
        val selected = layer.getLayer().find { it.boundingRectangle.contains(mouse.x, mouse.y) }
        if (selected != null && selected.name != "empty") {
            selected.texture = layer.canvas.spriteSheet.fromWBM("sprites/${selected.textureName}.wbm", true)
            setZxColor(selected, layer.colors)
        }
    }

    private fun setZxColor(zxObject: ZXObject, colorsMap: ByteArray) {
        val inc = when (currentInc) {
            Color.BLUE -> 1
            Color.RED -> 2
            Color.PURPLE -> 3
            Color.GREEN -> 4
            Color.CYAN -> 5
            Color.YELLOW -> 6
            Color.WHITE -> 7
            Color.BLACK -> 0
            else -> 4
        }
        val paper = when (currentPaper) {
            Color.BLUE -> 1
            Color.RED -> 2
            Color.PURPLE -> 3
            Color.GREEN -> 4
            Color.CYAN -> 5
            Color.YELLOW -> 6
            Color.WHITE -> 7
            Color.BLACK -> 0
            else -> 4
        }
        val color = (inc or (paper shl 3))
        zxObject.zxColor = color
        val id = (zxObject.y * 15 + zxObject.x).toInt()
        colorsMap[id] = color.toByte()
    }


}