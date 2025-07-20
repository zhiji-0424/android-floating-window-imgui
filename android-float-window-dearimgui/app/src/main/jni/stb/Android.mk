LOCAL_PATH := $(call my-dir)

# 清除之前定义的变量
include $(CLEAR_VARS)

# 设置静态库的名称（不需要加前缀 lib 和后缀 .a）
LOCAL_MODULE := stb

# 文件夹位置（相对于本 Android.mk）
STB_DIR := ../../../../../../stb-date-05.26

# 指定源文件
LOCAL_SRC_FILES := \
    stb_image.cpp

# 如果有头文件，可以指定包含路径（可选）
LOCAL_C_INCLUDES := $(LOCAL_PATH)/$(STB_DIR)
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_C_INCLUDES)

# 可选：添加编译标志
# LOCAL_CFLAGS := -Wall -Wextra

# 依赖库
# LOCAL_SHARED_LIBRARIES := SDL3

# 构建静态库
include $(BUILD_STATIC_LIBRARY)
