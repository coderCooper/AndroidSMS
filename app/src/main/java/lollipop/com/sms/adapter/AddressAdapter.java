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

public class AddressAdapter extends BaseAdapter {

    private Context mCon;
    private ArrayList<SmsFatherModel> smsBeans;

    public AddressAdapter(Context mCon, ArrayList<SmsFatherModel> smsBeans){
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
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }
        final SmsFatherModel bean = smsBeans.get(position);
        holder.name.setText(bean.getName());

        ArrayList<SmsModel> sms = bean.getSms();
        if (sms != null && sms.size() > 0){
            SmsModel model = sms.get(sms.size() - 1);
            holder.content.setText(model.getContent());
            holder.time.setText(model.getTime());
            if (TextUtils.isEmpty(bean.getName())){
                if (TextUtils.isEmpty(model.getName())){
                    holder.name.setText(model.getPhone());
                } else {
                    holder.name.setText(model.getName());
                }
            }
        } else {
            holder.content.setText(null);
        }

        return convertView;
    }
}
