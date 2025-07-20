#define SDL_MAIN_USE_CALLBACKS 1
#include <SDL3/SDL_main.h>
#include <SDL3/SDL.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <time.h>
#include <sys/stat.h>
#include <errno.h>
#include <string.h>


// 防卡死(2秒)
int is_crashed = 1;
int monitor_thread_func(void*xxxx) {
    int a = SDL_GetTicks();
    while (1) {
        int delta = (SDL_GetTicks() - a) / 1000;
        if (delta >= 2 && is_crashed == 1) {
            exit(9);
        }
    }
}


// 将内容输出到文件
static char log_file_name[1500];
static void log_cat(const char *msg) {
    FILE* fp = fopen(log_file_name, "a");
    if (!fp) {
        return;
    }
    fprintf(fp, "%s\n", msg);
    fclose(fp);
    
    // SDL_ShowSimpleMessageBox(0, "简单消息框", msg, 0);
}

// 执行命令调用，对system的封装
static void exec_cmd(const char* cmd)
{
    int status = system(cmd);
    if (status == -1) {
        log_cat("system() 调用失败：");
        log_cat(strerror(errno));
    }
}

// 保存日志(&符号，后台运行)
static void show_logcat(void)
{
    char cmd[1000];
    sprintf(cmd, "logcat >> %s &", log_file_name);
    log_cat("执行命令调用：logcat，细节如下：");
    log_cat(cmd);
    // log_cat("before system show_logcat");
    // system(cmd);
    // log_cat("show_logcat");
    exec_cmd(cmd);
    log_cat("完成命令调用");
}

// 清除日志缓存
static void clear_logcat(void)
{
    char cmd[1000];
    sprintf(cmd, "logcat -c &");
    // system(cmd);
    // log_cat("clear_logcat");
    log_cat("执行命令调用：logcat -c ＆");
    exec_cmd(cmd);
    log_cat("完成命令调用");
}

#define OK  1
#define ERR 0
// 检测加载是否成功
static int check(void)
{
    char* msg = dlerror();
    if (msg != NULL) {
        // 有错误信息
        log_cat(msg);
        return ERR;
    }
    return OK;
}

// 回调函数定义
// typedef int (*MainFunc)(int,char**);
typedef SDL_AppResult (*Func_SDL_AppInit)(void **appstate, int argc, char *argv[]);
typedef SDL_AppResult (*Func_SDL_AppIterate)(void *appstate);
typedef SDL_AppResult (*Func_SDL_AppEvent)(void *appstate, SDL_Event *event);
typedef void          (*Func_SDL_AppQuit)(void *appstate, SDL_AppResult result);

static Func_SDL_AppInit    appinit;
static Func_SDL_AppIterate appiterate;
static Func_SDL_AppEvent   appevent;
static Func_SDL_AppQuit    appquit;

// static const char* exPath;
static const char* inPath;          // Android APP 内部路径文件
static const char* DocumentsPath;   // Android 文档统一路径

// 共享库
static void *lib = 0;
static SDL_AppResult ret = SDL_APP_FAILURE;

SDL_AppResult SDL_AppInit(void **appstate, int argc, char *argv[])
{
    // exPath = SDL_GetAndroidExternalStoragePath();
    inPath = SDL_GetAndroidInternalStoragePath();
    DocumentsPath = "/storage/emulated/0/Documents";

    // 指定日志位置
    sprintf(log_file_name, "%s/loader.log", DocumentsPath);
    log_cat("");    // 分割上一次的信息

    // 打印时间
    time_t now = time(NULL);
    char time_str[1000];
    strftime(time_str, sizeof(time_str), "===============[%Y-%m-%d %H:%M:%S]==================", localtime(&now));
    log_cat(time_str);

    // 防卡死
    log_cat("创建防卡死线程。");
    SDL_Thread *tfks = SDL_CreateThread(monitor_thread_func, "防卡死", 0);
    if (!tfks) {
        log_cat("防卡死线程创建失败。");
        return SDL_APP_FAILURE;
    }

    // 显示上一次的logcat
    clear_logcat();
    show_logcat();
    
    // 防卡死
    is_crashed = 0;

    // 在Java端复制
    // 复制库文件
    // char cmd[2000];
    // sprintf(cmd, "cp %s/libmain.so %s/libmain.so 2>> %s", DocumentsPath, inPath, log_file_name);
    // log_cat(cmd);
    // system(cmd);

    // 指定库的路径
    char lib_path[1500];
    sprintf(lib_path, "%s/libmain.so", inPath);

    // 读取库文件
    lib = dlopen(lib_path, RTLD_NOW);
    log_cat("dlopen(lib)");
    ret = SDL_APP_FAILURE;
    if (check() == OK) {
        // 解析函数地址
        appinit = (Func_SDL_AppInit)dlsym(lib, "SDL_AppInit");
        log_cat("dlsym(appinit)");
        if (check() != OK) {
            log_cat("error");
            show_logcat();
        } else {
            appiterate = (Func_SDL_AppIterate)dlsym(lib, "SDL_AppIterate");
            log_cat("dlsym(appiterate)");
            if (check() != OK) {
                log_cat("error");
                show_logcat();
            } else {
                appevent = (Func_SDL_AppEvent)dlsym(lib, "SDL_AppEvent");
                log_cat("dlsym(appevent)");
                if (check() != OK) {
                    log_cat("error");
                    show_logcat();
                } else {
                    appquit = (Func_SDL_AppQuit)dlsym(lib, "SDL_AppQuit");
                    log_cat("dlsym(appquit)");
                    if (check() != OK) {
                        log_cat("error");
                        show_logcat();
                    } else {
                        ret = SDL_APP_CONTINUE;
                    }
                }
            }
        }
    }

    if (ret == SDL_APP_CONTINUE) {
        return appinit(appstate, argc, argv);
    }
    return SDL_APP_FAILURE;
}

SDL_AppResult SDL_AppIterate(void *appstate)
{
    return appiterate(appstate);
}

SDL_AppResult SDL_AppEvent(void *appstate, SDL_Event *event)
{
    return appevent(appstate, event);
}

void SDL_AppQuit(void *appstate, SDL_AppResult result)
{
    if (ret == SDL_APP_FAILURE) {
        // 错误处理
        log_cat("loader ret == SDL_APP_FAILURE");
    } else if (ret == SDL_APP_SUCCESS) {
        // 一般无此情况
        log_cat("loader ret == SDL_APP_SUCCESS");
    } else {
        log_cat("loader ret == SDL_APP_CONTINUE");
        appquit(appstate, result);
    }
    log_cat("dlclose(lib)");
    dlclose(lib);
    lib = 0;
    
    // log_cat("kill %%1");
    // system("kill %1");
    // log_cat("kill ok");
    // 在Java端强制清除
}