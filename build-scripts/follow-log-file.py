import time
import os
import re

def colorize_line(line):
    """
    根据日志行中的关键字给文本添加 ANSI 颜色
    """
    # 定义关键字与颜色的映射关系（不区分大小写）
    color_map = {
        r' E ': '\033[31m',       # 错误信息用红色
        r' W ': '\033[33m',  # 警告信息用黄色
        r' D ': '\033[32m',        # 用绿色
        # r' I ': '\033[32m',        # 用白色
        r' V ': '\033[34m',        # 用蓝色
        # 可以根据需要扩展更多关键字和颜色
    }

    # 遍历所有关键字规则
    for pattern, color in color_map.items():
        if re.search(pattern, line, re.IGNORECASE):
            # 找到匹配的关键字，给整行添加颜色，并在结尾重置颜色
            return f"{color}{line}\033[0m"

    for t in ["MIUIInput"]:
        if re.search(t, line, re.IGNORECASE):
            return ""

    # 如果没有匹配到关键字，则返回默认颜色（不加颜色）
    return line

def follow_file(filename):
    """
    实时跟踪文件新增内容，并对特定日志行进行彩色输出
    """
    try:
        with open(filename, 'r', encoding='utf-8') as file:
            # 将文件指针移动到文件末尾
            file.seek(0, os.SEEK_END)

            while True:
                # 检查文件是否被截断（大小变小）
                current_size = os.path.getsize(filename)
                if current_size < file.tell():
                    print(f"[警告] 文件 {filename} 被截断，重新打开...")
                    file.close()
                    file = open(filename, 'r', encoding='utf-8')
                    file.seek(0, os.SEEK_END)
                else:
                    # 读取新的内容
                    line = file.readline()
                    if line:
                        # 对读取到的行进行颜色处理并输出
                        colored_line = colorize_line(line)
                        print(colored_line, end='')
                    else:
                        # 如果没有新内容，稍作等待
                        time.sleep(0.05)
    except FileNotFoundError:
        print(f"错误: 文件 {filename} 不存在。")
    except KeyboardInterrupt:
        print("\n程序已停止。")
    except Exception as e:
        print(f"发生错误: {e}")

if __name__ == "__main__":
    import sys

    """if len(sys.argv) != 2:
        print("用法: python tail_f.py <文件名>")
    else:
        filename = sys.argv[1]
        follow_file(filename)"""
    follow_file("/sdcard/Documents/loader.log")
