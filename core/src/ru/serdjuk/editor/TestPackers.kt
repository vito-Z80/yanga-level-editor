package ru.serdjuk.editor

import java.io.File


var address = 0
val levelsPath = "C:/ZX/Projects/YangaV3.0/data/levels/"
val fileNames = File(levelsPath).list { dir: File, name: String ->
    name.contains(".bin")
}

//val files = File(levelsPath).listFiles().filter { it.extension == "bin" }
fun main() {
    println("All files length: ${300 * fileNames!!.size}")

    MlzPack().pack()
    ApLibPack().pack()
    ExomizerPack().pack()

    println()
}

class ExomizerPack {
    // exomizer.exe raw -c inputfile -o [outputfile.exo_simple]
    var levelsPack = ArrayList<Byte>()
    var byteAddr = 0
    val mapData = ArrayList<String>()
    fun pack() {
        levelsPack.clear()
        mapData.add("levelsMap:\n\tdw $byteAddr")
        fileNames?.forEach {
            val newFile = it.split(".")[0] + ".exm"
            ProcessBuilder("C:/ZX/Addons/exomizer.exe", "raw", "-c", "$levelsPath$it", "-o", "$levelsPath$newFile").start().waitFor()
            val f = File("$levelsPath$newFile")
            val b = f.readBytes()
            levelsPack.addAll(b.asSequence())
            byteAddr += f.length().toInt()
            mapData.add("$byteAddr")
            f.delete()
        }
        println("Exomizer 2: ${levelsPack.size}")
        File("${levelsPath}levelsPack.exm").writeBytes(levelsPack.toByteArray())
        File("${levelsPath}levelsMap.exm.asm").writeText(mapData.toString().removePrefix("[").removeSuffix("]"))
        println("levels number: ${mapData.size}")
        // 4839
    }
}

class ApLibPack {
    var levelsPack = ArrayList<Byte>()
    var byteAddr = 0
    val mapData = ArrayList<String>()
    fun pack() {
        levelsPack.clear()
        mapData.add("levelsMap:\n\tdw $byteAddr")
        fileNames?.forEach {
            val newFile = it.split(".")[0] + ".aplib"
            ProcessBuilder("C:/ZX/Addons/aplib_pack2.exe", "$levelsPath$it", "$levelsPath$newFile").start().waitFor()
            val f = File("$levelsPath$newFile")
            val b = f.readBytes()
            levelsPack.addAll(b.asSequence())
            byteAddr += f.length().toInt()
            mapData.add("$byteAddr")
            f.delete()
        }
        println("ApLib: ${levelsPack.size}")
        File("${levelsPath}levelsData.aplib").writeBytes(levelsPack.toByteArray())
        File("${levelsPath}levelsMap.aplib.asm").writeText(mapData.toString().removePrefix("[").removeSuffix("]"))
        println("levels number: ${mapData.size}")
        // 4706
    }
}

class MlzPack {
    var levelsPack = ArrayList<Byte>()
    var byteAddr = 0
    val mapData = ArrayList<String>()
    fun pack() {
        levelsPack.clear()
        mapData.add("levelsMap:\n\tdw $byteAddr")
        fileNames?.forEach {
            val newFile = it.split(".")[0] + ".mlz"
            ProcessBuilder("C:/ZX/Addons/mhmt.exe", "-mlz", "$levelsPath$it", "$levelsPath$newFile").start().waitFor()
            val f = File("$levelsPath$newFile")
            val b = f.readBytes()
            levelsPack.addAll(b.asSequence())
            byteAddr += f.length().toInt()
            mapData.add("$byteAddr")
            f.delete()
        }
        println("MegaLZ: ${levelsPack.size}")
        File("${levelsPath}levelsData.mlz").writeBytes(levelsPack.toByteArray())
        File("${levelsPath}levelsMap.mlz.asm").writeText(mapData.toString().removePrefix("[").removeSuffix("]"))
        println("levels number: ${mapData.size}")
        // 4863
    }
}


