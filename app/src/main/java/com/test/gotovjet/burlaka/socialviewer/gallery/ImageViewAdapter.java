package com.test.gotovjet.burlaka.socialviewer.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.test.gotovjet.burlaka.socialviewer.R;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImageViewAdapter extends android.support.v4.view.PagerAdapter {
    private Context context;
    ImageView imageView;
    ProgressBar progressBar;
    private LayoutInflater layoutInflater;
    List<String> imgUrl;
    ImageLoaderConfiguration config;
    DisplayImageOptions displayImageOptions;

    public ImageViewAdapter(Context context,  List<String> imgUrl){
        System.out.println("Inside ImageViewAdapter");
        this.context=context;
        this.imgUrl=imgUrl;
        System.out.println(imgUrl.size());
        initImageLoader();
    }

    private void initImageLoader() {
      displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();

        File cacheDir;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "data/cache");
        } else {
            cacheDir = context.getCacheDir();
        }
        cacheDir.mkdirs();
        config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // width, height
                //.discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75) // width, height, compress format, quality
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 2 Mb
                //.discCache(new UnlimitedDiscCache(cacheDir))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                // .imageDownloader(new BaseImageDownloader(5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                //.enableLogging()
                .build();
    }

    @Override
    public int getCount() {
        return imgUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final RecordHolder holder;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View item_view = layoutInflater.inflate(R.layout.image_swipe_custom_layout, container, false);
        imageView = (ImageView) item_view.findViewById(R.id.ivFaceBook);
        holder = new RecordHolder();
        holder.imagev = imageView ;

        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(config);

        imageLoader.displayImage(imgUrl.get(position), holder.imagev, displayImageOptions, new ImageLoadingListener() {
             final List<String> displayedImages = Collections
                    .synchronizedList(new LinkedList<String>());

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //progressBar.setVisibility(View.GONE);
                if (loadedImage != null) {
                    ImageView imageView = (ImageView) view;
                    boolean firstDisplay = !displayedImages.contains(imageUri);
                    if (firstDisplay) {
                        FadeInBitmapDisplayer.animate(imageView, 400);
                        displayedImages.add(imageUri);
                    }
                }
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        container.addView(item_view);
        return item_view;
    }

    static class RecordHolder {
        ImageView imagev;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //((ViewPager)container).removeView((View)object);
        container.removeView((LinearLayout) object);
    }
}