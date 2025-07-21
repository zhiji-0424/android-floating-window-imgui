#include "imgui.h"
#include "imgui_impl_android.h"
#include "imgui_impl_opengl3.h"
#include <android/native_window.h>
#include <android/log.h>
#include <EGL/egl.h>
#include <GLES3/gl3.h>
#include <string>
#include <thread>

// Data
static EGLDisplay           g_EglDisplay = EGL_NO_DISPLAY;
static EGLSurface           g_EglSurface = EGL_NO_SURFACE;
static EGLContext           g_EglContext = EGL_NO_CONTEXT;
static EGLConfig            mEglConfig;
static ANativeWindow       *window;
static bool                 g_Initialized = false;
static char                 g_LogTag[] = "ImGuiExample";
static std::string          g_IniFilename = "";



/*static int32_t handleInputEvent(struct android_app* app, AInputEvent* inputEvent)
{
    return ImGui_ImplAndroid_HandleInputEvent(inputEvent);
}*/

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,g_LogTag ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,g_LogTag ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,g_LogTag ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,g_LogTag ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,g_LogTag ,__VA_ARGS__) // 定义LOGF类型


void AppIterateLoop(); // 创建线程要用
void AppIterate();

void checkEglError(const char* operation);

void AppInit(ANativeWindow *window)
{
__android_log_print(ANDROID_LOG_ERROR, g_LogTag, "%s", "呼呼jjjhhhhhh哈哈哈哈哈哈哈哈哈");

    if (g_Initialized)
        return;

    ::window = window;
    ANativeWindow_acquire(window);

    // Initialize EGL
    // This is mostly boilerplate code for EGL...
    {
        //1、
    g_EglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (g_EglDisplay == EGL_NO_DISPLAY) {
        LOGE("eglGetDisplay error=%u", glGetError());
        return ;
    }
    LOGE("生成g_EglDisplay");
	checkEglError("");
    //2、
    EGLint *version = new EGLint[2];
    if (!eglInitialize(g_EglDisplay, &version[0], &version[1])) {
        LOGE("eglInitialize error=%u", glGetError());
        return ;
    }
    LOGE("eglInitialize成功");checkEglError("");
    //3、
    const EGLint attribs[] = {EGL_BUFFER_SIZE, 32, EGL_RED_SIZE, 8, EGL_GREEN_SIZE, 8,
                              EGL_BLUE_SIZE, 8, EGL_ALPHA_SIZE, 8, EGL_DEPTH_SIZE, 8, EGL_STENCIL_SIZE, 8, EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT, EGL_SURFACE_TYPE, EGL_WINDOW_BIT, EGL_NONE};

    EGLint num_config;
    if (!eglGetConfigs(g_EglDisplay, NULL, 1, &num_config)) {
        LOGE("eglGetConfigs  error =%u", glGetError());
        return ;
    }
    LOGE("num_config=%d", num_config);checkEglError("");
    // 4、
    if (!eglChooseConfig(g_EglDisplay, attribs, &mEglConfig, 1, &num_config)) {
        LOGE("eglChooseConfig  error=%u", glGetError());
        return ;
    }
    LOGE("eglChooseConfig成功");checkEglError("");
    //5、
    int attrib_list[] = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL_NONE};
    g_EglContext = eglCreateContext(g_EglDisplay, mEglConfig, EGL_NO_CONTEXT, attrib_list);
    if (g_EglContext == EGL_NO_CONTEXT) {
        LOGE("eglCreateContext  error = %u", glGetError());
        return ;
    }
    // 6、
    g_EglSurface = eglCreateWindowSurface(g_EglDisplay, mEglConfig, window, NULL);
    if (g_EglSurface == EGL_NO_SURFACE) {
        LOGE("eglCreateWindowSurface  error = %u", glGetError());
        return ;
    }
    LOGE("eglCreateWindowSurface成功");checkEglError("");
    //7、
    if (!eglMakeCurrent(g_EglDisplay, g_EglSurface, g_EglSurface, g_EglContext)) {
        LOGE("eglMakeCurrent  error = %u", glGetError());
        return ;
    }
    LOGE("eglMakeCurrent成功");checkEglError("");
    }

    // Setup Dear ImGui context
    IMGUI_CHECKVERSION();
    ImGui::CreateContext();
    ImGuiIO& io = ImGui::GetIO();

    // Redirect loading/saving of .ini file to our location.
    // Make sure 'g_IniFilename' persists while we use Dear ImGui.
    g_IniFilename = std::string("/sdcard") + "/imgui.ini";
    io.IniFilename = g_IniFilename.c_str();

    // Setup Dear ImGui style
    ImGui::StyleColorsDark();
    //ImGui::StyleColorsLight();

    // Setup Platform/Renderer backends
    ImGui_ImplAndroid_Init(window);
    ImGui_ImplOpenGL3_Init("#version 300 es");

    // Load Fonts
    // - If no fonts are loaded, dear imgui will use the default font. You can also load multiple fonts and use ImGui::PushFont()/PopFont() to select them.
    // - If the file cannot be loaded, the function will return a nullptr. Please handle those errors in your application (e.g. use an assertion, or display an error and quit).
    // - Read 'docs/FONTS.md' for more instructions and details.
    // - Remember that in C/C++ if you want to include a backslash \ in a string literal you need to write a double backslash \\ !
    // - Android: The TTF files have to be placed into the assets/ directory (android/app/src/main/assets), we use our GetAssetData() helper to retrieve them.

    // We load the default font with increased size to improve readability on many devices with "high" DPI.
    // FIXME: Put some effort into DPI awareness.
    // Important: when calling AddFontFromMemoryTTF(), ownership of font_data is transferred by Dear ImGui by default (deleted is handled by Dear ImGui), unless we set FontDataOwnedByAtlas=false in ImFontConfig
    ImFontConfig font_cfg;
    font_cfg.SizePixels = 22.0f;
    io.Fonts->AddFontDefault(&font_cfg);
    //void* font_data;
    //int font_data_size;
    //ImFont* font;
    //font_data_size = GetAssetData("segoeui.ttf", &font_data);
    //font = io.Fonts->AddFontFromMemoryTTF(font_data, font_data_size, 16.0f);
    //IM_ASSERT(font != nullptr);
    //font_data_size = GetAssetData("DroidSans.ttf", &font_data);
    //font = io.Fonts->AddFontFromMemoryTTF(font_data, font_data_size, 16.0f);
    //IM_ASSERT(font != nullptr);
    //font_data_size = GetAssetData("Roboto-Medium.ttf", &font_data);
    //font = io.Fonts->AddFontFromMemoryTTF(font_data, font_data_size, 16.0f);
    //IM_ASSERT(font != nullptr);
    //font_data_size = GetAssetData("Cousine-Regular.ttf", &font_data);
    //font = io.Fonts->AddFontFromMemoryTTF(font_data, font_data_size, 15.0f);
    //IM_ASSERT(font != nullptr);
    //font_data_size = GetAssetData("ArialUni.ttf", &font_data);
    //font = io.Fonts->AddFontFromMemoryTTF(font_data, font_data_size, 18.0f);
    //IM_ASSERT(font != nullptr);

    // Arbitrary scale-up
    // FIXME: Put some effort into DPI awareness
    ImGui::GetStyle().ScaleAllSizes(3.0f);
    ImGui::GetStyle().FontScaleDpi = 1.0f;

    g_Initialized = true;
    
    //std::thread *t = new std::thread(AppIterateLoop);
    //t->detach();
    //eglSwapBuffers(g_EglDisplay, g_EglSurface);checkEglError("eglSwapBuffers");
    // system("echo xxxx >> /storage/emulated/0/Android/data/io.github.androidfloatwindowdearimgui/files/a.txt");
	for (int i=0;i<100;i++)
	AppIterate();
	
	LOGE("sieiieieiejejejejdjejejejejejejjfjfbfbehehe==============");
}

