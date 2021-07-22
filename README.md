# TokTik
NJU SE&amp;Bytedance 安卓开发课程大作业 - 抖音APP

这是南京大学软件学院与字节跳动课程-安卓开发实训的大作业仓库，大作业内容是实现一个简易版的**抖音APP**

小组成员：[@FertileFragrance](https://github.com/FertileFragrance) 李和煦 [@cycsir](https://github.com/cycsir) 陈益超 [@fengguohao](https://github.com/fengguohao) 冯国豪

## 已经实现的功能列表

- 视频信息流列表展示
  - 使用ViewPager2+Fragment实现推荐页面
    - 无限列表
    - 视频流刷新
  - 使用RecyclerView实现精选页面
    - 视频封面显示
    - 用户信息与描述显示
  - 收藏列表展示
    - 收藏/取消收藏
    - 数据库存储收藏信息
- 视频播放
  - 视频封面显示
  - 用户信息与描述显示
  - 对视频播放组件使用Fragment进行封装
  - 单击暂停/继续播放
  - 双击点赞
  - 长按唤起控制栏
  - 可以收藏视频与取消收藏视频
  - 视频预加载
  - 视频缓存
  - 转发功能
- 视频拍摄录制
  - 调用摄像头
  - 重新封装拍摄界面
  - 切换前后摄像头
- 视频上传
  - 视频预览
  - 填写描述等信息
  - 视频上传

## 所使用的技术

视频下拉刷新：SwiperView

推荐页面：ViewPager2

精选页面：RecyclerView

图片加载：Glide

视频播放：ijkplayer

网络请求：Retrofit2

json解析与序列化：Glide

手势识别：GestureDetector

视频预加载与缓存：AndroidVideoCache

拍摄视频：Camera、Chronomater

其他：动画、layout基础、fragment、无尽列表......

**仓库中有本项目的展示视频可以查看**

欢迎大家交流学习，提出意见，感谢
