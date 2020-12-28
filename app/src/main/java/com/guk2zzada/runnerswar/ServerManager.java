package com.guk2zzada.runnerswar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerManager {

    private final static int FLAG_NONE = 0;
    private final static int FLAG_MATCH = 1;

    public final static String AWS_URL = "AWS_URL";
    public final static String API_KEY = "AWS_KEY";

    private Context context;
    private RequestQueue queue;
    private RoomList selRoom;
    private MyAsyncTask myAsync;
    private Move move;

    private ArrayList<RoomList> arrRoom = new ArrayList<>();

    private int flag = 0;

    ServerManager(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        move = new Move(context);
    }

    public RoomList getList() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ServerManager.AWS_URL + "?id=" + selRoom.id;

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

                            selRoom = new RoomList(entry1, entry2, entry1_dist, entry2_dist, entry1_score, entry2_score, updatedAt, createdAt, id, end_game);
                            Log.e("json_id", json.getString("id"));
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

        return selRoom;
    }

    public ArrayList<RoomList> getAllList(final boolean isCheck) {
        //ArrayList<RoomList> arrRoom = new ArrayList<>();
        String url = ServerManager.AWS_URL;


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            arrRoom.clear();
                            Log.e("try", response.toString());
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject json = response.getJSONObject(i);
                                Log.e("json", json.toString());
                                String entry1 = json.getString("entry1").replace("\"", "");
                                String entry2 = json.getString("entry2").replace("\"", "");
                                String entry1_dist = json.getString("entry1_dist").replace("\"", "");
                                String entry2_dist = json.getString("entry2_dist").replace("\"", "");
                                String entry1_score = json.getString("entry1_score").replace("\"", "");
                                String entry2_score = json.getString("entry2_score").replace("\"", "");
                                String updatedAt = json.getString("updatedAt").replace("\"", "");
                                String createdAt = json.getString("createdAt");
                                String id = json.getString("id").replace("\"", "");
                                boolean end_game = json.getBoolean("end_game");

                                arrRoom.add(new RoomList(entry1, entry2, entry1_dist, entry2_dist, entry1_score, entry2_score, updatedAt, createdAt, id, end_game));
                                Log.e("json", json.getString("id"));

                            }

                            if(isCheck)
                                checkRoom();

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

    /**
     * 방이 있는지 체크
     * 있으면 가장 적절한 방 선택
     */
    public void checkRoom() {

        for (int i = 0; i < arrRoom.size(); i++) {
            Log.e("arrRoom", "arrRoom[" + i + "]");
            Log.e("RoomList", "entry1 = " + arrRoom.get(i).entry1 + " | entry2 = " + arrRoom.get(i).entry2);
            if (!arrRoom.get(i).entry1.equals(GlobalVar.id) && arrRoom.get(i).entry2.equals("null")) {
                Log.e("empty", "arrRoom[" + i + "]");
                selRoom = arrRoom.get(i);
                changeRoomState();
                Log.e("Room", "Room Exist");
                return;
            }
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ServerManager.AWS_URL;

        final String json = "{\"entry1\":\"" + GlobalVar.id + "\"," +
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
        searching();

        Log.e("Room", "Room Create");
    }

    public void changeRoomState() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ServerManager.AWS_URL + "?id=" + selRoom.id;
        Log.e("ID", selRoom.id);

        try {

            final String json = "{" +
                    "\"entry1_dist\":null," +
                    "\"entry2\":\"" + GlobalVar.id + "\"," +
                    "\"entry2_dist\":\"0\"," +
                    "\"entry2_score\":\"0\"," +
                    "\"end_game\":false" +
                    "}";

            Log.e("json", json);

            move.startActivity(MatchedActivity.class);

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

    private void searching() {
        myAsync = new MyAsyncTask();
        myAsync.execute("");
    }

    public void deleteRow(final String id) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ServerManager.AWS_URL + "?id=" + id;

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

    class MyAsyncTask extends AsyncTask<String, String, String> {

        boolean boolSearch = false;
        boolean boolAllList = false;

        @Override
        protected String doInBackground(String... strings) {

            while(!boolSearch) {
                Log.e("AsyncTask", "RoomAsync");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!boolAllList) {
                    arrRoom = getAllList(false);
                    for (int i = 0; i < arrRoom.size(); i++) {
                        Log.e("Room", arrRoom.get(i).entry1);
                        if (arrRoom.get(i).entry1.equals(GlobalVar.id)) {
                            selRoom = arrRoom.get(i);
                            boolAllList = true;
                        }
                    }
                } else {
                    GlobalVar.room = getList();
                    if(selRoom.entry1.equals(GlobalVar.id) && !selRoom.entry2.equals("null")) {
                        flag = FLAG_MATCH;
                        boolSearch = true;
                    }
                }
                Log.e("Room", "리스트 불러옴");


            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            switch(flag) {
                case FLAG_NONE:
                    move.startActivity(MatchedActivity.class);
                    ((Activity)context).finish();
                    break;

                case FLAG_MATCH:
                    move.startActivity(MatchedActivity.class);
                    ((Activity)context).finish();
                    break;
            }

        }
    }
}
