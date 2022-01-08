package com.example.trt_figuresshop.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trt_figuresshop.Interface.ImageClickListener;
import com.example.trt_figuresshop.Model.EventBus.EventTinhTong;
import com.example.trt_figuresshop.Model.GioHang;
import com.example.trt_figuresshop.R;
import com.example.trt_figuresshop.Retrofit.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {

    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gio_hang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_gio_hang_ten.setText(gioHang.getTensp());
        holder.item_gio_hang_soluong.setText(gioHang.getSoluong() + " ");
        Glide.with(context).load(gioHang.getHinh()).into(holder.item_gio_hang_hinh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_gio_hang_gia.setText(decimalFormat.format((gioHang.getGiasp())) + "Đ");
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_gio_hang_gia_tong.setText(decimalFormat.format(gia));
        holder.setImageClickListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1) {
                    if (gioHangList.get(pos).getSoluong() > 1) {
                        int soluongmoi = gioHangList.get(pos).getSoluong() - 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                        holder.item_gio_hang_soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        holder.item_gio_hang_gia_tong.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new EventTinhTong());
                    }else if (gioHangList.get(pos).getSoluong() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông Báo");
                        builder.setMessage("Xóa sản phẩm khỏi giỏ hàng ? ? ?");
                        builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.arraygiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new EventTinhTong());
                            }
                        });
                        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }
                } else if (giatri == 2) {
                    if (gioHangList.get(pos).getSoluong() < 11) {
                        int soluongmoi = gioHangList.get(pos).getSoluong() + 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_gio_hang_soluong.setText(gioHangList.get(pos).getSoluong() + " ");
                    long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                    holder.item_gio_hang_gia_tong.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new EventTinhTong());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView item_gio_hang_hinh, item_gio_hang_them, item_gio_hang_xoa;
        TextView item_gio_hang_ten, item_gio_hang_gia, item_gio_hang_soluong, item_gio_hang_gia_tong;
        ImageClickListener imageClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_gio_hang_hinh = itemView.findViewById(R.id.item_gio_hang_hinh);
            item_gio_hang_ten = itemView.findViewById(R.id.item_gio_hang_ten);
            item_gio_hang_gia = itemView.findViewById(R.id.item_gio_hang_gia);
            item_gio_hang_soluong = itemView.findViewById(R.id.item_gio_hang_so_luong);
            item_gio_hang_gia_tong = itemView.findViewById(R.id.item_gio_hang_gia_tong);
            item_gio_hang_xoa = itemView.findViewById(R.id.item_gio_hang_xoa);
            item_gio_hang_them = itemView.findViewById(R.id.item_gio_hang_them);
            item_gio_hang_them.setOnClickListener(this);
            item_gio_hang_xoa.setOnClickListener(this);
        }

        public void setImageClickListener(ImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        @Override
        public void onClick(View v) {
            if (v == item_gio_hang_xoa) {
                imageClickListener.onImageClick(v, getAdapterPosition(), 1);
            } else if (v == item_gio_hang_them) {
                imageClickListener.onImageClick(v, getAdapterPosition(), 2);
            }
        }
    }
}
