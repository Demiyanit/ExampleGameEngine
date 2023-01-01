package example.scenes;

import example.Window;

public class LevelScene extends Scene{
    public LevelScene() {
        System.out.println("Inside lvl scene");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }

    @Override
    public void update(float dt) {

    }
}
