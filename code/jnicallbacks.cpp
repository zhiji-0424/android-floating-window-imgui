#include <jni.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>

extern void AppInit(ANativeWindow *window);
extern void AppEvent();
extern void AppIterate();
extern void AppQuit();

static ANativeWindow *window = 0;

extern "C" {

    JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppInit(JNIEnv *env, jobject obj, jobject surface);
    
    JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppQuit(JNIEnv *env, jobject obj);
    
    JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppIterate(JNIEnv *env, jobject obj);
    
    JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppEvent(JNIEnv *env, jobject obj);

}

JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppInit(JNIEnv *env, jobject obj, jobject surface) {
    window = ANativeWindow_fromSurface(env, surface);
    if (!window) {
        // 日志。。
    }
    AppInit(window);
}

JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppQuit(JNIEnv *env, jobject obj) {
    AppQuit();
}

JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppIterate(JNIEnv *env, jobject obj) {
    AppIterate();
}

JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppEvent(JNIEnv *env, jobject obj) {
    AppEvent();
}