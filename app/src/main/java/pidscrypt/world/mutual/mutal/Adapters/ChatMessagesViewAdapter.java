package pidscrypt.world.mutual.mutal.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

import pidscrypt.world.mutual.mutal.ImageViewActivity;
import pidscrypt.world.mutual.mutal.R;
import pidscrypt.world.mutual.mutal.api.AudioMessage;
import pidscrypt.world.mutual.mutal.api.ChatMessage;
import pidscrypt.world.mutual.mutal.api.ImageMessage;
import pidscrypt.world.mutual.mutal.api.MessageStatus;
import pidscrypt.world.mutual.mutal.api.MessageType;
import pidscrypt.world.mutual.mutal.api.MutualDateFormat;
import pidscrypt.world.mutual.mutal.api.MutualMessageViewTypes;
import pidscrypt.world.mutual.mutal.http.HttpCall;
import pidscrypt.world.mutual.mutal.http.HttpRequest;
import pidscrypt.world.mutual.mutal.media.Audio;

public class ChatMessagesViewAdapter extends FirestoreRecyclerAdapter<ChatMessage,RecyclerView.ViewHolder> {

    private Context mContext;
    private PowerManager.WakeLock wakeLock;
    private boolean ext_me , ext_other;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options query options
     */
    public ChatMessagesViewAdapter(@NonNull FirestoreRecyclerOptions<ChatMessage> options, Context context) {
        super(options);
        this.mContext = context;
    }




    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull ChatMessage model) {

        if(TextUtils.equals(model.getSenderId(), FirebaseAuth.getInstance().getUid())){
            switch(model.getMessageType()){
                case MessageType.TEXT:
                    configureSentMessageViewHolder((ChatMessageViewHolder) holder, position, model);
                    break;
                case MessageType.IMAGE:
                    configureSentImageViewHolder((ChatImageOutgoingViewHolder) holder, position, model);
                    break;
                case MessageType.AUDIO:
                    configureSentAudioViewHolder((ChatAudioOutgoingViewHolder) holder, position, model);
                    break;
                case MessageType.DOCUMENT:
                    configureSentDocumentViewHolder((ChatDocumentOutgoingViewHolder) holder, position, model);
                    break;
                case MessageType.CONTACT:
                    configureSentContactViewHolder((ChatContactOutgoingViewHolder) holder, position, model);
                    break;
            }

            ext_me = true; ext_other = false;

        }else{
            switch(model.getMessageType()){
                case MessageType.TEXT:
                    configureRecievedMessageViewHolder((OtherChatMessageViewHolder) holder, position, model);
                    break;
                case MessageType.IMAGE:
                    configureRecievedImageViewHolder((ChatImageIncomingViewHolder) holder, position, model);
                    break;
                case MessageType.AUDIO:
                    configureRecievedAudioViewHolder((ChatAudioIncomingViewHolder) holder, position, model);
                    break;
                case MessageType.DOCUMENT:
                    configureRecievedDocumentViewHolder((ChatDocumentIncomingViewHolder) holder, position, model);
                    break;
                case MessageType.CONTACT:
                    configureRecievedContactViewHolder((ChatContactIncomingViewHolder) holder, position, model);
                    break;
            }
            ext_me = false; ext_other = true;

        }
    }

    private void configureRecievedDocumentViewHolder(ChatDocumentIncomingViewHolder holder, int position, ChatMessage model) {
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));
        holder.filename.setText(model.getLocalFile());
        if(ext_other){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_incoming_normal_ext);
        }

    }

    private void configureSentDocumentViewHolder(ChatDocumentOutgoingViewHolder holder, int position, ChatMessage model) {
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));
        holder.filename.setText(model.getLocalFile());

        if(ext_me){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_outgoing_normal_ext);
        }

        switch (model.getMessageStatus()){
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_server_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_target_onmedia);
                break;
        }
    }

    private void configureSentContactViewHolder(ChatContactOutgoingViewHolder holder, int position, ChatMessage model) {
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));

        switch (model.getMessageStatus()){
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_server_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_target_onmedia);
                break;
        }

        holder.contact_name.setText(model.getMessage());
        holder.phone.setText(model.getLocalFile());

        if(ext_me){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_outgoing_normal_ext);
        }

    }

    private void configureRecievedContactViewHolder(ChatContactIncomingViewHolder holder, int position, ChatMessage model) {
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));

        holder.contact_name.setText(model.getMessage());
        holder.phone.setText(model.getLocalFile());

        if(ext_other){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_incoming_normal_ext);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_text_message,viewGroup,false);
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case MutualMessageViewTypes.TEXT_MESSAGE_OUTGOING:
                View viewTextIn = layoutInflater.inflate(R.layout.item_text_messsage_outgoing, viewGroup, false);
                viewHolder = new ChatMessageViewHolder(viewTextIn);
                break;
            case MutualMessageViewTypes.TEXT_MESSAGE_INCOMING:
                View viewTextOut = layoutInflater.inflate(R.layout.item_text_message_incoming, viewGroup, false);
                viewHolder = new OtherChatMessageViewHolder(viewTextOut);
                break;
            case MutualMessageViewTypes.IMAGE_MESSAGE_INCOMING:
                View viewImageIn = layoutInflater.inflate(R.layout.item_image_messsage_incoming, viewGroup, false);
                viewHolder = new ChatImageIncomingViewHolder(viewImageIn);
                break;
            case MutualMessageViewTypes.IMAGE_MESSAGE_OUTGOING:
                View viewImageOut = layoutInflater.inflate(R.layout.item_image_messsage_outgoing, viewGroup, false);
                viewHolder = new ChatImageOutgoingViewHolder(viewImageOut);
                break;
            case MutualMessageViewTypes.AUDIO_MESSAGE_OUTGOING:
                View viewAudioOut = layoutInflater.inflate(R.layout.item_audio_messsage_outgoing, viewGroup, false);
                viewHolder = new ChatAudioOutgoingViewHolder(viewAudioOut);
                break;
            case MutualMessageViewTypes.AUDIO_MESSAGE_INCOMING:
                View viewAudioIn = layoutInflater.inflate(R.layout.item_audio_messsage_incoming, viewGroup, false);
                viewHolder = new ChatAudioIncomingViewHolder(viewAudioIn);
                break;
            case MutualMessageViewTypes.DOCUMENT_MESSAGE_OUTGOING:
                View viewDocOut = layoutInflater.inflate(R.layout.item_message_document_outgoing, viewGroup, false);
                viewHolder = new ChatDocumentOutgoingViewHolder(viewDocOut);
                break;
            case MutualMessageViewTypes.DOCUMENT_MESSAGE_INCOMING:
                View viewDocIn = layoutInflater.inflate(R.layout.item_message_document_incoming, viewGroup, false);
                viewHolder = new ChatDocumentOutgoingViewHolder(viewDocIn);
                break;
            case MutualMessageViewTypes.CONTACT_MESSAGE_INCOMING:
                View viewContactIn = layoutInflater.inflate(R.layout.item_message_contact_incoming, viewGroup, false);
                viewHolder = new ChatContactOutgoingViewHolder(viewContactIn);
                break;
            case MutualMessageViewTypes.CONTACT_MESSAGE_OUTGOING:
                View viewContactOut = layoutInflater.inflate(R.layout.item_message_contact_outgoing, viewGroup, false);
                viewHolder = new ChatContactOutgoingViewHolder(viewContactOut);
                break;
            default:
                View viewNoneMessage = layoutInflater.inflate(R.layout.item_message_day,viewGroup, false);
                viewHolder = new NoneMessageViewHolder(viewNoneMessage);
                break;
        }
        return viewHolder;
        //return new ChatMessageViewHolder(view);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();

        if (this.getItemCount() > 1) {
            //mContext.getLayoutManager().smoothScrollToPosition(chat_messages_recycler, null, this.getItemCount() - 1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (TextUtils.equals(getItem(position).getSenderId(), FirebaseAuth.getInstance().getUid())) {
            switch (getItem(position).getMessageType()){
                case MessageType.TEXT:
                    type = MutualMessageViewTypes.TEXT_MESSAGE_OUTGOING;
                    break;
                case MessageType.IMAGE:
                    type = MutualMessageViewTypes.IMAGE_MESSAGE_OUTGOING;
                    break;
                case MessageType.AUDIO:
                    type = MutualMessageViewTypes.AUDIO_MESSAGE_OUTGOING;
                    break;
                case MessageType.DOCUMENT:
                    type = MutualMessageViewTypes.DOCUMENT_MESSAGE_OUTGOING;
                    break;
                case MessageType.CONTACT:
                    type = MutualMessageViewTypes.CONTACT_MESSAGE_OUTGOING;
                    break;
            }
        } else {
            switch (getItem(position).getMessageType()){
                case MessageType.TEXT:
                     type = MutualMessageViewTypes.TEXT_MESSAGE_INCOMING;
                     break;
                case MessageType.IMAGE:
                     type = MutualMessageViewTypes.IMAGE_MESSAGE_INCOMING;
                     break;
                case MessageType.AUDIO:
                    type = MutualMessageViewTypes.AUDIO_MESSAGE_INCOMING;
                    break;
                case MessageType.DOCUMENT:
                    type = MutualMessageViewTypes.DOCUMENT_MESSAGE_INCOMING;
                    break;
                case MessageType.CONTACT:
                    type = MutualMessageViewTypes.CONTACT_MESSAGE_INCOMING;
                    break;
            }
        }
        return type;
    }


    private void configureSentAudioViewHolder(final ChatAudioOutgoingViewHolder holder, int position, final ChatMessage model) {
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));

        switch (model.getMessageStatus()){
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_server_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_target_onmedia);
                break;
        }

        if(ext_me){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_outgoing_normal_ext);
        }

        holder.play_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                holder.audioFile = new Audio(model.getLocalFile()){
                    @Override
                    public void preparePlayback(MediaPlayer mediaPlayer) {
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(final MediaPlayer mediaPlayer) {
                                holder.audio_seekbar.setMax(mediaPlayer.getDuration());
                                holder.audio_seekbar.setProgress(mediaPlayer.getCurrentPosition());
                                holder.mediaPlayer = mediaPlayer;
                                if(holder.mediaPlayer.isPlaying()){
                                    Runnable seekbar_runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            preparePlayback(holder.mediaPlayer);
                                        }
                                    };

                                    holder.seekbar_handler.postDelayed(seekbar_runnable,1000);
                                }
                            }
                        });
                    }
                };
                holder.audioFile.startPlayback();
                holder.pause_btn.setVisibility(View.VISIBLE);
                holder.play_btn.setVisibility(View.GONE);
            }
        });

        holder.audio_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    holder.mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        holder.pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.audioFile.pausePlayback();
                holder.pause_btn.setVisibility(View.GONE);
                holder.play_btn.setVisibility(View.VISIBLE);
            }
        });
    }


    private void configureRecievedAudioViewHolder(final ChatAudioIncomingViewHolder holder, int position, final ChatMessage model) {
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));

        if(ext_other){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_incoming_normal_ext);
        }

        holder.download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // download file
                holder.download_progress.setIndeterminate(true);

                /*final ProgressDialog mProgress = new ProgressDialog(mContext);
                mProgress.setIndeterminate(true);
                mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgress.setCancelable(true);*/
                HttpCall httpCall = new HttpCall();
                httpCall.setMethodType(HttpCall.GET);
                httpCall.setUrl(model.getMessage());
                httpCall.setFOR_FILE(true);

                @SuppressLint("StaticFieldLeak") final HttpRequest downloadTask = new HttpRequest(){
                    @Override
                    public void requestResponse(String result) {
                        wakeLock.release();
                        //mProgress.dismiss();
                        holder.download_progress.setVisibility(View.GONE);

                        if(result != null){
                            Toast.makeText(mContext,"Download error "+result,Toast.LENGTH_SHORT).show();
                            holder.download_btn.setVisibility(View.VISIBLE);
                        } else {
                            holder.download_btn.setVisibility(View.GONE);
                            holder.play_btn.setVisibility(View.VISIBLE);
                            //@todo: update local msg message url in firestore
                        }
                    }

                    @Override
                    public void requestProgress(Integer... progress) {
                        holder.download_progress.setIndeterminate(false);
                        holder.download_progress.setMax(100);
                        holder.download_progress.setProgress(progress[0]);
                        /*mProgress.setIndeterminate(false);
                        mProgress.setMax(100);
                        mProgress.setProgress(progress[0]);*/
                    }

                    @Override
                    public void requestPreExecute() {
                        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
                        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,getClass().getName());
                        wakeLock.acquire();
                        //mProgress.show();
                        holder.download_progress.setVisibility(View.VISIBLE);
                        holder.download_btn.setVisibility(View.GONE);
                    }
                };

                downloadTask.execute(httpCall);

                /*mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        downloadTask.cancel(true);
                    }
                });*/
            }
        });

        //holder.audio_duration.setText(model.getMessage());
