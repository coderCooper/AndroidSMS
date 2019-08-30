package lollipop.com.sms.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lollipop.com.sms.R;

/**
 * Created by lollipop on 2018/11/6.
 */

public class SmsUserAdapter extends BaseAdapter {

    private Context mCon;
    private ArrayList<SmsFatherModel> smsBeans;

    public SmsUserAdapter(Context mCon, ArrayList<SmsFatherModel> smsBeans){
        this.mCon = mCon;
        this.smsBeans = smsBeans;
    }

    @Override
    public int getCount() {
        if (smsBeans != null){
            return smsBeans.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return smsBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView name;      // 姓名
        TextView content;       // 详细内容
        TextView time;       // 详细内容
        View readPoint;       // 是否已读
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCon).inflate(R.layout.item_sms, null);
            holder = new ViewHolder();
            holder.name = ((TextView) convertView.findViewById(R.id.tv00));
            holder.content = ((TextView) convertView.findViewById(R.id.tv01));
            holder.time = ((TextView) convertView.findViewById(R.id.tv02));
            holder.readPoint = convertView.findViewById(R.id.view0);
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }

        SmsFatherModel model = smsBeans.get(position);

        if (TextUtils.isEmpty(model.getName())){
            holder.name.setText(model.getPhone());
        } else {
            holder.name.setText(model.getName());
        }
        if (model.isUnRead()){
            holder.readPoint.setVisibility(View.VISIBLE);
            holder.content.setTextColor(mCon.getResources().getColor(R.color.color666666));
        } else {
            holder.readPoint.setVisibility(View.INVISIBLE);
            holder.content.setTextColor(mCon.getResources().getColor(R.color.color999999));
        }

        holder.content.setText(model.getContent());

        holder.time.setText(model.getTime());

        return convertView;
    }
}
