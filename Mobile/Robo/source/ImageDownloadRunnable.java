package RoboBluetoothClient.androidApp;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ImageDownloadRunnable implements Runnable {
    static final int STATE_FAILED = -1;
    static final int STATE_STARTED = 0;
    static final int STATE_COMPLETED = 1;
    private static final int READ_SIZE = 1024 * 2;
    final TaskRunnableDownloadMethods mImageTask;

    ImageDownloadRunnable(TaskRunnableDownloadMethods photoTask) {
        mImageTask = photoTask;
    }

    @Override
    public void run() {
        mImageTask.setDownloadThread(Thread.currentThread());
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);


        byte[] byteBuffer = mImageTask.getByteBuffer();

        try {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            if (null == byteBuffer) {
                mImageTask.handleDownloadState(STATE_STARTED);
                InputStream byteStream = null;

                try {

                    CommunicationsTask connection =
                            ClientApplication.getApplication().getCurrentBluetoothConnection();

                    connection.write("/photo/" + mImageTask.getImageNumber().toString());

                    if (Thread.interrupted()) {

                        throw new InterruptedException();
                    }
                    byteStream = connection.getBluetoothSocket().getInputStream();

                    if (Thread.interrupted()) {

                        throw new InterruptedException();
                    }

                    int contentSize = 0;

                    while (connection.isAvailable() > 0) {

                        byte[] imgsize = new byte[4];
                        int imageSize = connection.read(imgsize);
                        if(imageSize >0) {
                            ByteBuffer wrapped = ByteBuffer.wrap(imgsize);
                            contentSize = wrapped.getInt();
                        }
                        else
                            contentSize = -1;
                        /*
                         * For not yet available image
                         */
                        if (contentSize <0) {

                            // Allocates a temporary buffer
                            byte[] tempBuffer = new byte[READ_SIZE];
                            int bufferLeft = tempBuffer.length;
                            int bufferOffset = 0;
                            int readResult = 0;

                            outer:
                            do {
                                while (bufferLeft > 0) {

                                    readResult = byteStream.read(tempBuffer, bufferOffset,
                                            bufferLeft);

                                    if (readResult < 0) {
                                        break outer;
                                    }


                                    bufferOffset += readResult;
                                    bufferLeft -= readResult;

                                    if (Thread.interrupted()) {

                                        throw new InterruptedException();
                                    }
                                }

                                bufferLeft = READ_SIZE;
                                int newSize = tempBuffer.length + READ_SIZE;

                                byte[] expandedBuffer = new byte[newSize];
                                System.arraycopy(tempBuffer, 0, expandedBuffer, 0,
                                        tempBuffer.length);
                                tempBuffer = expandedBuffer;
                            } while (true);


                            byteBuffer = new byte[bufferOffset];
                            System.arraycopy(tempBuffer, 0, byteBuffer, 0, bufferOffset);
                        } else {
                            byteBuffer = new byte[contentSize];

                            // How much of the buffer still remains empty
                            int remainingLength = contentSize;
                            int bufferOffset = 0;

                            while (remainingLength > 0) {
                                int readResult = byteStream.read(
                                        byteBuffer,
                                        bufferOffset,
                                        remainingLength);

                                if (readResult < 0) {
                                    throw new EOFException();
                                }

                                bufferOffset += readResult;
                                remainingLength -= readResult;

                                if (Thread.interrupted()) {

                                    throw new InterruptedException();
                                }
                            }
                        }
                        if (Thread.interrupted()) {

                            throw new InterruptedException();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return;

                } finally {
                    if (null != byteStream) {
                        try {
                            //byteStream.close();
                        } catch (Exception e) {

                        }
                    }
                }
            }

            mImageTask.setByteBuffer(byteBuffer);
            mImageTask.handleDownloadState(STATE_COMPLETED);

        } catch (InterruptedException e1) {
            // Does nothing
        } finally {
            if (null == byteBuffer) {
                mImageTask.handleDownloadState(STATE_FAILED);
            }

            mImageTask.setDownloadThread(null);
            Thread.interrupted();
        }
    }

    interface TaskRunnableDownloadMethods {
        void setDownloadThread(Thread currentThread);
        byte[] getByteBuffer();
        void setByteBuffer(byte[] buffer);
        void handleDownloadState(int state);
        Integer getImageNumber();
    }
}