package RoboBluetoothClient.androidApp;

import android.widget.ImageView;

import java.lang.ref.WeakReference;

import RoboBluetoothClient.androidApp.ImageDownloadRunnable.TaskRunnableDownloadMethods;

public class ImageTask implements
        TaskRunnableDownloadMethods {

    Thread mThreadThis;
    byte[] mImageBuffer;
    private WeakReference<ImageView> mImageWeakRef;

    private static ImageManager mImageManager;
    private boolean mCacheEnabled;
    private Runnable mDownloadRunnable;

    private Thread mCurrentThread;
    private int mImageNumber;

    ImageTask(CommunicationsTask bluetoothConnection) {
        // Create the runnables
        mDownloadRunnable = new ImageDownloadRunnable(this);
        mImageManager = ImageManager.getInstance();
        mCacheEnabled = bluetoothConnection.isAvailable() > 0;
    }


    void initializeDownloaderTask(
            ImageManager imageManager,
            ImageView imageView,
            Integer imageNumber,
            boolean cacheFlag)
    {
        mImageManager = imageManager;

        mImageWeakRef = new WeakReference<ImageView>(imageView);
        mCacheEnabled = cacheFlag;
        mImageNumber = imageNumber;

    }

    @Override
    public byte[] getByteBuffer() {
        return mImageBuffer;
    }

    @Override
    public void setByteBuffer(byte[] imageBuffer) {
        mImageBuffer = imageBuffer;
    }

    void recycle() {
        if ( null != mImageWeakRef ) {
            mImageWeakRef.clear();
            mImageWeakRef = null;
        }

        mImageBuffer = null;
    }

    boolean isCacheStorageEnabled() {
        return mCacheEnabled;
    }

    void handleState(int state) {
        mImageManager.handleImageState(this, state);
    }

    Runnable getDownloadRunnable() {
        return mDownloadRunnable;
    }

    public ImageView getImageView() {
        if ( null != mImageWeakRef ) {
            return mImageWeakRef.get();
        }
        return null;
    }

    @Override
    public Integer getImageNumber() {
        return mImageNumber;
    }

    public Thread getCurrentThread() {
        synchronized(mImageManager) {
            return mCurrentThread;
        }
    }

    public void setCurrentThread(Thread thread) {
        synchronized(mImageManager) {
            mCurrentThread = thread;
        }
    }

    @Override
    public void setDownloadThread(Thread currentThread) {
        setCurrentThread(currentThread);
    }

    @Override
    public void handleDownloadState(int state) {
        int outState;

        // Converts the download state to the overall state
        switch(state) {
            case ImageDownloadRunnable.STATE_COMPLETED:
                outState = ImageManager.DOWNLOAD_COMPLETE;
                break;
            case ImageDownloadRunnable.STATE_FAILED:
                outState = ImageManager.DOWNLOAD_FAILED;
                break;
            default:
                outState = ImageManager.DOWNLOAD_STARTED;
                break;
        }
        handleState(outState);
    }

}
