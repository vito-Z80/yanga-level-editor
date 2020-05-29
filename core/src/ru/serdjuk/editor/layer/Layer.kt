package ru.serdjuk.editor.layer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Stage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.serdjuk.editor.*
import ru.serdjuk.editor.canvas.Canvas
import ru.serdjuk.editor.layer.abilityWindow.AbilityWindow
import ru.serdjuk.editor.imitationZXObjects.ZXObject
import ru.serdjuk.editor.level.ExportFile
import ru.serdjuk.editor.level.ImportFile
import java.io.File
import java.nio.charset.Charset
import kotlin.math.floor
import kotlin.math.sin

@ExperimentalUnsignedTypes
class Layer(val canvas: Canvas) {

    var isGridVisible = true
    var isVisible = true
    var id = 0
    val selection = Rectangle()
    private val layer = ArrayList<ZXObject>()
    val colors = ByteArray(150) { 7 }


    private val mapIndicesWindow = MapIndicesWindow(canvas).also {
        //        it.install()
    }

    fun getLayer() = layer


    fun update(stage: Stage) {

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            ImportFile.load(stage, this, canvas.spriteSheet)
        }
        //---------------------------------
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.S)) {

            GlobalScope.launch {
                println("saving...")
//                asm()
//                createStorageAndRelated()
                ExportFile.save(stage, this@Layer, canvas.spriteSheet)
//                println("Save complete !")
            }
//                LevelConverter.toZx(layer)
        }
        //----------------------------
        mapIndicesWindow.isVisible = isVisible
        mapIndicesWindow.window.isVisible = isVisible
        refreshMouse(canvas.viewport)
        if (isVisible) {
            if (Gdx.input.isButtonJustPressed(1)) {
                abilitiesWindow(stage)
            }

        }
    }

    private fun abilitiesWindow(stage: Stage) {


        when (selectedTool) {
            Tools.PENCIL -> {
                val loner = layer.find { it.boundingRectangle.contains(mouse.x, mouse.y) }
                if (loner != null) {
                    AbilityWindow().show(stage, listOf(loner))
                }
            }
            Tools.SELECTION -> {
                val objects = layer.filter { selection.overlaps(it.boundingRectangle) }
                if (objects.isNotEmpty()) {
                    AbilityWindow().show(stage, objects)
                } else {
                    val loner = layer.find { it.boundingRectangle.contains(mouse.x, mouse.y) }
                    if (loner != null) {
                        AbilityWindow().show(stage, listOf(loner))
                    }
                }
            }
            Tools.FILL -> {


            }
            else -> Unit
        }

    }


    fun rudeLevel() {

    }

    /*
    color
        0 - black
        1 - blue
        2 - red
        3 - purple
        4 - green
        5 - cyan
        6 - yellow
        7 - white
     */

    fun asm() {
        println("Objects in level: ${layer.count()}")
        val result = StringBuilder()
        val indexesOnly = ByteArray(150) { i -> (0).toByte() }
        val storage = "storage."
        result.append("level_test\n\n")
        result.append("\tdb\t").append(layer.count { it.name != "empty" }).append("\t; количество объектов\\байт: " +
                "${layer.count { it.name != "empty" } * 4}\n\n")
        layer.forEach {
            if (it.name != "empty") {
                result.append("\tdb\t").append(cellToByte(it)).append("\t//\tfirst 4 bits column, second 4 bits row\n")
                result.append("\tdb\t").append("${it.through.asInt() or getColor(it) or (it.walking shl 1)}"
                ).append("\t//\tbit 0 through, bits 1-4 walking, bits 5-7 color\n")
                result.append("\tdb\t").append(
                        canvas.spriteSheet.files.listFiles()?.indexOfFirst { fileName -> fileName.nameWithoutExtension == it.name })
                        .append("\t//\tsprite id\n\n")
            }
        }

        val names = canvas.spriteSheet.files.listFiles()?.map { it.nameWithoutExtension }
        layer.forEach {
            val cell = it.y.toInt() * 15 + it.x.toInt()
            val id = names?.indexOf(it.name)
//            val id = canvas.spriteSheet.files.listFiles()?.indexOfFirst { name -> name.nameWithoutExtension == it.name }
            indexesOnly[cell] = id?.toByte() ?: (0).toByte()
            println("$cell, $id")
        }

        val indexesLvlFile = File("E:\\YangaV2\\YangaV3.0\\data\\levels\\level_indexes_test.bin")
        indexesLvlFile.writeBytes(indexesOnly)

        val file = File("E:\\YangaV2\\YangaV3.0\\data\\levels\\level_test6.asm")
        file.writeText(result.toString(), Charset.defaultCharset())
    }

    private fun cellToByte(zxObject: ZXObject) = zxObject.x.toInt() shl 4 or zxObject.y.toInt()


    private fun getColor(zxObject: ZXObject) = when (zxObject.color.toIntBits()) {
        Color.BLACK.toIntBits() -> 0
        Color.BLUE.toIntBits() -> 1
        Color.RED.toIntBits() -> 2
        Color.PURPLE.toIntBits() -> 3
        Color.GREEN.toIntBits() -> 4
        Color.CYAN.toIntBits() -> 5
        Color.YELLOW.toIntBits() -> 6
        Color.WHITE.toIntBits() -> 7
        else -> 4
    } shl 5


    fun createStorageAndRelated() {
        val staticSpritesFolder = File("E:\\YangaV2\\YangaV3.0\\data\\sprites\\static\\").absolutePath
        val animatesSpritesFolder = File("E:\\YangaV2\\YangaV3.0\\data\\sprites\\animated\\").absolutePath
        val storageCode = File("E:\\YangaV2\\YangaV3.0\\storage.asm")
        //  copy sprites from assets to Yanga
        val files = File("core/assets/sprites").listFiles()
        files?.forEach {
            if (it.length() == 36L) {
                val saveFile = File(staticSpritesFolder, it.name)
                saveFile.createNewFile()
                saveFile.writeBytes(it.readBytes().drop(4).toByteArray())
            } else {
                val saveFile = File(animatesSpritesFolder, it.name)
                saveFile.createNewFile()
                saveFile.writeBytes(it.readBytes().drop(4).toByteArray())
            }
        }
        //  create storage file
        val fileText = StringBuilder()
        val indexes = StringBuilder()
        indexes.append("\n\n\tmodule indexes\n")
        fileText.append("\tmodule storage\n")
        fileText.append("sprites32\n")
        files?.forEachIndexed { id, file ->
            val name = file.nameWithoutExtension.replace("1_", "").replace("0_", "")
            fileText.append("${name}\tincbin \"data/sprites/static/${file.name}\"\n")
            indexes.append("${name}ID\tequ\t${id}\n")
        }
        fileText.append("\tendmodule\n")
        indexes.append("\tendmodule\n")
        fileText.append(indexes)
        storageCode.createNewFile()
        storageCode.writeText(fileText.toString(), Charset.defaultCharset())
    }

    fun draw(batch: SpriteBatch) {
        if (isVisible)
            layer.forEach {
                if (it.name != "empty") {
                    it.setFlip(false, true)
                    it.draw(batch)
                }
            }
    }


    var time = 0f
    fun drawShapes(shape: ShapeRenderer) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) isGridVisible = !isGridVisible
        time += 0.12f
        if (isGridVisible) {
            val c = 0.5f + sin(time) / 2f
            //  grid
            shape.color = Color.DARK_GRAY
            repeat(worldHeight.toInt()) { y ->
                repeat(worldWidth.toInt()) { x ->
                    shape.rect(x.toFloat(), y.toFloat(), 1f, 1f)
                }
            }
            //  selected
            if (!mouseInActor && canvas.bounds.contains(mouse.x, mouse.y)) {
                shape.setColor(1f, 0f, 1f, c)
                shape.rect(floor(mouse.x), floor(mouse.y), 1f, 1f)
            }
        }
        shape.color = Color.GREEN.also { it.a = 0.5f }
        shape.rect(selection.x, selection.y, selection.width, selection.height)
//        selectionArea(shape)
    }

}