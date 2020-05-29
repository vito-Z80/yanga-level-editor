package ru.serdjuk.editor.imitationZXObjects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite

class ZXObject : Sprite() {

    var zxColor = 0
    var tmpPixmap = Pixmap(16,16,Pixmap.Format.RGB565)
    var textureName = ""
    var preX = 0
    var preY = 0
    var dstX = 0
    var dstY = 0
    var dstX2 = 0
    var dstY2 = 0


    var dangerous = 0
    var isToggle = false

    var through = false
    var driven = false
    /*
    параметр отвечает за перемещение объекта такого как враг
         0 - не шагающий объект
         1 - по горизонтали
         2 - по вертикали
         3 - рандомно
         4 - умный
     */
    var walking = 0
    var vulnerable = false
    var isVisible = true

    var id = -1
    var name = ""


}
