LOCAL_PATH := $(call my-dir)

# 清除之前定义的变量
include $(CLEAR_VARS)

# 设置静态库的名称（不需要加前缀 lib 和后缀 .a）
LOCAL_MODULE := dearimgui

# 文件夹位置（相对于本 Android.mk）
IMGUI_DIR := ../../../../../../DearImGui-1.92.1

# 指定源文件
LOCAL_SRC_FILES := \
    $(IMGUI_DIR)/imgui.cpp \
    $(IMGUI_DIR)/imgui_demo.cpp \
    $(IMGUI_DIR)/imgui_draw.cpp \
    $(IMGUI_DIR)/imgui_tables.cpp \
    $(IMGUI_DIR)/imgui_widgets.cpp \
    $(IMGUI_DIR)/backends/imgui_impl_android.cpp \
    $(IMGUI_DIR)/backends/imgui_impl_opengl3.cpp

# 如果有头文件，可以指定包含路径（可选）
LOCAL_C_INCLUDES := $(LOCAL_PATH)/$(IMGUI_DIR) $(LOCAL_PATH)/$(IMGUI_DIR)/backends
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_C_INCLUDES)

# 可选：添加编译标志
# LOCAL_CFLAGS := -Wall -Wextra

# 依赖库
LOCAL_SHARED_LIBRARIES := 

# 构建静态库
include $(BUILD_STATIC_LIBRARY)
