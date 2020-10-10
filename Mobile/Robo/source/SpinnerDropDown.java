package RoboBluetoothClient.androidApp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class SpinnerDropDown extends Spinner {
    public SpinnerDropDown(Context context) {
        super(context);
    }

    private boolean mOpenInitiated = false;

    public SpinnerDropDown(Context context, int mode) {
        super(context, mode);
    }

    public SpinnerDropDown(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerDropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean performClick() {
        // register that the Spinner was opened so we have a status
        // indicator for when the container holding this Spinner may lose focus
        mOpenInitiated = true;
        if (mListener != null) {
            mListener.onSpinnerOpened(this);
        }
        return super.performClick();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasBeenOpened() && hasFocus) {
            performClosedEvent();
        }
    }

    public void setSpinnerEventsListener(
            OnSpinnerEventsListener onSpinnerEventsListener) {
        mListener = onSpinnerEventsListener;
    }

    public void performClosedEvent() {
        mOpenInitiated = false;
        if (mListener != null) {
            mListener.onSpinnerClosed(this);
        }
    }

    public boolean hasBeenOpened() {
        return mOpenInitiated;
    }

    public interface OnSpinnerEventsListener {
        void onSpinnerOpened(Spinner spinner);
        void onSpinnerClosed(Spinner spinner);
    }

    private OnSpinnerEventsListener mListener;
}