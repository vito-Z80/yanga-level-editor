package ru.serdjuk.editor

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import kotlin.experimental.xor
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


fun xorFUCK(text:String):StringBuilder {
    val xorResult = StringBuilder()
    text.forEach {
        val xorFy = it.toByte().toUByte() xor 'Y'.toByte().toUByte()
        xorResult.append("$xorFy,")
    }
    return xorResult
}

fun main() {
    val fy = "FUCKYANDEX"
    val xor1 = xorFUCK(fy)
//    val xor2 = xorFUCK(xor1.toString())
    println(fy)
    fy.forEach { print("$it,") }
    println()
    println(xor1)
//    println(xor2)

    return

    val bytes = byteArrayOf(
            0x7f,
            0xbf.toByte(),
            0xdf.toByte(),
            0xef.toByte(),
            0xf7.toByte(),
            0xfb.toByte(),
            0xfd.toByte(),
            0xfe.toByte()
    )


    bytes.forEach { println(it.toUByte().toString(2)) }

    return

    repeat(33) {
        var r = it
        r = r and 30
        val c = r and  1
        r =r xor c
        r = r or 1
        println("$it: $r: ${r.toString(2)}")

    }




    return


    println(6.toString(2))
    println(22.toString(2))
    println(23.toString(2))
    println((7 and 22 and 23).toString(2))
    println()

    println(32.toString(2))
    println(33.toString(2))
    println(48.toString(2))
    println((32 and 33 and 48).toString(2))

    println()


    println(7.toString(2))
    println(22.toString(2))
    println(37.toString(2))
    println((7 and 22 and 37).toString(2))

    println()
    println(10.toString(2))
    println(13.toString(2))
    println(12.toString(2))
    println((10 and 13 and 12).toString(2))

    println()
    println(0x6c)
    println(0x5e)
    println(0x6d)


    println()
    println(109-108)
    println(109-94)


    println()
    println(94-108)
    println(94-109)


    println()
    println(112-111)
    println(112-107)
    println()
    println(-14-15)
    println(1-2)
    println(1-15)
    return

    //  x
    repeat(256) {
        print("${(it shr 4) and 0b00001111}, ")
    }

    println()

    //  y
    repeat(160) {
        print("${it and 0b11110000}, ")

    }

    return
    var time = 0f
    var t2 = 0.5f
    repeat(255) {
        var c = (sin(time) * 148).toInt()
        c = abs(c)
        if (it and 0b00001111 == 0) {
            println()
            print("db ")
            print("$c, ")
        } else {
            if (it and 0b00001111 == 0b00001111)
                print("$c")
            else
                print("$c, ")
        }
        time += 0.00390625f/4
        t2 += 0.079f
    }


}

interface DrawObject {
    fun drawObject()
}

abstract class Render : DrawObject {

    fun draw() {
        drawObject()
    }

}

class Rect : Render() {
    private val rectangle = Rectangle()
    override fun drawObject() {
        println(rectangle)
    }
}

class Circ : Render() {
    private val circle = Circle()
    override fun drawObject() {
        println(circle)
    }

}







