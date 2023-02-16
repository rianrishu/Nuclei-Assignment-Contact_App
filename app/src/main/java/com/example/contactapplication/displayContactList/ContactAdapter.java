package com.example.contactapplication.displayContactList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapplication.Contact;
import com.example.contactapplication.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> contacts;

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.controller_contact_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
        holder.itemView.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), contact.getFullName(), Toast.LENGTH_SHORT).show();
            router.

        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView phoneNumberTextView;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_name);
            phoneNumberTextView = itemView.findViewById(R.id.tv_number);
            imageView = itemView.findViewById(R.id.profile_pic);
        }

        public void bind(Contact contact) {
            nameTextView.setText(contact.getFullName());
            phoneNumberTextView.setText(contact.getContactNumber());
            if (contact.getImage() != null) {
                imageView.setImageURI(contact.getImage());
            }
        }
    }
}
