package ru.serdjuk.editor

import java.io.File
import java.io.FileFilter

//  RLE compression


fun main() {
    val compression = Compression()
    compression.compression()

//    val c= CompressionPath()
//    println(c.allFilesLength)
//    println(c.allFilesCompressedLength)
//    val r = c.allFilesLength-c.allFilesCompressedLength
//    println(" = $r ")
}

class CompressionPath : Compression() {

    var fullSize = 0
    val files = File("C:\\ZX\\Projects\\YangaV3.0\\data\\levels").listFiles(FileFilter { it.extension == "bin" }).also {
        it.forEach { fullSize += it.readBytes().size }
    }


    init {
        var counter = 0
        files.forEach { f ->
            allFilesLength +=f.readBytes().size
            id = 0
            result.clear()
            bytes = f.readBytes()
            compression()
            allFilesCompressedLength += result.size
//            f.readBytes().forEach { b ->
//                bytes[counter++] = b
//            }
        }
//        compression()
    }
}


open class Compression {
    var path = File("").absolutePath
    var file = File("$path/core/assets/lvl_001.bin")
    val result = ArrayList<Byte>()
    var id = 0
    open var bytes = file.readBytes()

    var allFilesLength = 0
    var allFilesCompressedLength = 0

    fun compression() {
        while (id <= bytes.size - 1) {
            val i = id
            identical()
            if (id >= bytes.size - 1) {
                break
            }
            various()
            if (id >= bytes.size - 1) break
            if (i == id) id++
        }
        //------show
//        println(bytes.size)
//        bytes.forEach { print("${it.toUByte()},") }
//        println()
//        result.forEach { print("${it.toUByte()},") }
//        println()
        println("result: ${result.size}")
    }

    private fun identical() {
        var i = id
        var counter = 0
        while (bytes[i] == bytes[i + 1]) {
            counter++
            i++
            if (i >= bytes.size - 1) {
                break
            }
        }
        if (counter > 0) {
            result.add(((128 or ++counter).toByte()))
            result.add(bytes[i])
        }
        if (counter > 0) id = ++i
    }

    private fun various() {
        var i = id
        var counter = 0
        val v = ArrayList<Byte>()
        while (bytes[i] != bytes[i + 1]) {
            v.add(bytes[i])
            counter++
            i++
            if (i >= bytes.size - 1) break
        }
        if (counter > 0) {
            result.add(((counter).toByte()))
            result.addAll(v)
        }
        if (counter > 0) id = i
    }

}


