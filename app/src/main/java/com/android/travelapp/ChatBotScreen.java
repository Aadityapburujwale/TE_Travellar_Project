package com.android.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ChatBotScreen extends AppCompatActivity {

    private RecyclerView chatsRV;
    private ImageButton sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";

    private RequestQueue mRequestQueue;
    DecimalFormat df = new DecimalFormat("#.##");

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "e53301e27efa0b66d05045d91b2742d3";
    String weatherWords[] = new String[]{"wheather","weather","weater","wether"};
    String temperatureWords[] = new String[]{"temperature","temp","temperature","temp.","temparature"};
    String showMapWords[] = new String[]{"my location","my loc","temples","hotels","places to see","places","where i am?","where i am?","show my near by places","near by places","near by hotels","near by hotel","my location","my loc","my current loc","my current location","nearby hotels","nearby temples","my near by temples","temples near by me", "hotels near by me","temple near to me","hotel near to me","hotels near to me"};


    // creating a variable for array list and adapter class.
    private ArrayList<ChatBotModel> messageModalArrayList;
    private ChatBotAdapter chatBotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_screen);

        chatsRV = findViewById(R.id.idRVChats);
        sendMsgIB = findViewById(R.id.idIBSend);
        userMsgEdt = findViewById(R.id.idEdtMessage);

        // below line is to initialize our request queue.
        mRequestQueue = Volley.newRequestQueue(ChatBotScreen.this);
        mRequestQueue.getCache().clear();

        // creating a new array list
        messageModalArrayList = new ArrayList<>();

        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the message entered
                // by user is empty or not.
                if (userMsgEdt.getText().toString().isEmpty()) {
                    // if the edit text is empty display a toast message.
                    Toast.makeText(ChatBotScreen.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                    return;
                }

                // calling a method to send message
                // to our bot to get response.
                sendMessage(userMsgEdt.getText().toString());

                // below line we are setting text in our edit text as empty
                userMsgEdt.setText("");
            }
        });

        // on below line we are initialing our adapter class and passing our array list to it.
        chatBotAdapter = new ChatBotAdapter(messageModalArrayList, this);

        // below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatBotScreen.this, RecyclerView.VERTICAL, false);

        // below line is to set layout
        // manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);

        // below line we are setting
        // adapter to our recycler view.
        chatsRV.setAdapter(chatBotAdapter);


        messageModalArrayList.add(new ChatBotModel("Hello, How are you?", BOT_KEY));
        chatBotAdapter.notifyDataSetChanged();

    }

    private void sendMessage(String userMsg) {
        // below line is to pass message to our
        // array list which is entered by the user.
        messageModalArrayList.add(new ChatBotModel(userMsg, USER_KEY));
        chatBotAdapter.notifyDataSetChanged();

        // url for our brain
        // make sure to add mshape for uid.
        // make sure to add your url.

        int listSize = messageModalArrayList.size();
        for(String str:weatherWords){
            if(userMsg.toLowerCase().contains(str)){

                checkWheather(userMsg);
                break;
            }
        }

        if(listSize!=messageModalArrayList.size()){
            return;
        }

        for(String str:temperatureWords){
            if(userMsg.toLowerCase().contains(str)){

                checkTemp(userMsg);
                break;
            }
        }

        if(listSize!=messageModalArrayList.size()){
            return;
        }

        for(String str:showMapWords){
            if(userMsg.toLowerCase().contains(str)){
                messageModalArrayList.add(new ChatBotModel("You are redirected to map", BOT_KEY));
                chatBotAdapter.notifyDataSetChanged();
            showMap(str);
            break;
            }
        }

        if(listSize!=messageModalArrayList.size()){
            return;
        }

        String url = "http://api.brainshop.ai/get?bid=164965&key=VlAvKqtPeAawuJro&uid=aadityapb&msg=" + userMsg;

        // creating a variable for our request queue.
        RequestQueue queue = Volley.newRequestQueue(ChatBotScreen.this);

        // on below line we are making a json object request for a get request and passing our url .
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // in on response method we are extracting data
                    // from json response and adding this response to our array list.

                    String botResponse = response.getString("cnt");
                    messageModalArrayList.add(new ChatBotModel(botResponse, BOT_KEY));

                    // notifying our adapter as data changed.
                    chatBotAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();

                    // handling error response from bot.
                    messageModalArrayList.add(new ChatBotModel("No response", BOT_KEY));
                    chatBotAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error handling.
                Log.e("Volley-Error",error.toString());
                messageModalArrayList.add(new ChatBotModel("you are offline! I can't help you.", BOT_KEY));
                chatBotAdapter.notifyDataSetChanged();
                //Toast.makeText(ChatBotScreen.this, "No response from the bot..", Toast.LENGTH_SHORT).show();
            }
        });

        // at last adding json object
        // request to our queue.
        queue.add(jsonObjectRequest);

        // to scroll recycler view to last message
        chatsRV.smoothScrollToPosition(messageModalArrayList.size());
    }

    private void checkTemp(String userMsg) {
        String tempUrl = "";
        String cities[] = userMsg.split(" ");

        for(String city:cities){
            tempUrl = url + "?q=" + city + "&appid=" + appid;

            Set<String> set = new HashSet<>();
            set.add("temp");
            set.add("see");
            set.add("is");
            set.add("of");
            set.add("show");
            set.add("me");
            if(set.contains(city)) continue;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                       // String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        String temp = df.format(jsonObjectMain.getDouble("temp") - 273.15) + " °C";
                        messageModalArrayList.add(new ChatBotModel(temp, BOT_KEY));
                        //messageModalArrayList.add(new ChatBotModel(description, BOT_KEY));

                        // notifying our adapter as data changed.
                        chatBotAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getApplicationContext(), "Enter Valid City Name.", Toast.LENGTH_SHORT).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

    }

    private void checkWheather(String userMsg) {
        String tempUrl = "";
        String cities[] = userMsg.split(" ");

        for(String city:cities){
                tempUrl = url + "?q=" + city + "&appid=" + appid;

                Set<String> set = new HashSet<>();
                set.add("temp");
                set.add("see");
                set.add("is");
                set.add("of");
                set.add("show");
                set.add("me");
                if(set.contains(city)) continue;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String output = "";
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String description = jsonObjectWeather.getString("description");
                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            String temp = df.format(jsonObjectMain.getDouble("temp") - 273.15) + " °C";
                            messageModalArrayList.add(new ChatBotModel(description, BOT_KEY));
                            // notifying our adapter as data changed.
                            chatBotAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener(){

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(getApplicationContext(), "Enter Valid City Name.", Toast.LENGTH_SHORT).show();
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }

    }


    private void showMap(String location){

                Uri uri = Uri.parse("geo:0,0?q="+location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if(mapIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(mapIntent);
                }
    }

}