package ru.serdjuk.editor.sprites

import com.badlogic.gdx.Gdx
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
import ru.serdjuk.editor.Tools
import ru.serdjuk.editor.currentInc
import ru.serdjuk.editor.currentPaper
import ru.serdjuk.editor.imitationZXObjects.ZXObject
import ru.serdjuk.editor.selectedTool
import java.io.File

class SpriteSheet : VisWindow("Sprite sheets:") {

    val files = File("C:\\Projects\\Libgdx\\zx\\yanga level editor\\core\\assets\\sprites")
    val textures = HashMap<String, Texture>()
    private val size = 16
    private val table = VisTable()
    var selected = "empty"
    private val selectedName = VisLabel("Name: $selected").also {
        it.setEllipsis(false)
        it.setWrap(true)
        it.color = Color.GREEN
    }

    val selectId = VisLabel("")

    //    val sprites = HashMap<String, ZXObject>()
    fun install(stage: Stage) {
        createTextures()
        addItems()
        centerWindow()
        row()
        add("-----Selected-----").center()
        row()
        addValues()
//        add("-----Selected-----").center().row()
        pack()
//        debug = true
        stage.addActor(this)
    }

    fun getSelectedTexture() = textures[selected]!!

    private fun createTextures() {
        val names = files.list()
        names?.forEach {
            textures[it?.substringBefore(".")!!] = fromWBM("sprites/${it}")
        }
        textures["empty"] = Texture(Pixmap(size, size, Pixmap.Format.RGBA8888))
    }


     fun fromWBM(fileName: String, setColor: Boolean = false): Texture {
        //  first 4 bytes (x,y,width,height)
        val offset = 4
        val bytes = Gdx.files.internal(fileName).readBytes()
        val width = bytes[2].toInt()
        val height = bytes[3].toInt()
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        repeat(height) { y ->
            repeat(width / 8) { x ->
                val id = (y * (width / 8)) + x + offset
                draw8Bit(pixmap, bytes[id], x * 8, y, setColor)
            }
        }
        return Texture(pixmap)
    }

    private fun draw8Bit(pixmap: Pixmap, byte: Byte, offsetX: Int, y: Int, innerColor: Boolean) {
        repeat(8) {
            val bit = (byte.toInt() shr (7 - it)) and 0b00000001
            val color = if (bit != 0) {
                if (innerColor) {
                    currentInc
                } else {
                    Color.WHITE
                }
            } else {
                if (innerColor) {
                    currentPaper
                } else {
                    Color.BLACK
                }
            }
            pixmap.setColor(color)
            pixmap.drawPixel(offsetX + it, y)
        }
    }




    private fun addValues() {
        val color = VisLabel("")
        val size = VisLabel("Size: ")
        val selectID = VisLabel("Select ID: ")
        add(selectedName).left().fillX()
        row()
        add(size).left()
        add("${textures.size}").left()
        row()
        add(selectID).left()
        add(this.selectId)
        row()
    }

    private fun addItems() {
        var counter = 0
        textures.toSortedMap().forEach {
            val key = it.key
            val sprite = it.value
            table.add(VisImageButton(SpriteDrawable(Sprite(textures[key]).also { s ->
                s.setSize(size * 4f, size * 4f)
            })).also { button ->
                button.addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        selected = it.key
                        selectedName.setText(selected)
                        selectedName.setAlignment(Align.left)
                        selectedName.setEllipsis(true)
                        selectedName.setWrap(true)
                        selectedName.pack()
                        selectedTool = Tools.PENCIL
                        val sid = textures.toSortedMap().values.indexOf(textures[it.key])
                        selectId.setText(sid)
                    }
                })

            })
            if (counter % 4 == 0) table.row()
            counter++
        }
//        table.pack()
        scroll()
    }

    fun setValues(zxObject: ZXObject) {
        when {
            selected == "hero" -> {
                zxObject.through = false
            }
            selected == "sword" || selected == "cat" -> {
                zxObject.through = true
                zxObject.dangerous = 2
            }
            selected == "enemy" -> {
                zxObject.through = false
            }
            selected == "empty" -> {
                zxObject.through = true
            }
            selected.toLowerCase().contains("wall") -> {
                zxObject.through = false
            }
            selected == "rune" -> {
                zxObject.through = false
                zxObject.driven = true
            }
            selected == "toggle" -> {
                zxObject.through = true
                zxObject.isToggle = true
            }
            selected == "exit" -> {
                zxObject.through = true
            }
            selected == "rope" -> {
                zxObject.through = true
            }

            selected == "fire" -> {     //  при вхождении в огонь наносит урон входящему
                zxObject.through = true
                zxObject.dangerous = 2
            }

//            else -> throw Exception("${javaClass.name} ERROR: Unknown texture name.")
        }
        zxObject.name = selected
        zxObject.preX = zxObject.x.toInt()
        zxObject.preY = zxObject.y.toInt()
        zxObject.dstX = zxObject.x.toInt()
        zxObject.dstY = zxObject.y.toInt()
    }

    private fun scroll() {
        val scroll = VisScrollPane(table)
        scroll.setupOverscroll(0f, 0.1f, 3f)
        scroll.setupFadeScrollBars(0f, 0f)
        add(scroll).center().width(300f).height(512f)
    }

}

