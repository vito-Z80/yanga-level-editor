package ru.serdjuk.editor.layer.abilityWindow

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisRadioButton
import ru.serdjuk.editor.imitationZXObjects.ZXObject

class AbilityWalk {

    private val container = HorizontalFlowGroup()

    fun install(stage: Stage, window: AbilityWindow, objects: List<ZXObject>) {
        if (objects.size != 1) throw Exception("Walking ability must be one selected object (enemy,some blocks)")
        val d = VisLabel("Walking: ")
        d.setAlignment(Align.topLeft)
        window.table.add(d).align(Align.topLeft)
        val group = ButtonGroup<VisRadioButton>()
        group.setMaxCheckCount(1)
        group.setMinCheckCount(1)
        val r1 = VisRadioButton("Horizontal")
        val r2 = VisRadioButton("Vertical")
        val r3 = VisRadioButton("Random")
        val r4 = VisRadioButton("Smart")
        group.add(r1, r2, r3, r4)
        val checkBox = VisCheckBox("")
        checkBox.isChecked = objects[0].walking != 0
        container.addActor(checkBox)

        if (checkBox.isChecked) {
            container.addActor(r1)
            container.addActor(r2)
            container.addActor(r3)
            container.addActor(r4)
        }
        when (objects[0].walking) {
            1 -> r1.isChecked = true
            2 -> r2.isChecked = true
            3 -> r3.isChecked = true
            4 -> r4.isChecked = true
            else -> Unit
        }
        val cell = window.table.add(container).align(Align.topLeft)
        container.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                if (checkBox.isChecked) {
                    cell.actor.addActor(r1)
                    cell.actor.addActor(r2)
                    cell.actor.addActor(r3)
                    cell.actor.addActor(r4)
                    objects[0].walking = group.checkedIndex+1
                } else {
                    cell.actor.removeActor(r1)
                    cell.actor.removeActor(r2)
                    cell.actor.removeActor(r3)
                    cell.actor.removeActor(r4)
                    objects[0].walking = 0
                }
                window.window.pack()
                window.window.width = AbilityWindow.width
            }

        })
        window.table.row()
    }


}