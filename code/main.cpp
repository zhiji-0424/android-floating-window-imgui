#include "imgui.h"
#include <android/log.h>
#include <string>


#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,g_LogTag ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,g_LogTag ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,g_LogTag ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,g_LogTag ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,g_LogTag ,__VA_ARGS__) // 定义LOGF类型



void AppInit()
{
    
}

void AppEvent()
{
    
}

void AppIterate()
{
    ImGuiIO& io = ImGui::GetIO();
    ImGui::SetNextWindowPos(ImVec2(0, 0));          // 将窗口位置设置为屏幕左上角
    ImGui::SetNextWindowSize(io.DisplaySize);      // 将窗口大小设置为屏幕分辨率
    ImGui::Begin("你好, hello", nullptr, ImGuiWindowFlags_NoResize        // 禁止调整窗口大小
                                       | ImGuiWindowFlags_NoMove          // 禁止移动窗口
    );
    ImGui::Text("You need to load a font supporting Chinese.");
    ImGui::End();
}

void AppQuit()
{
    
}
