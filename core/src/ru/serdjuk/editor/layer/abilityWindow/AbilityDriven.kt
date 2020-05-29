package ru.serdjuk.editor.layer.abilityWindow

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup
import com.kotcrab.vis.ui.layout.VerticalFlowGroup
import com.kotcrab.vis.ui.widget.*
import ru.serdjuk.editor.imitationZXObjects.ZXObject

class AbilityDriven {

    val container = HorizontalFlowGroup()

    fun install(stage: Stage, window: AbilityWindow, objects: List<ZXObject>) {
        val d = VisLabel("Driving: ")
        d.setAlignment(Align.topLeft)
        window.table.add(d).align(Align.topLeft)
        window.table.add(VisCheckBox("").also {
            window.setCheckBoxValues(it, objects) { objects.forEach { obj -> obj.driven = it.isChecked } }
            it.isChecked = if (objects.size > 1) objects.all { obj -> obj.driven } else objects[0].driven ?: false
        }).align(Align.left)
        window.table.row()
    }


}