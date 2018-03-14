package in.mizardar.kisanhubtest.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import in.mizardar.kisanhubtest.R;

/**
 * Created by anujr on 14-Mar-18.
 */

public class AdapterValues extends BaseAdapter {

    private String[] values;
    private Context context;
    private LayoutInflater inflater;

    public AdapterValues(String[] values, Context context) {
        this.values = values;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int i) {
        return values[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();

        if (view==null){
            view = inflater.inflate(R.layout.list_item,null,false);

            holder.key = (TextView) view.findViewById(R.id.key);
            holder.value = (TextView) view.findViewById(R.id.value);

            view.setTag(holder);
        }else
            holder = (Holder) view.getTag();

        String currentValue = values[position];
        String currentKey = StaticDataList.KEY_LIST[position];

        holder.key.setText(currentKey);
        holder.value.setText(currentValue);

        return view;
    }

    private class Holder{

        TextView key;
        TextView value;
    }

}
