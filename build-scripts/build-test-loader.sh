#!/bin/bash

# 任意位置运行，编译并安放调试加载器

# 设置文件夹路径变量（你可以修改这个值）
folder_path="/storage/emulated/0/AppProjects/greedy-snake-practice-project/android/app/src/main/"

# 检查路径是否存在
if [ ! -d "$folder_path" ]; then
    echo "\033[31m错误：路径 '$folder_path' 不存在\033[0m"
    exit 1
fi

# 定义目标文件夹的完整路径
a_folder="$folder_path/jni"
hidden_a_folder="$folder_path/.jni"

# 检查并执行重命名操作
if [ -d "$hidden_a_folder" ]; then
    # 如果.a存在，重命名为a
    mv "$hidden_a_folder" "$a_folder"
    echo "\033[34m操作：将 '.jni' 重命名为 'jni'\033[0m"
else
    echo "\033[31m操作：'.jni' 文件夹不存在，没有执行任何操作\033[0m"
    exit 1
fi

# 调用编译工具
cd $folder_path
~/android-ndk-r24/ndk-build -j use_debug_loader=1
echo "\033[34mndk-build 调用结束，使用loader\033[0m"

# 转移编译生成的结果
rm -r jniLibs/
mv -v libs/ jniLibs/
echo "\033[34m转移完成\033[0m"

# 检查并执行重命名操作
if [ -d "$a_folder" ]; then
    # 如果.a存在，重命名为a
    mv "$a_folder" "$hidden_a_folder"
    echo "\033[34m操作：将 'jni' 重命名为 '.jni'\033[0m"
else
    echo "\033[31m操作：'jni' 文件夹不存在，没有执行任何操作\033[0m"
    exit 1
fi
