package ru.serdjuk.editor.level

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter
import com.kotcrab.vis.ui.widget.file.FileChooserListener
import com.kotcrab.vis.ui.widget.file.StreamingFileChooserListener
import ru.serdjuk.editor.imitationZXObjects.ZXObject
import ru.serdjuk.editor.layer.Layer
import ru.serdjuk.editor.sprites.SpriteSheet
import java.io.File

@ExperimentalUnsignedTypes
object ExportFile : FileChooser(Mode.SAVE) {


    fun save(stage: Stage, layer: Layer, spriteSheet: SpriteSheet) {
        name = javaClass.simpleName
        if (stage.root.children.any { it.name == name }) return

//        setDefaultPrefsName("ru.retro.gfxeditor.game.importBin") //  так и не понял нахуя это надо ? и чета она опять начала варнинг кидать сука
//        setDirectory(preference.getString(LOAD_PATH))
        setSaveLastDirectory(true)
        selectionMode = SelectionMode.FILES
        isResizable = false
        val listener = object : StreamingFileChooserListener() {

            val indexesOnly = ByteArray(150) { _ -> (0).toByte() }
            override fun selected(file: FileHandle?) {
                saveLastPath(this@ExportFile)
                if (file != null) {
                    val names = spriteSheet.files.listFiles()?.map { it.nameWithoutExtension }
                    layer.getLayer().forEach {
                        val cell = it.y.toInt() * 15 + it.x.toInt()
                        val id = names?.indexOf(it.name)?.toByte() ?: (0).toByte()
//            val id = canvas.spriteSheet.files.listFiles()?.indexOfFirst { name -> name.nameWithoutExtension == it.name }
                        indexesOnly[cell] = id
                        println("$cell, $id")
                    }
//                    val indexesLvlFile = File("E:\\YangaV2\\YangaV3.0\\data\\levels\\level_indexes_test.bin")
                    file.writeBytes(indexesOnly + layer.colors, false)




//                    layer.createStorageAndRelated()






                } else {
                    throw Exception("No file")
                }
            }

            override fun canceled() {
                saveLastPath(this@ExportFile)
                super.canceled()
            }
        }
        setListener(listener)
        stage.addActor(this)
    }


    private fun saveLastPath(loadFiles: FileChooser) {
//        preference.putString(LOAD_PATH, loadFiles.currentDirectory.path()).flush()
    }
}