package com.huangyuanlove.liaoba.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.entity.Message;

import java.util.List;


/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/16
 */
public class MessageAdapter extends ArrayAdapter<Message> {

    private int resourceID;
    private Context context;
    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        resourceID = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Message msg = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            viewHolder.leftMessage = (TextView) view.findViewById(R.id.left_message);
            viewHolder.rightMessage = (TextView) view.findViewById(R.id.right_message);

            ((Activity)context).registerForContextMenu(viewHolder.leftMessage);
            viewHolder.rightMessage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (msg.getType() == Message.MSG_TYPE_RECEIVED) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMessage.setText(msg.getContent());
        } else if (msg.getType() == Message.MSG_TYPE_SENT) {
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightMessage.setText(msg.getContent());
        }

        return view;
    }

    class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMessage;
        TextView rightMessage;
    }


}
