#!/bin/bash

# 任意位置运行，此脚本作为jni文件夹的开关（隐藏与否）

# 设置文件夹路径变量（你可以修改这个值）
folder_path=$(pwd)/android-float-window-dearimgui/app/src/main/

# 检查路径是否存在
if [ ! -d "$folder_path" ]; then
    echo "错误：路径 '$folder_path' 不存在"
    exit 1
fi

# 定义目标文件夹的完整路径
a_folder="$folder_path/jni"
hidden_a_folder="$folder_path/.jni"

# 检查并执行重命名操作
if [ -d "$a_folder" ]; then
    # 如果a存在，重命名为.a
    mv "$a_folder" "$hidden_a_folder"
    echo "操作：将 'jni' 重命名为 '.jni'"
elif [ -d "$hidden_a_folder" ]; then
    # 如果.a存在，重命名为a
    mv "$hidden_a_folder" "$a_folder"
    echo "操作：将 '.jni' 重命名为 'jni'"
else
    echo "操作：'jni' 和 '.jni' 文件夹都不存在，没有执行任何操作"
fi
