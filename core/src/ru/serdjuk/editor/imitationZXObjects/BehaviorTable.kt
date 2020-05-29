package ru.serdjuk.editor.imitationZXObjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.*
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel
import com.kotcrab.vis.ui.widget.spinner.Spinner
import ru.serdjuk.editor.cellSize


class BehaviorTable(private val stg: Stage) : VisWindow("Behaviors: ") {


    private val enable = "Enable"
    private val disable = "Disable"
    private val yes = VisLabel("+").also { it.color = Color.GREEN }
    private val no = VisLabel("-").also { it.color = Color.RED }
    private val itemColor = VisLabel("Color: ")
    private val table = VisTable()
    private val checks = ButtonGroup<VisCheckBox>()

    private val toggle = VisCheckBox("Toggle: ")
    private val danger = VisCheckBox("Danger: ")

    private val toggleValue = VisLabel(disable)
    private val dangerValue = VisLabel(disable)

    private val colorTexture = Texture(Pixmap(cellSize, cellSize, Pixmap.Format.RGB565).also {
        it.setColor(Color.WHITE)
        it.fill()
    })

    fun install(zxObject: ZXObject) {
        stage = stg
        if (stage.root.children.any { it.name == javaClass.simpleName }) return
        name = javaClass.simpleName
        isModal = true
        closeOnEscape()
        addCloseButton()
        create(zxObject)
        stage.addActor(this)
    }

    private fun create(zxObject: ZXObject) {
        table.add(VisLabel("Position: X(${zxObject.x}), Y(${zxObject.y})")).align(Align.left)
        table.add()
        table.row()

        table.add("Name: ").align(Align.left)
        table.add(VisLabel(zxObject.name).also { it.color = Color.GREEN }).align(Align.left)
        table.row()

        table.add("ID:").align(Align.left)
        table.add(VisLabel(zxObject.id.toString()).also { it.color = Color.GREEN }).align(Align.left)
        table.row()

        patency(zxObject)
        toggle(zxObject)
        danger(zxObject)
        color(zxObject)
        driven(zxObject)
        table.pack()
        add(table)
        pack()
        setPosition(Gdx.input.x.toFloat(), (Gdx.graphics.height - Gdx.input.y).toFloat())
    }

    private fun driven(zxObject: ZXObject) {
        val driven = VisCheckBox("Driven:")
        driven.isChecked = zxObject.driven
        val value = if (zxObject.driven) yes else no
        table.add(driven).align(Align.left)
        val cell = table.add(value).align(Align.left)
        driven.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                zxObject.driven = !zxObject.driven
                driven.isChecked = zxObject.driven
                cell.clearActor()
                cell.setActor(if (zxObject.driven) yes else no)
            }
        })

        table.row()
    }

    private fun color(zxObject: ZXObject) {
        val horizontal = VisTable()
        horizontal.add(createItemColor(Color.BLUE, zxObject))
        horizontal.add(createItemColor(Color.RED, zxObject))
        horizontal.add(createItemColor(Color.YELLOW, zxObject))
        horizontal.add(createItemColor(Color.WHITE, zxObject))
        horizontal.add(createItemColor(Color.PURPLE, zxObject))
        horizontal.add(createItemColor(Color.GREEN, zxObject))
        horizontal.add(createItemColor(Color.CYAN, zxObject))
        val scroll = VisScrollPane(horizontal)
        scroll.setupOverscroll(0f, 0.1f, 3f)
        scroll.setupFadeScrollBars(0f, 0f)
        scroll.setScrollingDisabled(false, true)
//        add(scroll).left().width(16f).height(32f)


        table.add(itemColor).align(Align.left)
        table.add(scroll).align(Align.left).width(64f)
        table.row()

        itemColor.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
//                palette(itemColor.x,itemColor.y)
            }

        })
    }

    //  проходимость
    private fun patency(zxObject: ZXObject) {
        val patency = VisCheckBox("Проходимость:")
        patency.isChecked = zxObject.through
        val value = if (zxObject.through) yes else no
        table.add(patency).align(Align.left)
        val cell = table.add(value).align(Align.left)
        patency.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                zxObject.through = !zxObject.through
                patency.isChecked = zxObject.through
                cell.clearActor()
                cell.setActor(if (zxObject.through) yes else no)
            }
        })

        table.row()
    }

    private fun danger(zxObject: ZXObject) {
        val danger = VisCheckBox("Danger")
        danger.isChecked = zxObject.dangerous != 0
        val value: Actor = if (danger.isChecked || zxObject.dangerous != 0) {
            spinner(zxObject)
        } else {
            VisLabel(disable).also { it.color = Color.RED }
        }
        table.add(danger).align(Align.left)
        val cell = table.add(value).align(Align.left)

        danger.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val v: Actor = if (danger.isChecked) {
                    spinner(zxObject)
                } else {
                    VisLabel(disable).also { it.color = Color.RED }
                }
                cell.clearActor()
                cell.setActor(v)
            }

        })
        table.row()
    }

    private fun toggle(zxObject: ZXObject) {
        toggleValue.setText(if (zxObject.isToggle) enable else disable)
        toggleValue.color = if (zxObject.isToggle) Color.GREEN else Color.RED
        val checkBox = VisCheckBox("Toggle: ")
        checkBox.isChecked = zxObject.isToggle
        checkBox.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                zxObject.isToggle = !zxObject.isToggle
                toggleValue.setText(if (zxObject.isToggle) enable else disable)
                toggleValue.color = if (zxObject.isToggle) Color.GREEN else Color.RED
            }
        })
        table.add(checkBox).align(Align.left)
        table.add(toggleValue).align(Align.left)
        table.row()
    }


    private fun spinner(zxObject: ZXObject): Spinner {
        val model = IntSpinnerModel(zxObject.dangerous, 0, 255)
        val spinner = Spinner("", model)
        spinner.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                zxObject.dangerous = model.value
            }
        })
        return spinner
    }


    private fun createItemColor(color: Color, zxObject: ZXObject): VisImageButton {
        val image = Image(Texture(Pixmap(cellSize, cellSize, Pixmap.Format.RGB565).also {
            it.setColor(color)
            it.fill()
        }))
        val button = VisImageButton(image.drawable)
        button.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                zxObject.color = color
            }

        })
        return button
    }

    fun dispose() {
    }
}