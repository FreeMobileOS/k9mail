package com.fsck.k9.activity;


import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fsck.k9.R;
import com.fsck.k9.view.RecipientSelectView.Recipient;
import com.fsck.k9.helper.ContactPicture;


public class RecipientAdapter extends BaseAdapter implements Filterable {

    List<Recipient> recipients;
    boolean showCryptoStatus = true;
    Context context;

    public RecipientAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recipients == null ? 0 : recipients.size();
    }

    @Override
    public Recipient getItem(int position) {
        return recipients == null ? null : recipients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return recipients.get(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = newView(parent);
        }
        Recipient recipient = getItem(position);
        bindView(view, recipient);

        return view;
    }

    public View newView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipient_dropdown_item, parent, false);
        RecipientTokenHolder holder = new RecipientTokenHolder(view);
        view.setTag(holder);
        return view;
    }

    public void bindView(View view, Recipient recipient) {

        RecipientTokenHolder holder = (RecipientTokenHolder) view.getTag();

        holder.name.setText(recipient.address.getPersonal());

        String address = recipient.address.getAddress();
        holder.email.setText(address);

        setContactPhotoOrPlaceholder(context, holder.photo, recipient);

        if (!showCryptoStatus) {
            holder.cryptoStatus.setVisibility(View.GONE);
            return;
        }
        holder.cryptoStatus.setVisibility(View.VISIBLE);

        int cryptoStatusRes, cryptoStatusColor;
        if (recipient.cryptoStatus != null && recipient.cryptoStatus > 0) {
            if (recipient.cryptoStatus == 2) {
                cryptoStatusRes = R.drawable.status_lock_closed;
                cryptoStatusColor = context.getResources().getColor(R.color.openpgp_green);
            } else {
                cryptoStatusRes = R.drawable.status_lock_error;
                cryptoStatusColor = context.getResources().getColor(R.color.openpgp_orange);
            }
        } else {
            cryptoStatusRes = R.drawable.status_lock_open;
            cryptoStatusColor = context.getResources().getColor(R.color.openpgp_red);
        }
        holder.cryptoStatus.setImageResource(cryptoStatusRes);
        holder.cryptoStatus.setImageTintList(ColorStateList.valueOf(cryptoStatusColor));

    }

    public static void setContactPhotoOrPlaceholder(Context context, ImageView imageView, Recipient recipient) {
        imageView.setImageDrawable(null);
        if (recipient.photoThumbnailUri != null) {
            Glide.with(context).load(recipient.photoThumbnailUri).into(imageView);
        } else {
            ContactPicture.getContactPictureLoader(context).loadContactPicture(recipient.address, imageView);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (recipients == null) {
                    return null;
                }
                FilterResults result = new FilterResults();
                result.values = recipients;
                result.count = recipients.size();
                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    static class RecipientTokenHolder {
        TextView name, email;
        ImageView photo;
        ImageView cryptoStatus;

        public RecipientTokenHolder(View view) {
            name = (TextView) view.findViewById(R.id.text1);
            email = (TextView) view.findViewById(R.id.text2);
            photo = (ImageView) view.findViewById(R.id.contact_photo);
            cryptoStatus = (ImageView) view.findViewById(R.id.contact_crypto_status);
        }
    }

}
