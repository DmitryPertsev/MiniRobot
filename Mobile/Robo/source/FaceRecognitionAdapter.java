package RoboBluetoothClient.androidApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class FaceRecognitionAdapter extends ArrayAdapter<FaceRecognition> {

    protected AdapterView.OnItemLongClickListener mOnItemLongClickListener;
    private LayoutInflater mInflater;
    private ArrayList<FaceRecognition> mSubjects;

    public FaceRecognitionAdapter(Context context, int resource, ArrayList<FaceRecognition> subjects) {
        super(context, resource, subjects);

        this.mSubjects = subjects;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOwner(Spinner widget)
    {
        this.mOwner = widget;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener){
        this.mOnItemLongClickListener = listener;
    }

    public void setAdapterEventsListener(
            FaceRecognitionAdapter.OnAdapterEventsListener EventsListener) {
        mEventsListener = EventsListener;
    }

    @Override
    public FaceRecognition getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getDropDownView(final int position, View convertView, final ViewGroup parent) {
        View row = mInflater.inflate(R.layout.face_item, parent, false);

        FaceRecognition subject = mSubjects.get(position);

        TextView name = row.findViewById(R.id.subject_name);
        ImageView image = row.findViewById(R.id.subject_image);

        name.setText("subject №" + subject.getName());
        image.setImageBitmap(subject.getBitmap());

        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mOwner.setSelection(position);
            }
        });

        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(mOnItemLongClickListener != null)
                    mOnItemLongClickListener.onItemLongClick((AdapterView<?>) parent, view, position, getItemId(position));
                return false;
            }
        });

        if (mEventsListener != null) {
            mEventsListener.onGetDropDownView(image, subject.getNumber());
        }

        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = mInflater.inflate(R.layout.face_item, parent, false);

        FaceRecognition subject = mSubjects.get(position);

        TextView name = row.findViewById(R.id.subject_name);
        ImageView image = row.findViewById(R.id.subject_image);

        name.setText(subject.getName());
        image.setImageBitmap(subject.getBitmap());
        name.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);

        if (mEventsListener != null) {
            mEventsListener.onGetView(image, subject.getNumber());
        }

        return row;
    }

    @Override
    public boolean areAllItemsEnabled() {
        //currently fix issue when opening view cause exception
        //return super.areAllItemsEnabled();
        return false;
    }

    public interface OnAdapterEventsListener {
        void onGetDropDownView(ImageView image, Integer number);
        void onGetView(ImageView image, Integer number);
    }

    private OnAdapterEventsListener mEventsListener;
    private Spinner mOwner;
}