#define LOG_TAG "测试"
void checkEglError(const char* operation) {
    EGLint error = eglGetError();
    if (error != EGL_SUCCESS) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL error after %s: 0x%x", operation, error);
        // 可以根据错误码输出更详细的描述
        switch(error) {
            case EGL_NOT_INITIALIZED:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_NOT_INITIALIZED: EGL not initialized or failed to initialize");
                break;
            case EGL_BAD_ACCESS:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_ACCESS: EGL cannot access a requested resource");
                break;
            case EGL_BAD_ALLOC:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_ALLOC: EGL failed to allocate resources");
                break;
            case EGL_BAD_ATTRIBUTE:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_ATTRIBUTE: An unrecognized attribute was specified");
                break;
            case EGL_BAD_CONTEXT:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_CONTEXT: Invalid EGL context");
                break;
            case EGL_BAD_CONFIG:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_CONFIG: Invalid EGL frame buffer configuration");
                break;
            case EGL_BAD_CURRENT_SURFACE:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_CURRENT_SURFACE: Current surface is no longer valid");
                break;
            case EGL_BAD_DISPLAY:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_DISPLAY: Invalid EGL display");
                break;
            case EGL_BAD_SURFACE:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_SURFACE: Invalid surface (window, pbuffer, etc.)");
                break;
            case EGL_BAD_MATCH:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_MATCH: Arguments are inconsistent");
                break;
            case EGL_BAD_PARAMETER:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_PARAMETER: One or more parameters are invalid");
                break;
            case EGL_BAD_NATIVE_PIXMAP:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_NATIVE_PIXMAP: Invalid native pixmap");
                break;
            case EGL_BAD_NATIVE_WINDOW:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_BAD_NATIVE_WINDOW: Invalid native window");
                break;
            case EGL_CONTEXT_LOST:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "EGL_CONTEXT_LOST: Context lost due to power management event");
                break;
            default:
                __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Unknown EGL error");
        }
    }
}

void AppEvent()
{
}
void AppIterateLoop()
{
    for (int i=0;i<100;i++)
    {
        AppIterate();
    }
}

