package com.quy.buvape.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quy.buvape.R;
import com.quy.buvape.api.BaseLink;
import com.quy.buvape.api.LoginQrApi;
import com.quy.buvape.api.UserApi;
import com.quy.buvape.model.Author;
import com.quy.buvape.model.Message;
import com.quy.buvape.model.MessageUser;
import com.quy.buvape.model.User;
import com.quy.buvape.model.User_Token;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestingSendMessage extends AppCompatActivity {
    private DialogsList dialogsList;
    protected DialogsListAdapter<MessageUser> dialogsListAdapter;

    private Retrofit retrofit;
    private UserApi userApi;
    private LoginQrApi loginApi;
    private Button button;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(BaseLink.BASE_URL_WEB);
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_send_message);
        dialogsList = findViewById(R.id.dialogsList);


        retrofit = new Retrofit.Builder()
                .baseUrl(BaseLink.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);

        dialogsListAdapter = new DialogsListAdapter<>(R.layout.item_dialog, new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Glide.with(TestingSendMessage.this).load(url).into(imageView);
            }
        });
        dialogsList.setAdapter(dialogsListAdapter);

//        try {
//            getMessage();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        // button = findViewById(R.id.button);
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BaseLink.BASE_URL_WEB)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        loginApi = retrofit.create(LoginQrApi.class);
//
//        Call<String> call = loginApi.testCall();
//
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
        //mSocket.connect();
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //mSocket.emit("kaka",1000);
//
//            }
//
//        });



    }

//    private void getMessage() throws IOException, JSONException {
//        String idCurrentUser = getSharedPreferences("LOGIN", Context.MODE_PRIVATE).getString("id","");
//        URL url = new URL("http://192.168.1.2:3002/user/" + idCurrentUser);
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//        int responseCode = con.getResponseCode();
//        System.out.println("GET Response Code : " + responseCode);
//        if (responseCode == HttpURLConnection.HTTP_OK) { // success
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            JsonParser jsonParser = new JsonParser();
//            JSONArray jsonArray = new JSONArray(response.toString());
//            Log.e("DEEE", jsonArray.length()+"");
//            for (int i = 0; i< jsonArray.length();i++){
//                Object obj = jsonArray.get(i);
//                JSONObject jsonObject = (JSONObject) obj;
//                Log.e("DATA", jsonObject.toString());
//                if (jsonObject.has("type")){
//                    List<Author> authors = new ArrayList<>();
//                    JSONArray jsonReceivers = jsonObject.getJSONArray("receiver");
//                    for (int j = 0; j < jsonReceivers.length();j++){
//                        JSONObject receiver = (JSONObject) jsonReceivers.get(j);
//                        authors.add(new Author(receiver.getString("id"),receiver.getString("name"),"https://uifaces.co/our-content/donated/gPZwCbdS.jpg"));
//                    }
//                    JSONArray jsonMessages = jsonObject.getJSONArray("messages");
//                    JSONObject jsonMessage = (JSONObject) jsonMessages.get(jsonMessages.length()-1);
//                    Message message = new Message(new Author(jsonMessage.getString("id"),"https://uifaces.co/our-content/donated/gPZwCbdS.jpg"),jsonMessage.getString("id_gen"),jsonMessage.getString("message"),new Date());
//                    int un_readMessage = 0;
//                    if (!idCurrentUser.equalsIgnoreCase(message.getUser().getId())){
//                        un_readMessage = jsonObject.getInt("un_read");
//                    }
//
//                    MessageUser messageUser = new MessageUser(jsonObject.getString("maso"),authors,message,un_readMessage,jsonObject.getString("name"),"https://avatar.oxro.io/avatar.svg?name="+jsonObject.getString("name")+"&caps=1");
//                    dialogsListAdapter.addItem(messageUser);
//
//                }else {
//                    List<Author> authors = new ArrayList<>();
//                    String receiverId = jsonObject.getString("receiver");
//                    String senderId = jsonObject.getString("sender");
//                    authors.add(new Author(receiverId,"https://uifaces.co/our-content/donated/gPZwCbdS.jpg"));
//                    authors.add(new Author(senderId,"https://uifaces.co/our-content/donated/gPZwCbdS.jpg"));
//                    JSONArray jsonMessages = jsonObject.getJSONArray("messages");
//                    JSONObject jsonMessage = (JSONObject) jsonMessages.get(jsonMessages.length()-1);
//                    Message message = new Message(new Author(jsonMessage.getString("id"),""),jsonMessage.getString("id_gen"),jsonMessage.getString("message"),new Date());
//                    if (idCurrentUser.equalsIgnoreCase(receiverId)){
//                        Call<User> call = userApi.getUserById(senderId);
//                        call.enqueue(new Callback<User>() {
//                            @Override
//                            public void onResponse(Call<User> call, Response<User> response) {
//                                if(!response.isSuccessful()){
//                                    Log.e("CODE1",response.code()+"");
//                                    Toasty.error(TestingSendMessage.this, "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
//                                    return;
//                                }
//
//                                User user = response.body();
//                                Log.e("info", user.toString());
//                                MessageUser messageUser = null;
//                                try {
//                                    int un_readMessage = 0;
//                                    if (!idCurrentUser.equalsIgnoreCase(message.getUser().getId())){
//                                        un_readMessage = jsonObject.getInt("un_read");
//                                    }
//                                    messageUser = new MessageUser(jsonObject.getString("maso"),authors,message,un_readMessage,user.getFirstname()+ " " + user.getLastname(),"https://uifaces.co/our-content/donated/gPZwCbdS.jpg");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                dialogsListAdapter.addItem(messageUser);
//                            }
//                            @Override
//                            public void onFailure(Call<User> call, Throwable t) {
//                                Log.e("CODE_FAILURE",t.getMessage()+"");
//
//                                Toasty.error(TestingSendMessage.this, "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
//                            }
//                        });
//                    }else{
//                        Call<User> call = userApi.getUserById(receiverId);
//                        call.enqueue(new Callback<User>() {
//                            @Override
//                            public void onResponse(Call<User> call, Response<User> response) {
//                                if(!response.isSuccessful()){
//                                    Log.e("CODE1",response.code()+"");
//                                    Toasty.error(TestingSendMessage.this, "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
//                                    return;
//                                }
//
//                                User user = response.body();
//                                Log.e("info", user.toString());
//                                MessageUser messageUser = null;
//
//                                try {
//                                    int un_readMessage = 0;
//                                    if (!idCurrentUser.equalsIgnoreCase(message.getUser().getId())){
//                                        un_readMessage = jsonObject.getInt("un_read");
//                                    }
//                                    messageUser = new MessageUser(jsonObject.getString("maso"),authors,message,un_readMessage,user.getFirstname()+ " " + user.getLastname(),"https://uifaces.co/our-content/donated/gPZwCbdS.jpg");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                dialogsListAdapter.addItem(messageUser);
//                            }
//                            @Override
//                            public void onFailure(Call<User> call, Throwable t) {
//                                Log.e("CODE_FAILURE",t.getMessage()+"");
//
//                                Toasty.error(TestingSendMessage.this, "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
//                            }
//                        });
//                    }
//                }
//
//            }
//
//            dialogsListAdapter.notifyDataSetChanged();
//            in.close();
//        }
//    }

}