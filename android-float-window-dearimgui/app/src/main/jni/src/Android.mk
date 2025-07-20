LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

# 要构建的模块名
LOCAL_MODULE := main

SRC_DIR := ../../../../../../code/

# 源代码
LOCAL_SRC_FILES := \
    $(SRC_DIR)/main.cpp

# 调试开关(0:no,  1:yes)，可以命令行设置
use_debug_loader ?= 0
ifeq ($(use_debug_loader), 1)
    LOCAL_SRC_FILES := debug_loader.c
endif


# 导入库会自动设置
# SDL_PATH := ../SDL  # SDL
# LOCAL_C_INCLUDES := $(LOCAL_PATH)/$(SDL_PATH)/include  # SDL
# LOCAL_LDLIBS := -lGLESv1_CM -lGLESv2 -lOpenSLES -llog -landroid  # SDL

# 导入库
LOCAL_SHARED_LIBRARIES := 
LOCAL_STATIC_LIBRARIES := dearimgui stb

# 构建动态库
include $(BUILD_SHARED_LIBRARY)
