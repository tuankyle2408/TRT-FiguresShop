package com.example.trt_figuresshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trt_figuresshop.Model.ProductType;
import com.example.trt_figuresshop.R;

import java.util.List;

public class ProductTypeAdapter extends BaseAdapter {

    List<ProductType> array;
    Context context;

    public ProductTypeAdapter(Context context,List<ProductType> array) {
        this.array = array;
        this.context = context;

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public  class ViewHolder {
        TextView tensp;
        ImageView imageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_product,null);
            viewHolder.tensp = view.findViewById(R.id.product_name);
            viewHolder.imageView = view.findViewById(R.id.item_image);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tensp.setText(array.get(i).getTenfigure());
        Glide.with(context).load(array.get(i).getHinh()).into(viewHolder.imageView);

        return  view;
    }
}
