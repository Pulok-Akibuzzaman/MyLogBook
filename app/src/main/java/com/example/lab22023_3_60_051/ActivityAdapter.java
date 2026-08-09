package com.example.lab22023_3_60_051;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class ActivityAdapter extends ArrayAdapter<Contact> {

    private final Context context;
    private final ArrayList<Contact> values;

    public ActivityAdapter(@NonNull Context context, @NonNull ArrayList<Contact> items) {
        super(context, -1, items);
        this.context = context;
        this.values = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_contact, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        TextView tvDob = convertView.findViewById(R.id.tvDob);
        ImageView ivProfile = convertView.findViewById(R.id.ivProfileIcon); // Need to check ID in row_contact.xml

        Contact c = values.get(position);
        tvName.setText(c.name);
        tvPhone.setText(c.phone);
        tvDob.setText(c.dob);

        if (c.imageUri != null) {
            ivProfile.setImageURI(Uri.parse(c.imageUri));
            ivProfile.setPadding(0, 0, 0, 0); // Remove padding if image is set
            ivProfile.setImageTintList(null); // Remove blue tint for real photo
        } else {
            ivProfile.setImageResource(R.drawable.ic_person);
            ivProfile.setPadding(12, 12, 12, 12);
            ivProfile.setImageTintList(android.content.res.ColorStateList.valueOf(0xFF0052D4));
        }

        return convertView;
    }
}
