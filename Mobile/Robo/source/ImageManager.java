package RoboBluetoothClient.androidApp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageManager {
    public static boolean sDownloadInProgress = false;

    static final int DOWNLOAD_FAILED = -1;
    static final int DOWNLOAD_STARTED = 1;
    static final int DOWNLOAD_COMPLETE = 2;

    private static final TimeUnit TIME_UNIT;
    private static final int IMAGE_CACHE_SIZE = 1024 * 1024 * 4;
    private static final int KEEP_ALIVE_TIME = 1;

    private static final int POOL_SIZE = 8;

    private static ImageManager sInstance = null;
    static {
        TIME_UNIT = TimeUnit.SECONDS;
        sInstance = new ImageManager();
    }

    private final LruCache<Integer, byte[]> mImageCacheStorage;
    private final BlockingQueue<Runnable> mDownloadQueue;
    private final Queue<ImageTask> mImageTaskWorkQueue;
    private final ThreadPoolExecutor mDownloadPool;
    private Handler mHandler;

    private ImageManager() {
        mDownloadQueue = new LinkedBlockingQueue<Runnable>();

        mImageTaskWorkQueue = new LinkedBlockingQueue<ImageTask>();

        mDownloadPool = new ThreadPoolExecutor(POOL_SIZE, 8,
                KEEP_ALIVE_TIME, TIME_UNIT, mDownloadQueue);

        mImageCacheStorage = new LruCache<Integer, byte[]>(IMAGE_CACHE_SIZE) {
            @Override
            protected int sizeOf(Integer paramURL, byte[] paramArrayOfByte) {
                return paramArrayOfByte.length;
            }
        };

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                final int TASK_COMPLETE = 4;
                ImageTask photoTask = (ImageTask) inputMessage.obj;

                // input ImageView
                ImageView localView = photoTask.getImageView();

                // If this input view isn't null
                if (localView != null) {

                        switch (inputMessage.what) {
                            case DOWNLOAD_STARTED:
                                localView.setImageResource(R.drawable.ic_donloading);
                                sDownloadInProgress = true;
                                break;

                            case DOWNLOAD_COMPLETE:
                                if(photoTask.mImageBuffer != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(photoTask.mImageBuffer, 0, photoTask.mImageBuffer.length);

                                    localView.setImageBitmap(bitmap);
                                }
                                sDownloadInProgress = false;
                                break;

                            case TASK_COMPLETE:
                                if(photoTask.mImageBuffer != null) {
                                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(photoTask.mImageBuffer, 0, photoTask.mImageBuffer.length);
                                    localView.setImageBitmap(bitmap2);
                                }
                                recycleTask(photoTask);
                                sDownloadInProgress = false;
                                break;
                            case DOWNLOAD_FAILED:
                                // Attempts to re-use the Task object
                                recycleTask(photoTask);
                                break;
                            default:
                                // Otherwise, calls the super method
                                super.handleMessage(inputMessage);
                        }
                }
            }
        };
    }

    public static ImageManager getInstance() {
        return sInstance;
    }

    public static void stopAllDownloads() {
        ImageTask[] taskArray = new ImageTask[sInstance.mDownloadQueue.size()];
        sInstance.mDownloadQueue.toArray(taskArray);

        int taskArraylen = taskArray.length;

        synchronized (sInstance) {

            for (int taskArrayIndex = 0; taskArrayIndex < taskArraylen; taskArrayIndex++) {

                Thread thread = taskArray[taskArrayIndex].mThreadThis;

                if (null != thread) {
                    thread.interrupt();
                }
            }
        }
    }

    static public void stopDownload(ImageTask downloaderTask, Integer imageNumber) {
        if (downloaderTask != null && downloaderTask.getImageNumber().equals(imageNumber))
             {

            synchronized (sInstance) {

                Thread thread = downloaderTask.getCurrentThread();

                if (null != thread)
                    thread.interrupt();
            }
            sInstance.mDownloadPool.remove(downloaderTask.getDownloadRunnable());
        }
    }


    public static ImageTask startDownload(
            ImageView imageView,
            Integer imageNumber,
            boolean cacheFlag) {

        ImageTask downloadTask = sInstance.mImageTaskWorkQueue.poll();

        if (null == downloadTask) {
            downloadTask = new ImageTask(ClientApplication.getApplication().getCurrentBluetoothConnection());
        }

        downloadTask.initializeDownloaderTask(ImageManager.sInstance, imageView,imageNumber, cacheFlag);
        downloadTask.setByteBuffer(sInstance.mImageCacheStorage.get(imageNumber));

        if (null == downloadTask.getByteBuffer()) {
            sInstance.mDownloadPool.execute(downloadTask.getDownloadRunnable());
        } else {
            sInstance.handleImageState(downloadTask, DOWNLOAD_COMPLETE);
        }

        return downloadTask;
    }

    @SuppressLint("HandlerLeak")
    public void handleImageState(ImageTask photoTask, int state) {
        final int TASK_COMPLETE = 4;
        switch (state) {

            case TASK_COMPLETE:
                // Save in cache
                if (photoTask.isCacheStorageEnabled()) {
                    int size = mImageCacheStorage.size();
                    mImageCacheStorage.put(size, photoTask.getByteBuffer());
                }
                Message completeMessage = mHandler.obtainMessage(state, photoTask);
                completeMessage.sendToTarget();
                break;


            //Wait for message
            default:
                mHandler.obtainMessage(state, photoTask).sendToTarget();
                break;
        }

    }

    void recycleTask(ImageTask downloadTask) {
        downloadTask.recycle();
        mImageTaskWorkQueue.offer(downloadTask);
    }
}
