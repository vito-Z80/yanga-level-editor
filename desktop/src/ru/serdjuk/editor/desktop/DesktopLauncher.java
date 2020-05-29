package ru.serdjuk.editor.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.sun.scenario.Settings;
import ru.serdjuk.editor.Yle;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setResizable(true);
        config.setTitle("Retro editor");
        config.useVsync(true);
        config.setWindowedMode(800, 600);
        config.setWindowSizeLimits(800, 600, 9999, 9999);
        config.useOpenGL3(true, 3, 2);

//        Settings settings = new Settings();

        new Lwjgl3Application(new Yle(), config);
    }
}



