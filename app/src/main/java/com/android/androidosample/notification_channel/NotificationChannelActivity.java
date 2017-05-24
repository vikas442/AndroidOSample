package com.android.androidosample.notification_channel;

import android.app.NotificationChannel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.androidosample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationChannelActivity extends AppCompatActivity {

    private List<NotificationChannel> mNotificationChannels = new ArrayList<>();

    @BindView(R.id.list)
    RecyclerView mChannelsRecyclerView;

    private NotificationChannelAdapter mChannelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_channel);
        ButterKnife.bind(this);
        mChannelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChannelAdapter = new NotificationChannelAdapter(this, mNotificationChannels);
        mChannelsRecyclerView.setAdapter(mChannelAdapter);
    }

    @OnClick({R.id.btn_notification_channel, R.id.btn_list_notification_channel})
    public void generateNotificationForO(View view) {
        NotificationChannelHelper channelHelper;
        switch (view.getId()) {
            case R.id.btn_notification_channel:
                channelHelper = NotificationChannelHelper.getInstance(getApplicationContext());
                channelHelper.generateNotificationThroughChannel(getApplicationContext());
                break;
            case R.id.btn_list_notification_channel:
                channelHelper = NotificationChannelHelper.getInstance(getApplicationContext());
                mNotificationChannels.clear();
                mNotificationChannels.addAll(channelHelper.getNotificationChannels());
                mChannelAdapter.notifyDataSetChanged();
                break;
        }
    }

    class NotificationChannelAdapter extends RecyclerView.Adapter<NotificationChannelAdapter.NotificationChannelHolder> {

        private final LayoutInflater inflater;
        private final List<NotificationChannel> mChannels;

        private NotificationChannelAdapter(Context context, List<NotificationChannel> channels) {
            this.mChannels = channels;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public NotificationChannelAdapter.NotificationChannelHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.item_route, viewGroup, false);
            return new NotificationChannelAdapter.NotificationChannelHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationChannelAdapter.NotificationChannelHolder viewHolder, int i) {
            NotificationChannel channel = mChannels.get(i);
            viewHolder.textView.setText(channel.getName());
        }

        @Override
        public int getItemCount() {
            return mChannels.size();
        }

        class NotificationChannelHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            @BindView(android.R.id.text1)
            TextView textView;

            NotificationChannelHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
            }

            @Override
            public void onClick(@NonNull View v) {
                onChannelClicked(mChannels.get(getAdapterPosition()));
            }

            @Override
            public boolean onLongClick(View view) {
                onChannelLongClicked(mChannels.get(getAdapterPosition()));
                return true;
            }
        }
    }

    private void onChannelLongClicked(final NotificationChannel channel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.msg_delete_channel)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NotificationChannelHelper channelHelper = NotificationChannelHelper.getInstance(getApplicationContext());
                        channelHelper.deleteChannel(channel);
                        mNotificationChannels.clear();
                        mNotificationChannels.addAll(channelHelper.getNotificationChannels());
                        mChannelAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true);
        builder.show();
    }

    private void onChannelClicked(NotificationChannel channel) {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
    }
}
