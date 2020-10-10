package RoboBluetoothClient.androidApp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainMenuAdapter extends ArrayAdapter<MainMenuItem> {

    private LayoutInflater mInflater;
    private ArrayList<MainMenuItem> mSubjects;

    public MainMenuAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public MainMenuAdapter(Context context, int resource, ArrayList<MainMenuItem> subjects) {
        super(context, resource, subjects);

        this.mSubjects = subjects;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MainMenuItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getDropDownView(final int position, View convertView, final ViewGroup parent) {
        View row = mInflater.inflate(R.layout.main_menu_list_item, parent, false);

        MainMenuItem subject = mSubjects.get(position);

        TextView textView = row.findViewById(R.id.menu_item);

        textView.setText(subject.getTextString());
        textView.setCompoundDrawablesWithIntrinsicBounds(subject.getIconId(), 0, R.drawable.ic_navigate, 0);

        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = mInflater.inflate(R.layout.main_menu_list_item, parent, false);

        MainMenuItem subject = mSubjects.get(position);

        TextView textView = row.findViewById(R.id.menu_item);

        textView.setText(subject.getTextString());
        textView.setCompoundDrawablesWithIntrinsicBounds(subject.getIconId(), 0, R.drawable.ic_navigate, 0);

        return row;
    }
}
