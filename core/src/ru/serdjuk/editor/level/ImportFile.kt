package ru.serdjuk.editor.level

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.kotcrab.vis.ui.widget.file.StreamingFileChooserListener
import ru.serdjuk.editor.layer.Layer
import ru.serdjuk.editor.sprites.SpriteSheet

@ExperimentalUnsignedTypes
object ImportFile : FileChooser(Mode.OPEN) {


    fun load(stage: Stage, layer: Layer, spriteSheet: SpriteSheet) {
        name = javaClass.simpleName
        if (stage.root.children.any { it.name == name }) return

//        setDefaultPrefsName("ru.retro.gfxeditor.game.importBin") //  так и не понял нахуя это надо ? и чета она опять начала варнинг кидать сука
//        setDirectory(preference.getString(LOAD_PATH))
        setSaveLastDirectory(true)
        selectionMode = SelectionMode.FILES
        isResizable = false
        val listener = object : StreamingFileChooserListener() {
            override fun selected(file: FileHandle?) {
                saveLastPath(this@ImportFile)
                if (file != null) {
                    LevelSpecification.convertToPC(layer, file, spriteSheet)

                } else {
                    throw Exception("No file")
                }
            }

            override fun canceled() {
                saveLastPath(this@ImportFile)
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