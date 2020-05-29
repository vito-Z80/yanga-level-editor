package ru.serdjuk.editor.level

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import ru.serdjuk.editor.currentInc
import ru.serdjuk.editor.currentPaper
import ru.serdjuk.editor.imitationZXObjects.ZXObject
import ru.serdjuk.editor.layer.Layer
import ru.serdjuk.editor.sprites.SpriteSheet
import kotlin.experimental.and

@ExperimentalUnsignedTypes
object LevelSpecification {

    private const val find = "\t[0-9]{1,3}\t"


    fun convertToPC(layer: Layer, file: FileHandle, spriteSheet: SpriteSheet) {
        layer.getLayer().clear()
        val level = file.readBytes()
        val textures = spriteSheet.textures.toSortedMap()
        var y = -1f
        val x = 0f
        level.forEachIndexed { id, byte ->
            if (id < 150) {
                if (id % 15 == 0) y++
                if (byte != 0.toByte()) {
                    val sprite = ZXObject()
                    sprite.y = y
                    sprite.x = (id % 15).toFloat()
                    val cell = (sprite.y * 15 + sprite.x).toInt()
                    val fileNames = spriteSheet.files.listFiles()?.map { it.nameWithoutExtension }
                    val name = fileNames?.get(byte.toUByte().toInt())
                    println(name)
                    if (level.size == 150) {
                        sprite.setRegion(spriteSheet.textures[name])
                        layer.colors[cell] = 7
                    } else {
                        val inc = (level[id + 150] and 7).toInt()
                        val paper = (level[id + 150].toInt() shr 3) and 7
                        currentInc = getColor(inc)
                        currentPaper = getColor(paper)
                        sprite.setRegion(spriteSheet.fromWBM("sprites/${name}.wbm", true))
                        sprite.zxColor = inc or (paper shl 3)
                        layer.colors[cell] = sprite.zxColor.toByte()
                    }
                    sprite.setSize(1f, 1f)
                    sprite.name = name!!
                    sprite.textureName = name
                    layer.getLayer().add(sprite)
                }
            }
        }


//
//        val values = Regex("\t[0-9]{1,3}\t").findAll(file.readString()).map { it.value.trim().toInt() }.toList()
//        println(values[0])
//        for (i in 1..(values[0] * 3) step 3) {
//            val sprite = ZXObject()
//            sprite.x = (values[i] shr 4 and 0b00001111).toFloat()
//            sprite.y = (values[i] and 0b00001111).toFloat()
//            sprite.through = values[i + 1] and 0b00000001 != 0
//            sprite.walking = values[i + 1] and 0b00011110
//            sprite.color = getColor(values[i + 1] shr 5 and 0b00000111)
//            println("${values[i]}, ${sprite.x}, ${sprite.y}")
//            val name = spriteSheet.files.list()[values[i + 2]].split(".")[0]
//            sprite.setRegion(spriteSheet.textures[name])
//            sprite.setSize(1f, 1f)
//            layer.add(sprite)
//        }


//        objects.forEach {
//
//            while (it.iterator().hasNext()) {
//                val v = it.iterator().next()
//                val sprite = ZXObject()
//                sprite.x = (v shr 4 and 0x00001111).toFloat()
//                sprite.y = (v and 0x00001111).toFloat()
//                sprite.color
//
//                sprite.setSize(1f, 1f)
//            }
//
//            it.forEach { v ->
//                val sprite = ZXObject()
//                sprite.x = (v shr 4 and 0x00001111).toFloat()
//                sprite.y = (v and 0x00001111).toFloat()
//                sprite.color
//
//                sprite.setSize(1f, 1f)
//            }
//        }


    }


    private fun getColor(color: Int) = when (color) {
        1 -> Color.BLUE
        2 -> Color.RED
        3 -> Color.PURPLE
        4 -> Color.GREEN
        5 -> Color.CYAN
        6 -> Color.YELLOW
        7 -> Color.WHITE
        else -> Color.BLACK
    }

}