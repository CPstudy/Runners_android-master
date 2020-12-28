package com.guk2zzada.runnerswar;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class RoomlistActivity extends AppCompatActivity {

    CreateAsync async;
    ArrayList<RoomList> arrRoom = new ArrayList<>();
    ListAdapter adapter;

    ListView list;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomlist);

        list = findViewById(R.id.list);
        btnCreate = findViewById(R.id.btnCreate);

        adapter = new ListAdapter(this, arrRoom, R.layout.item_roomlist);
        list.setAdapter(adapter);

        getAllList(getApplicationContext());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CognitoUserPool userPool = new CognitoUserPool(getApplicationContext(), new AWSConfiguration(getApplicationContext()));
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = ServerManager.AWS_URL;

                final String json = "{\"entry1\":\"2mb\"," +
                        "\"entry2\":null," +
                        "\"entry1_score\":\"3333\"," +
                        "\"entry2_score\":null," +
                        "\"total_dist\":\"1\"," +
                        "\"entry1_dist\":\"0\"," +
                        "\"entry2_dist\":\"0\"," +
                        "\"end_game\":false" +
                        "}";

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("body", json);

                            return params;
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            byte[] body = new byte[0];
                            body = json.getBytes();

                            return body;
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("x-api-key", ServerManager.API_KEY);
                            headers.put("Content-type", "application/json");
                            headers.put("accept-encoding", "gzip, deflate");

                            return headers;
                        }
                    };
                    queue.add(request);
            }
        });
    }

    public ArrayList<RoomList> getList(Context context) {
        //ArrayList<RoomList> arrRoom = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ServerManager.AWS_URL + "?id=4d9a76d0-6e14-11e8-afaf-b1047b398992";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                JSONObject json = response;
                                Log.e("json_string", json.toString());
                                String entry1 = json.getString("entry1").replace("\"", "");
                                String entry2 = json.getString("entry2").replace("\"", "");
                                String entry1_dist = json.getString("entry1_dist").replace("\"", "");
                                String entry2_dist = json.getString("entry2_dist").replace("\"", "");
                                String entry1_score = json.getString("entry1_score").replace("\"", "");
                                String entry2_score = json.getString("entry2_score").replace("\"", "");
                                String updatedAt = json.getString("updatedAt").replace("\"", "");
                                String createdAt = json.getString("createdAt").replace("\"", "");
                                String id = json.getString("id").replace("\"", "");
                                boolean end_game = json.getBoolean("end_game");

                                arrRoom.add(new RoomList(entry1, entry2, entry1_dist, entry2_dist, entry1_score, entry2_score, updatedAt, createdAt, id, end_game));
                                Log.e("json_id", json.getString("id"));

                                adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", ServerManager.API_KEY);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);

        return arrRoom;
    }

    public ArrayList<RoomList> getAllList(Context context) {
        //ArrayList<RoomList> arrRoom = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ServerManager.AWS_URL;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.e("json_array", response.toString());
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject json = response.getJSONObject(i);
                                Log.e("json_string", json.toString());
                                String entry1 = json.getString("entry1").replace("\"", "");
                                String entry2 = json.getString("entry2").replace("\"", "");
                                String entry1_dist = json.getString("entry1_dist").replace("\"", "");
                                String entry2_dist = json.getString("entry2_dist").replace("\"", "");
                                String entry1_score = json.getString("entry1_score").replace("\"", "");
                                String entry2_score = json.getString("entry2_score").replace("\"", "");
                                String updatedAt = json.getString("updatedAt").replace("\"", "");
                                String createdAt = json.getString("createdAt").replace("\"", "");
                                String id = json.getString("id").replace("\"", "");
                                boolean end_game = json.getBoolean("end_game");

                                arrRoom.add(new RoomList(entry1, entry2, entry1_dist, entry2_dist, entry1_score, entry2_score, updatedAt, createdAt, id, end_game));
                                Log.e("json_id", json.getString("id"));
                                adapter.notifyDataSetChanged();
                            }
                            Log.e("try", "22222");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", ServerManager.API_KEY);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);

        return arrRoom;
    }

    public void deleteRow(final int position) {
        CognitoUserPool userPool = new CognitoUserPool(getApplicationContext(), new AWSConfiguration(getApplicationContext()));
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = ServerManager.AWS_URL + "?id=ec9fd980-6c81-11e8-9283-1fd9d3707540";

        try {

            StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("x-api-key", ServerManager.API_KEY);

                    return headers;
                }
            };
            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRow(final int position) {
        CognitoUserPool userPool = new CognitoUserPool(getApplicationContext(), new AWSConfiguration(getApplicationContext()));
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = ServerManager.AWS_URL + "?id=ec9fd980-6c81-11e8-9283-1fd9d3707540";

        try {

            final String json = "{" +
                    "\"entry1_dist\":\"2000\"," +
                    "\"entry2_dist\":null," +
                    "\"end_game\":false" +
                    "}";

            Log.e("json", json);

            StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    byte[] body = new byte[0];
                    body = json.getBytes();
                    return body;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("x-api-key", ServerManager.API_KEY);
                    headers.put("Content-type", "application/json");

                    return headers;
                }
            };
            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class PlaceHolder {
        TextView txtID;
        TextView txtEntry1;
        TextView txtEntry2;
        TextView txtScore;
    }

    private class ListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<RoomList> array;

        int layout;

        public ListAdapter(Context context, ArrayList<RoomList> array, int layout) {
            this.context = context;
            this.array = array;
            this.layout = layout;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final PlaceHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);

                holder = new PlaceHolder();
                holder.txtID = convertView.findViewById(R.id.txtID);
                holder.txtEntry1 = convertView.findViewById(R.id.txtEntry1);
                holder.txtEntry2 = convertView.findViewById(R.id.txtEntry2);
                holder.txtScore = convertView.findViewById(R.id.txtScore);

                convertView.setTag(holder);
            } else {
                holder = (PlaceHolder) convertView.getTag();
            }


            holder.txtID.setText(array.get(position).id);

            holder.txtEntry1.setText(array.get(position).entry1);

            if(array.get(position).entry2.equals("")) {
                holder.txtEntry2.setText("");
            } else {
                holder.txtEntry2.setText(array.get(position).entry2);
            }
            holder.txtScore.setText(array.get(position).entry1_score + "점");

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRow(position);
                }
            });

            return convertView;
        }
    }

    class CreateAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String json = strings[0];
            Log.e("async", json);

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, json);
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(ServerManager.AWS_URL)
                    .post(body)
                    .addHeader("Accept", "*/*")
                    .addHeader("x-api-key", ServerManager.API_KEY)
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();
                Log.e("okhttp3", "success");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
