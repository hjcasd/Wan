package com.hjc.wan.utils.image

import android.content.Context
import android.widget.ImageView

import com.youth.banner.loader.ImageLoader

/**
 * @Author: HJC
 * @Date: 2019/3/21 17:44
 * @Description: Banner图片加载器
 */
class GlideImageLoader : ImageLoader() {

    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        ImageManager.loadImage(imageView, path as String)
    }
}
