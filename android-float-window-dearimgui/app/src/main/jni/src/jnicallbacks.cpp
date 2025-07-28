#include <jni.h>
#include <stdlib.h>
#include <android/log.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

extern void AdapterAppInit(ANativeWindow *window);
extern void AdapterAppEvent(int event_type,
                     int key_code, int scan_code, int key_action, int mate_state,
                     int device_type, int action, int x, int y);
// extern void AdapterAppIterate();
extern void AdapterAppQuit();
extern void AdapterAssetInit(AAssetManager *asset_manager);


extern "C" {

    JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppInit(JNIEnv *env, jobject obj, jobject surface);
    
    JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppQuit(JNIEnv *env, jobject obj);
    
    JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppAssetInit(JNIEnv *env, jobject obj, jobject jasset_manager);
    
    // JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppIterate(JNIEnv *env, jobject obj);
    
    JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppEvent(JNIEnv *env, jobject obj, jint event_type,
                                                                                           jint key_code, jint scan_code, jint key_action, jint mate_state,
                                                                                           jint device_type, jint action, jint x, jint y);

}

JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppInit(JNIEnv *env, jobject obj, jobject surface) {
    ANativeWindow *window = ANativeWindow_fromSurface(env, surface);
    if (!window) {
        __android_log_print(ANDROID_LOG_ERROR, "jnicallbacks.cpp", "ANativeWindow_fromSurface 发生错误，window==null");
        exit(1);
    } else {
        AdapterAppInit(window);
    }
}

JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppQuit(JNIEnv *env, jobject obj) {
    AdapterAppQuit();
}

// 记住，AssetInit 的调用时间早于 Init
JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppAssetInit(JNIEnv *env, jobject obj, jobject jasset_manager) {
    AAssetManager* mgr = AAssetManager_fromJava(env, jasset_manager);
    if (!mgr) {
        __android_log_print(ANDROID_LOG_ERROR, "jnicallbacks.cpp", "AAssetManager_fromJava 发生错误，mgr==null");
        exit(1);
    } else {
        AdapterAssetInit(mgr);
    }
}

// JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppIterate(JNIEnv *env, jobject obj) {
    // AdapterAppIterate();
// }

JNIEXPORT void JNICALL Java_net_zhiji_androidfloatingwindowimgui_jnicallbacks_AppEvent(JNIEnv *env, jobject obj, jint event_type,
                                                                                       jint key_code, jint scan_code, jint key_action, jint mate_state,
                                                                                       jint device_type, jint action, jint x, jint y) {
    AdapterAppEvent(event_type,
             key_code, scan_code, key_action, mate_state,
             device_type, action, x, y);
}