/*
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.avatar_contact)
                .error(R.drawable.avatar_contact)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(model.getMessage()).thumbnail(0.5f).into(image_message);*/
    }

    private void configureRecievedMessageViewHolder(OtherChatMessageViewHolder holder, int position, @NonNull ChatMessage model){
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));
        holder.message.setText(model.getMessage());

        if(ext_other){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_incoming_normal_ext);
        }
    }

    private void configureSentImageViewHolder(final ChatImageOutgoingViewHolder holder, int position, @NonNull final ChatMessage model){
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));

        if(ext_me){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_outgoing_normal_ext);
        }

        switch (model.getMessageStatus()){
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_server_onmedia);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET_ONMEDIA:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_target_onmedia);
                break;
        }

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.avatar_contact)
                .error(R.drawable.bg_outline_gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(model.getMessage()).thumbnail(0.5f).into(holder.message);

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] screenLocation = new int[2];
                view.getLocationOnScreen(screenLocation);
                Intent subActivity = new Intent(mContext, ImageViewActivity.class);
                int orientation = mContext.getResources().getConfiguration().orientation;
                subActivity.
                        putExtra("orientation", orientation).
                        putExtra("fileLocation", model.getMessage()).
                        putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", view.getWidth()).
                        putExtra("height", view.getHeight()).
                        putExtra("timeSent", model.getTime_sent()).
                        putExtra("sentBy", "Me");
                Activity activity = (Activity) mContext;
                activity.startActivity(subActivity);
                activity.overridePendingTransition(0,0);

            }
        });
    }

    private void configureRecievedImageViewHolder(ChatImageIncomingViewHolder holder, int position, @NonNull final ChatMessage model){
        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.avatar_contact)
                .error(R.drawable.bg_outline_gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(model.getMessage()).thumbnail(0.5f).into(holder.message);

        if(ext_other){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_incoming_normal_ext);
        }

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] screenLocation = new int[2];
                view.getLocationOnScreen(screenLocation);
                Intent subActivity = new Intent(mContext, ImageViewActivity.class);
                int orientation = mContext.getResources().getConfiguration().orientation;
                subActivity.
                        putExtra("orientation", orientation).
                        putExtra("fileLocation", model.getMessage()).
                        putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", view.getWidth()).
                        putExtra("height", view.getHeight()).
                        putExtra("timeSent", model.getTime_sent()).
                        putExtra("sentBy", "Me");
                Activity activity = (Activity) mContext;
                activity.startActivity(subActivity);
                activity.overridePendingTransition(0,0);

            }
        });
    }

    private void configureSentMessageViewHolder(ChatMessageViewHolder holder, int position, @NonNull ChatMessage model){

        holder.messageTime.setText(DateFormat.format(MutualDateFormat.SHORT,model.getTime_sent()));
        holder.message.setText(model.getMessage());
        switch (model.getMessageStatus()){
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_TARGET:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_target);
                break;
            case MessageStatus.MESSAGE_GOT_READ_RECIEPT_FROM_TARGET:
                holder.message_status.setImageResource(R.drawable.message_got_read_receipt_from_target);
                break;
            case MessageStatus.MESSAGE_GOT_RECIEPT_FROM_SERVER:
                holder.message_status.setImageResource(R.drawable.message_got_receipt_from_server);
                break;
            case MessageStatus.MESSAGE_UNSENT:
                holder.message_status.setImageResource(R.drawable.message_unsent);
                break;
        }

        if(ext_me){
            holder.wrapper.setBackgroundResource(R.drawable.balloon_outgoing_normal_ext);
        }

    }


    class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        TextView messageTime;
        ImageView message_status;
        RelativeLayout wrapper;

        ChatMessageViewHolder(View itemView) {
            super(itemView);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message = (TextView) itemView.findViewById(R.id.message);
            message_status = (ImageView) itemView.findViewById(R.id.message_status);
            wrapper = (RelativeLayout) itemView.findViewById(R.id.wrapper);
        }

    }

    class OtherChatMessageViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        TextView messageTime;
        RelativeLayout wrapper;

        OtherChatMessageViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message = (TextView) itemView.findViewById(R.id.message);
            wrapper = (RelativeLayout) itemView.findViewById(R.id.wrapper);

        }

    }

    class ChatImageOutgoingViewHolder extends RecyclerView.ViewHolder {

        ImageView message;
        TextView messageTime;
        ImageView message_status;
        RelativeLayout wrapper;

        ChatImageOutgoingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message = (ImageView) itemView.findViewById(R.id.message);
            message_status = (ImageView) itemView.findViewById(R.id.message_status);
            wrapper = (RelativeLayout) itemView.findViewById(R.id.wrapper);

        }

    }

    class ChatImageIncomingViewHolder extends RecyclerView.ViewHolder {

        ImageView message;
        TextView messageTime;
        RelativeLayout wrapper;

        ChatImageIncomingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message = (ImageView) itemView.findViewById(R.id.message);
            wrapper = (RelativeLayout) itemView.findViewById(R.id.wrapper);

        }

    }


    class ChatAudioIncomingViewHolder extends RecyclerView.ViewHolder {

        ImageView download_btn,play_btn;
        TextView messageTime, audio_duration;
        ProgressBar download_progress;
        LinearLayout wrapper;

        ChatAudioIncomingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            audio_duration = (TextView) itemView.findViewById(R.id.audio_duration);
            download_btn = (ImageView) itemView.findViewById(R.id.download_btn);
            play_btn = (ImageView) itemView.findViewById(R.id.play_btn);
            download_progress = (ProgressBar) itemView.findViewById(R.id.download_progress);
            wrapper = (LinearLayout) itemView.findViewById(R.id.wrapper);

        }

    }


    class ChatAudioOutgoingViewHolder extends RecyclerView.ViewHolder {

        ImageView message_status, play_btn, pause_btn;
        TextView messageTime, audio_duration;
        Audio audioFile;
        SeekBar audio_seekbar;
        Handler seekbar_handler;
        MediaPlayer mediaPlayer;
        LinearLayout wrapper;

        ChatAudioOutgoingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message_status = (ImageView) itemView.findViewById(R.id.message_status);
            audio_duration = (TextView) itemView.findViewById(R.id.audio_duration);
            play_btn = (ImageView) itemView.findViewById(R.id.play_btn);
            pause_btn = (ImageView) itemView.findViewById(R.id.pause_btn);
            audio_seekbar = (SeekBar) itemView.findViewById(R.id.audio_seekbar);
            wrapper = (LinearLayout) itemView.findViewById(R.id.wrapper);
        }

    }

    private class NoneMessageViewHolder extends RecyclerView.ViewHolder {

        TextView day;
        NoneMessageViewHolder(View viewNoneMessage) {
            super(viewNoneMessage);
            day = (TextView) viewNoneMessage.findViewById(R.id.message_day_text_view);
        }
    }

    private class ChatDocumentOutgoingViewHolder extends RecyclerView.ViewHolder {
        ImageView message_status;
        TextView messageTime, filename;
        LinearLayout wrapper;

        public ChatDocumentOutgoingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message_status = (ImageView) itemView.findViewById(R.id.message_status);
            filename = (TextView) itemView.findViewById(R.id.filename);
            wrapper = (LinearLayout) itemView.findViewById(R.id.wrapper);
        }
    }

    private class ChatDocumentIncomingViewHolder extends RecyclerView.ViewHolder {

        TextView messageTime, filename;
        LinearLayout wrapper;

        public ChatDocumentIncomingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            filename = (TextView) itemView.findViewById(R.id.filename);
            wrapper = (LinearLayout) itemView.findViewById(R.id.wrapper);
        }
    }


    private class ChatContactOutgoingViewHolder extends RecyclerView.ViewHolder {
        ImageView message_status;
        TextView messageTime, phone, contact_name;
        LinearLayout wrapper;

        public ChatContactOutgoingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message_status = (ImageView) itemView.findViewById(R.id.message_status);
            phone = (TextView) itemView.findViewById(R.id.phone_number);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            wrapper = (LinearLayout) itemView.findViewById(R.id.wrapper);
        }
    }

    private class ChatContactIncomingViewHolder extends RecyclerView.ViewHolder {
        TextView messageTime, phone, contact_name;
        LinearLayout wrapper;

        public ChatContactIncomingViewHolder(View itemView) {
            super(itemView);

            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            phone = (TextView) itemView.findViewById(R.id.phone_number);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            wrapper = (LinearLayout) itemView.findViewById(R.id.wrapper);
        }
    }
}
