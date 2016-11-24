# WirelessRemote

本项目是一个无线遥控器，可以代替物理遥控器操作TV端，比如，移动焦点，选择，关机，打开设置菜单等基本操作

分为客户端和服务端

遥控器客户端，安装在手机上
服务端WirelessRemoteServer.apk安装在TV中，当然安装到手机上也可以

客户端地址：https://github.com/andevele/WirelessRemote
服务端地址：https://github.com/andevele/WirelessRemoteServer


客户端：安装好WirelessRemote.apk后，需修改一下Constants.java中的HOST_ADDR变量，这是服务端的ip地址，改成你自己的服务端(TV或另外手机)即可

服务端：直接安装到TV中是无法操作的，因为涉及到注入事件权限的问题，因此按照下面步骤进行
1. 在AndroidManifest.xml中添加android:sharedUserId="android.uid.system" 

2. 把服务端源码WirelessRemoteServer放到package/app中，添加Android.mk文件，内容为：
LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_CERTIFICATE := platform
LOCAL_PACKAGE_NAME := WirelessRemoteServer

include $(BUILD_PACKAGE)

作者已经在源码中提交了Android.mk文件，可直接使用

3. mm局部编译生成WirelessRemoteServer.apk，然后无论是放到system/app下还是adb install安装都可以

4. 安装好后，打开程序，选择右边的多选框，打开服务，然后按下HOME或者返回键退出程序，服务仍然在运行
5. 再打开手机上的客户端app，操作按键即可控制TV端



本应用程序主要用于学习交流，用户可以发挥想象自行修改，完善更多功能

作者联系方式：
朱罗锋_zhulf
fgood@aliyun.com