int aaa=0;

void AppIterate()
{
// checkEglError("未知错误未知错误未知错误未知错误未知错误未知错误");
LOGE("hhhh");

    ImGuiIO& io = ImGui::GetIO();
    if (g_EglDisplay == EGL_NO_DISPLAY)
        return;

    // Our state
    // (we use static, which essentially makes the variable globals, as a convenience to keep the example code easy to follow)
    static bool show_demo_window = true;
    static bool show_another_window = false;
    static ImVec4 clear_color = ImVec4(1.0f, 0.0f, 0.0f, 0.00f);

    // Poll Unicode characters via JNI
    // FIXME: do not call this every frame because of JNI overhead
    // PollUnicodeChars();

    // Open on-screen (soft) input if requested by Dear ImGui
    /*static bool WantTextInputLast = false;
    if (io.WantTextInput && !WantTextInputLast)
        ShowSoftKeyboardInput();
    WantTextInputLast = io.WantTextInput;*/

    // Start the Dear ImGui frame
    ImGui_ImplOpenGL3_NewFrame();checkEglError("ImGui_ImplOpenGL3_NewFrame");
    ImGui_ImplAndroid_NewFrame();checkEglError("ImGui_ImplAndroid_NewFrame");
    ImGui::NewFrame();checkEglError("NewFrame");

    // 1. Show the big demo window (Most of the sample code is in ImGui::ShowDemoWindow()! You can browse its code to learn more about Dear ImGui!).
    //if (show_demo_window)
        ImGui::ShowDemoWindow(0);checkEglError("ShowDemoWindow");
    // 2. Show a simple window that we create ourselves. We use a Begin/End pair to create a named window.
    {
        static float f = 0.0f;
        static int counter = 0;

        //。ImGui::Begin("Hello, world!"); // Create a window called "Hello, world!" and append into it.

        ImGui::Text("This is some useful text.");               // Display some text (you can use a format strings too)
        //ImGui::Checkbox("Demo Window", &show_demo_window);      // Edit bools storing our window open/close state
        //ImGui::Checkbox("Another Window", &show_another_window);

        ImGui::SliderFloat("float", &f, 0.0f, 1.0f);            // Edit 1 float using a slider from 0.0f to 1.0f
        ImGui::ColorEdit3("clear color", (float*)&clear_color); // Edit 3 floats representing a color

        if (ImGui::Button("Button"))                            // Buttons return true when clicked (most widgets return true when edited/activated)
            counter++;
        ImGui::SameLine();
        ImGui::Text("counter = %d", counter);

        ImGui::Text(" %.3f ms/frame (%.1f FPS)", 1000.0f / io.Framerate, io.Framerate);
        //ImGui::End();
    }checkEglError("Hello, world");

    // 3. Show another simple window.
    if (show_another_window)
    {
        ImGui::Begin("Another Window", &show_another_window);   // Pass a pointer to our bool variable (the window will have a closing button that will clear the bool when clicked)
        ImGui::Text("Hello from another window!");
        if (ImGui::Button("Close Me"))
            show_another_window = false;
        ImGui::End();
    }checkEglError("show_another_windowd");

    // Rendering
    ImGui::Render();checkEglError("Render");
    glViewport(0, 0, (int)io.DisplaySize.x, (int)io.DisplaySize.y);checkEglError("glViewport");
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glClearColor(clear_color.x * clear_color.w, clear_color.y * clear_color.w, clear_color.z * clear_color.w, clear_color.w);
    // glClear(GL_COLOR_BUFFER_BIT);
    
    ImGui_ImplOpenGL3_RenderDrawData(ImGui::GetDrawData());checkEglError("ImGui_ImplOpenGL3_RenderDrawData");
    eglSwapBuffers(g_EglDisplay, g_EglSurface);
    //checkEglError("eglSwapBuffers:313");
	aaa++;
	if (aaa<=30){
	//checkEglError("eglSwapBuffers");
    }
	
	
    // system("echo xxxx >> /storage/emulated/0/Android/data/io.github.androidfloatwindowdearimgui/files/a.txt");
}

void AppQuit()
{
    if (!g_Initialized)
        return;

    // Cleanup
    ImGui_ImplOpenGL3_Shutdown();
    ImGui_ImplAndroid_Shutdown();
    ImGui::DestroyContext();

    if (g_EglDisplay != EGL_NO_DISPLAY)
    {
        eglMakeCurrent(g_EglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);

        if (g_EglContext != EGL_NO_CONTEXT)
            eglDestroyContext(g_EglDisplay, g_EglContext);

        if (g_EglSurface != EGL_NO_SURFACE)
            eglDestroySurface(g_EglDisplay, g_EglSurface);

        eglTerminate(g_EglDisplay);
    }

    g_EglDisplay = EGL_NO_DISPLAY;
    g_EglContext = EGL_NO_CONTEXT;
    g_EglSurface = EGL_NO_SURFACE;
    ANativeWindow_release(window);

    g_Initialized = false;
}
