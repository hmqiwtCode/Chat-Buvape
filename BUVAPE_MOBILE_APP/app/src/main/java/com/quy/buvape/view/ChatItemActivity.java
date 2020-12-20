package com.quy.buvape.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.quy.buvape.R;
import com.quy.buvape.api.BaseLink;
import com.quy.buvape.api.MessageApi;
import com.quy.buvape.model.Author;
import com.quy.buvape.model.ItemMessage;
import com.quy.buvape.model.ItemMessageWrapper;
import com.quy.buvape.model.Message;
import com.quy.buvape.model.SocketIOClient;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatItemActivity extends AppCompatActivity {
    private Socket mSocket;
    private MessagesList messagesList;
    private MessageInput input;
    private MessagesListAdapter<Message> adapter;
    private Author authorSender;
    private Retrofit retrofit;
    private MessageApi messageApi;
    private String idMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_item);
        idMessage = getIntent().getStringExtra("idMessage");

        messagesList = findViewById(R.id.messagesList);
        input = findViewById(R.id.input);
        authorSender = new Author(getSharedPreferences("LOGIN",MODE_PRIVATE).getString("id",null),getSharedPreferences("LOGIN",MODE_PRIVATE).getString("name",null),"123");

        mSocket = SocketIOClient.getInstance(this);
        mSocket.connect();
        mSocket.emit("addJoin", authorSender.getId()); // send ID current user to server to save to array



        adapter = new MessagesListAdapter<>(authorSender.getId(), null);
        messagesList.setAdapter(adapter);
        getOldMessage();
        setupBar();
        chatSender();
        chatTime();
    }

    private void setupBar(){
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getIntent().getStringExtra("name_group"));
        ab.setBackgroundDrawable(getDrawable(R.color.barLoadingScreen));
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.barLoadingScreen));
    }


    private void chatTime(){
        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    System.out.println(args[0].toString());
                    JSONObject jsonObject = new JSONObject(args[0].toString());
                    Log.e("Data",jsonObject.getString("message"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                adapter.addToStart(new Message(new Author(jsonObject.getString("sender"),"KKAKA","ggug"),"huhu",jsonObject.getString("message")
                                        ,new Date()), true);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void chatSender(){
        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                adapter.addToStart(new Message(authorSender,"s",input.toString()
                        ,new Date()), true);
                JSONObject json = new JSONObject();
                LocalTime time = LocalTime.now();
                String t = time.format(DateTimeFormatter.ofPattern("HH:mm"));
                try {
                    json.put("sender",authorSender.getId());
                    json.put("idMessage",idMessage);
                    json.put("message",input.toString());
                    json.put("user",authorSender.getName());
                    json.put("time",t);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("addGroup",json);
                return true;
            }
        });
    }

    private void getOldMessage(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseLink.BASE_URL_CHAT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        messageApi = retrofit.create(MessageApi.class);

        Call<ItemMessageWrapper> call = messageApi.getMessageGroup(idMessage);
        call.enqueue(new Callback<ItemMessageWrapper>() {
            @Override
            public void onResponse(Call<ItemMessageWrapper> call, Response<ItemMessageWrapper> response) {
                if(!response.isSuccessful()){
                    Log.e("CODE1",response.code()+"" + response.message());
                    return;
                }


                Log.e("VODAYCHUNHI",response.body().getItemMessages().size()+"");
                Log.e("DDDDD",response.toString());
                List<ItemMessage> listItemMessage = response.body().getItemMessages();
                listItemMessage.forEach(mess ->{
//                    String m = LocalDate.now().atTime(LocalTime.parse(mess.getTime()))
//                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = null;
//                    try {
//                        date = sdf.parse(m);
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }

                    adapter.addToStart(new Message(new Author(mess.getId()),mess.getId_gen(),mess.getMessage(),new Date()), true);
                });


            }

            @Override
            public void onFailure(Call<ItemMessageWrapper> call, Throwable t) {
                Log.e("CODE_FAILURE",t.getMessage()+"");

            }
        });


    }

}