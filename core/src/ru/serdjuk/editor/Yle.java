package ru.serdjuk.editor;

import com.badlogic.gdx.Game;

public class Yle extends Game {

    @Override
    public void create() {
        Editor editor = new Editor();
		setScreen(editor);
    }

}
