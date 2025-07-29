from PIL import Image
import os
def compress_image_with_resize(input_path, output_path, max_size=(800, 800), quality=85):
    """
    调整图片尺寸并压缩质量
    :param input_path: 输入图片路径
    :param output_path: 输出图片路径
    :param max_size: 最大尺寸 (宽, 高)
    :param quality: 压缩质量 (0-100)
    """
    try:
        img = Image.open(input_path)
        if img.mode != 'RGB':
            img = img.convert('RGB')
        
        # 调整尺寸
        img.thumbnail(max_size, Image.LANCZOS)
        
        # 保存
        img.save(output_path, "JPEG", quality=quality, optimize=True)
        
        original_size = os.path.getsize(input_path)
        compressed_size = os.path.getsize(output_path)
        print(f"压缩完成: 原始大小 {original_size/1024:.2f}KB -> 压缩后 {compressed_size/1024:.2f}KB")
        
    except Exception as e:
        print(f"压缩失败: {e}")

# 使用示例
compress_image_with_resize("截屏.jpg", "output.jpg", max_size=(504, 500), quality=80)
