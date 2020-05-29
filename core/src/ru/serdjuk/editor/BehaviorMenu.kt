package ru.serdjuk.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.*
import ru.serdjuk.editor.imitationZXObjects.ZXObject

object BehaviorMenu {

    val menu: PopupMenu = PopupMenu()
    val itemTable = MenuItem("aaaaaa")
    val table = VisTable()
    private val enable = "Enable"
    private val disable = "Disable"

    private val checkGroup = ButtonGroup<VisCheckBox>()
    //---------------------------------
    private val toggleValue = VisLabel(disable)
    private val dangerValue = VisLabel(disable)

    fun install(stage: Stage) {
        addItems()
        stage.addActor(menu)
    }

    private fun addItems() {
        val toggle = VisCheckBox("Toggle")
        val danger = VisCheckBox("Danger")
        checkGroup.add(toggle, danger)

        table.add(toggle).align(Align.right)
        table.add(toggleValue).align(Align.left)
        table.row()

        table.add(danger).align(Align.right)
        table.add(dangerValue).align(Align.left)
        table.row()

        itemTable.addActor(table)
        menu.addItem(itemTable)

//        val item = MenuItem("")
//        item.add(VisCheckBox("DANGER")).row()
//        menu.addItem(item)
//        val item2 = MenuItem("")
//        item.add(VisCheckBox("TOGGLE")).row()
//        menu.addItem(item2)


        menu.pack()
    }

    fun show(stage: Stage, it: ZXObject) {
        menu.showMenu(stage, Gdx.input.x.toFloat(), (Gdx.graphics.height - Gdx.input.y).toFloat())
        menu.pack()
    }

    fun dispose() {
        menu.remove()
    }
}