package com.example.ask_me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{
   private RecyclerView recyclerview;
   private EditText input;
   private Button send;
   CustomAdapter customAdapter;
   List<message> messageList;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview=findViewById(R.id.recycleview);
        input=findViewById(R.id.input);
        send=findViewById(R.id.send);
        messageList = new ArrayList<>();

        customAdapter = new CustomAdapter(messageList);
        recyclerview.setAdapter(customAdapter);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerview.setLayoutManager(llm);
        addToChat("Hi there! How can I help you?","bot");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String ques = input.getText().toString();

                input.setText("");
                if(!ques.isEmpty()) {
                    addToChat(ques, "me");
                    callApi(ques);
                }
            }
        });

    }


    void addToChat(String message, String sentby){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new message(message,sentby));
                customAdapter.notifyDataSetChanged();
                recyclerview.smoothScrollToPosition(customAdapter.getItemCount());
            }
        });


    }

    void addResponse(String reply){
        addToChat(reply,"bot");
    }


    void callApi(String ques)  {
        JSONObject jsonbody = new JSONObject();
        try {
            jsonbody.put("model","gpt-3.5-turbo-instruct");
            jsonbody.put("prompt",ques);
            jsonbody.put("max_tokens",350);
            jsonbody.put("temperature",0.2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        RequestBody body = RequestBody.create(jsonbody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .addHeader("Authorization","Bearer ************************************************************")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response" );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                 if(response.isSuccessful()){
                     JSONObject jsonObject=null;
                     try {

                        jsonObject = new JSONObject(response.body().string());
                         JSONArray  jsonArray = jsonObject.getJSONArray("choices");
                         String result = jsonArray.getJSONObject(0).getString("text");
                         addResponse(result);
                     } catch (JSONException e) {
                         throw new RuntimeException(e);
                     }
                 }
                 else{
                     addResponse("Failed to load response ");
                 }
            }
        });
    }

}