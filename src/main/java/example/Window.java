package example;

import example.listeners.KeyListener;
import example.listeners.MouseListener;
import example.scenes.LevelEditorScene;
import example.scenes.LevelScene;
import example.scenes.Scene;
import example.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String  title;
    private long glfwWindow;

    public float r, g, b, a;

    private static Window window = null;

    private static Scene currentScene;

    private Window() {
       this.width = 800;
       this.height = 600;
       this.title = "Test engine";
       r = 1;
       g = 1;
       b = 1;
       a = 1;
    }

    public static void changeScene(int newScene) {
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("Example engine is starting...    the version of LWGL is:" + Version.getVersion() + "!");

        init();
        loop();

        //Free the memory
        //Делаем переодическую очистку памяти от лишнего
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and free the error callback
        //Выключаем GLFW и очищаем логи ошибок
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup an Error callback
        // Создаём систему отчёта об ошибках
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        //Инициализируем GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to  initialize GLFW");
        }

        //Configure GLFW
        //Настраиваем GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the window
        //Создаём окно
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window!!!");
        }


        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //Make the OpenGL context current
        //...
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        //Включить v-sync
        int VsyncState = 1;
        glfwSwapInterval(VsyncState);

        //Make the window visible
        //Сделать окно видимым
        glfwShowWindow(glfwWindow);

        //Важная штука но текста дофига
        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        while (!glfwWindowShouldClose(glfwWindow)) {
            //Poll events
            //Записываем события
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt > 0) {
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}