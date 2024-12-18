package com.example.mobile_parking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

    public Context context;
    public ArrayList<ParkingModel> parkingList;
    public OnItemActionListener onItemActionListener;

    // Interface for handling item actions
    public interface OnItemActionListener {
        void onDelete(ParkingModel parking);
        void onEdit(ParkingModel parking);
    }

    // Constructor
    public ParkingAdapter(Context context, ArrayList<ParkingModel> parkingList, OnItemActionListener onItemActionListener) {
        this.context = context;
        this.parkingList = parkingList;
        this.onItemActionListener = onItemActionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingModel parking = parkingList.get(position);

        // Set the parking details
        holder.tvVehicleNumber.setText("Vehicle Number: " + parking.getVehicleNumber());
        holder.tvOwnerName.setText("Owner Name: " + parking.getOwnerName());
        holder.tvParkingDays.setText("Parking Days: " + parking.getParkingDays());
        holder.tvParkingNumber.setText("Parking Number: " + parking.getParkingNumber());
        holder.tvStartDate.setText("Start Date: " + parking.getStartDate());
        holder.tvEndDate.setText("End Date: " + parking.getEndDate());

        // Handle delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (onItemActionListener != null) {
                onItemActionListener.onDelete(parking);
            }
        });

        // Handle edit button click
        holder.btnEdit.setOnClickListener(v -> {
            if (onItemActionListener != null) {
                onItemActionListener.onEdit(parking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    // ViewHolder for RecyclerView items
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvVehicleNumber, tvOwnerName, tvParkingDays, tvParkingNumber, tvStartDate, tvEndDate;
        Button btnDelete, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Bind the views
            tvVehicleNumber = itemView.findViewById(R.id.tvVehicleNumber);
            tvOwnerName = itemView.findViewById(R.id.tvOwnerName);
            tvParkingDays = itemView.findViewById(R.id.tvParkingDays);
            tvParkingNumber = itemView.findViewById(R.id.tvParkingNumber);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
