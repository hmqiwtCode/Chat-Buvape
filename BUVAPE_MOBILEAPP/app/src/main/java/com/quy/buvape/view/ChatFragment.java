package com.quy.buvape.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quy.buvape.R;
import com.quy.buvape.api.BaseLink;
import com.quy.buvape.api.UserApi;
import com.quy.buvape.model.Author;
import com.quy.buvape.model.Message;
import com.quy.buvape.model.MessageUser;
import com.quy.buvape.model.User;
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


public class ChatFragment extends Fragment {
    private DialogsList dialogsList;
    protected DialogsListAdapter<MessageUser> dialogsListAdapter;

    private Retrofit retrofit;
    private UserApi userApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogsList = view.findViewById(R.id.dialogsList);
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseLink.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApi.class);

        dialogsListAdapter = new DialogsListAdapter<>(R.layout.item_dialog, new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Glide.with(getContext()).load(url).into(imageView);
            }
        });
        dialogsList.setAdapter(dialogsListAdapter);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//
        try {
            getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialogsListAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<MessageUser>() {
            @Override
            public void onDialogClick(MessageUser dialog) {
                Toast.makeText(getActivity(), dialog.getId()+"", Toast.LENGTH_SHORT).show();

                if (dialog.getTypeMessage().equalsIgnoreCase("group")){
                    Intent intent = new Intent(getActivity(),ChatItemActivity.class);
                    intent.putExtra("name_group",dialog.getDialogName());
                    intent.putExtra("idMessage",dialog.getId());
                    startActivity(intent);

                }else{
                    Intent chatOneIntent = new Intent(getActivity(),ChatActivity.class);
                    List<Author> authorList = (List<Author>) dialog.getUsers();

                    if (getActivity().getSharedPreferences("LOGIN",getActivity().MODE_PRIVATE).getString("id",null).equalsIgnoreCase(authorList.get(0).getId())){
                        chatOneIntent.putExtra("friend_id",authorList.get(1).getId());
                    }else{
                        chatOneIntent.putExtra("friend_id",authorList.get(0).getId());
                    }
                    chatOneIntent.putExtra("friends_name",dialog.getDialogName());
                    startActivity(chatOneIntent);
                }

            }
        });

    }


    private void getMessage() throws IOException, JSONException {
        List<MessageUser> listMessageUser = new ArrayList<>();
        String idCurrentUser = getContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE).getString("id", "");
        URL url = new URL(BaseLink.BASE_URL_CHAT + "/user/" + idCurrentUser);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            try {
                JSONArray jsonArray = new JSONArray(response.toString());
                Log.e("DEEE", jsonArray.length() + "");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Object obj = jsonArray.get(i);
                    JSONObject jsonObject = (JSONObject) obj;
                    Log.e("DATA", jsonObject.toString());
                    if (jsonObject.has("type")) {
                        List<Author> authors = new ArrayList<>();
                        JSONArray jsonReceivers = jsonObject.getJSONArray("receiver");
                        for (int j = 0; j < jsonReceivers.length(); j++) {
                            JSONObject receiver = (JSONObject) jsonReceivers.get(j);
                            authors.add(new Author(receiver.getString("id"), receiver.getString("name"), "https://uifaces.co/our-content/donated/gPZwCbdS.jpg"));
                        }
                        JSONArray jsonMessages = jsonObject.getJSONArray("messages");
                        JSONObject jsonMessage = (JSONObject) jsonMessages.get(jsonMessages.length() - 1);
                        Message message = new Message(new Author(jsonMessage.getString("id"), "https://uifaces.co/our-content/donated/gPZwCbdS.jpg"), jsonMessage.getString("id_gen"), jsonMessage.getString("message"), new Date());
                        int un_readMessage = 0;
                        if (!idCurrentUser.equalsIgnoreCase(message.getUser().getId())) {
                            un_readMessage = jsonObject.getInt("un_read");
                        }

                        MessageUser messageUser = new MessageUser(jsonObject.getString("maso"), authors, message, un_readMessage, jsonObject.getString("name"), "https://avatar.oxro.io/avatar.svg?name=" + jsonObject.getString("name") + "&caps=1","group");
                        dialogsListAdapter.addItem(messageUser);

                    } else {
                        List<Author> authors = new ArrayList<>();
                        String receiverId = jsonObject.getString("receiver");
                        String senderId = jsonObject.getString("sender");
                        Author receiver = new Author(receiverId, "https://uifaces.co/our-content/donated/gPZwCbdS.jpg");
                        Author sender = new Author(senderId, "https://uifaces.co/our-content/donated/gPZwCbdS.jpg");
                        authors.add(receiver);
                        authors.add(sender);
                        JSONArray jsonMessages = jsonObject.getJSONArray("messages");
                        JSONObject jsonMessage = (JSONObject) jsonMessages.get(jsonMessages.length() - 1);
                        Message message = new Message(new Author(jsonMessage.getString("id"), ""), jsonMessage.getString("id_gen"), jsonMessage.getString("message"), new Date());
                        Log.e("HUHULOI", "VODAY");
                        if (idCurrentUser.equalsIgnoreCase(receiverId)) {
                            Log.e("HUHULOI", "VODAY1");
                            Call<User> call = userApi.getUserById(senderId);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (!response.isSuccessful()) {
                                        Log.e("CODE1", response.code() + "");
                                        Toasty.error(getContext(), "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
                                        return;
                                    }

                                    User user = response.body();
                                    sender.setName(user.getFirstname() + " " + user.getLastname());
                                    Log.e("info", user.toString());
                                    MessageUser messageUser = null;
                                    try {
                                        int un_readMessage = 0;
                                        if (!idCurrentUser.equalsIgnoreCase(message.getUser().getId())) {
                                            un_readMessage = jsonObject.getInt("un_read");
                                        }
                                        messageUser = new MessageUser(jsonObject.getString("maso"), authors, message, un_readMessage, user.getFirstname() + " " + user.getLastname(), "https://uifaces.co/our-content/donated/gPZwCbdS.jpg","one");
                                        listMessageUser.add(messageUser);

                                        System.out.println("MEOHIEU GI LUON" + "FLLLLLLLLLLLLLL");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                      dialogsListAdapter.addItem(messageUser);
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.e("CODE_FAILURE1", t.getMessage() + "");

                                    //   Toasty.error(getContext(), "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
                                }
                            });
                        } else {
                            Call<User> call = userApi.getUserById(receiverId);
                            Log.e("HUHULOI", "VODAY2");
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (!response.isSuccessful()) {
                                        Log.e("CODE1", response.code() + "");
                                        // Toasty.error(getContext(), "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
                                        return;
                                    }
                                    Log.e("HUHULOI", "VODAY3");
                                    User user = response.body();
                                    receiver.setName(user.getFirstname() + " " + user.getLastname());
                                    Log.e("info", user.toString());
                                    MessageUser messageUser = null;

                                    try {
                                        int un_readMessage = 0;
                                        if (!idCurrentUser.equalsIgnoreCase(message.getUser().getId())) {
                                            un_readMessage = jsonObject.getInt("un_read");
                                        }
                                        messageUser = new MessageUser(jsonObject.getString("maso"), authors, message, un_readMessage, user.getFirstname() + " " + user.getLastname(), "https://uifaces.co/our-content/donated/gPZwCbdS.jpg","one");
                                        listMessageUser.add(messageUser);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                      dialogsListAdapter.addItem(messageUser);

                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.e("CODE_FAILURE2", t.getMessage() + "");

                                    //  Toasty.error(getContext(), "Login Failed.Please Try Again!!!", Toast.LENGTH_SHORT, true).show();
                                }
                            });
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}