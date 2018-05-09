package com.example.wregea63.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WarGameService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_JOIN_GAME = "com.example.wregea63.myapplication.action.FOO";
    private static final String ACTION_GET_CARDS = "com.example.wregea63.myapplication.action.BAZ";
    private static final String ACTION_FULL_LOBBY = "FullLobby";
    private static final String ACTION_LOGIN = "LOGIN";
    private static final String ACTION_REGISTER = "REGISTER";

    // TODO: Rename parameters
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String TABLE_ID = "TABLE_ID";

    public WarGameService() {
        super("WarGameService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionJoinGame(Context context, String username) {
        Intent intent = new Intent(context, WarGameService.class);
        intent.setAction(ACTION_JOIN_GAME);
        intent.putExtra(USERNAME, username);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetCards(Context context, String username, int tableId) {
        Intent intent = new Intent(context, WarGameService.class);
        intent.setAction(ACTION_GET_CARDS);
        intent.putExtra(USERNAME, username);
        intent.putExtra(TABLE_ID, tableId);
        context.startService(intent);
    }

    public static void startActionGameStarted(Context context, int tableId) {
        Intent intent = new Intent(context, WarGameService.class);
        intent.setAction(ACTION_FULL_LOBBY);
        intent.putExtra(TABLE_ID, tableId);
        context.startService(intent);
    }

    //ToDo check username/password against database rather than local, low priority
    public static void startActionUserLogin(Context context, String username, String password) {
        Intent intent = new Intent(context, WarGameService.class);
        intent.setAction(ACTION_REGISTER);
        intent.putExtra(USERNAME, username);
        intent.putExtra(PASSWORD, password);
        context.startService(intent);
    }

    //ToDo check username/password against database rather than local, low priority
    public static void startActionUserRegister(Context context, String username, String password) {
        Intent intent = new Intent(context, WarGameService.class);
        intent.setAction(ACTION_REGISTER);
        intent.putExtra(USERNAME, username);
        intent.putExtra(PASSWORD, password);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_JOIN_GAME.equals(action)) {
                final String param1 = intent.getStringExtra(USERNAME);
                handleActionJoinGame(param1);
            } else if (ACTION_GET_CARDS.equals(action)) {
                final String param1 = intent.getStringExtra(USERNAME);
                final int param2 = intent.getIntExtra(TABLE_ID, -1);
                handleActionGetCards(param1, param2);
            } else if (ACTION_FULL_LOBBY.equals(action)) {
                final int param1 = intent.getIntExtra(TABLE_ID, -1);
                handleActionCheckFullLobby(param1);
            }
            else if (ACTION_REGISTER.equals(action)) {
                final String param1 = intent.getStringExtra(USERNAME);
                final String param2 = intent.getStringExtra(PASSWORD);
                handleActionRegister(param1,param2);
            } else if (ACTION_LOGIN.equals(action)) {
                final String param1 = intent.getStringExtra(USERNAME);
                final String param2 = intent.getStringExtra(PASSWORD);
                handleActionLogin(param1,param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionJoinGame(String param1) {
        RequestQueue getGame = Volley.newRequestQueue(this);
        String urlUser;
        try {
            urlUser = URLEncoder.encode(param1, "utf-8");
            String url = "webdev.cs.uwosh.edu/students/wregea63/JoinGame.php?username=" + urlUser;
            StringRequest getLobbyId = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        public void onResponse(String response) {
                Intent lobby = new Intent();
                try {
                    int lobbyNum = Integer.parseInt(response);
                    lobby.putExtra("LOBBY", lobbyNum);
                    sendBroadcast(lobby);
                }
                catch (NumberFormatException e) {
                    Log.e("lobby", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error joining a lobby", Toast.LENGTH_LONG);
            }
        });
            getGame.add(getLobbyId);
        }
        catch (UnsupportedEncodingException e) {
            Log.e("Service", e.getMessage());
        }

    }

    private void handleActionLogin(String username, String password) {
        RequestQueue login = Volley.newRequestQueue(this);
        String urlUser;
        try {
            urlUser = URLEncoder.encode(username, "utf-8");
            // never pass passwords in a get request, only do with post,
            // but was unable to figure out adding parameters to volley post on short notice and will do later
            // ToDo don't pass password and username over get request
            String url = "webdev.cs.uwosh.edu/students/wregea63/Login.php?username=" + urlUser + "&password=" + password;
            StringRequest userLogin = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        public void onResponse(String response) {
                            Intent userExists = new Intent();
                            userExists.putExtra("SUCCESS", Boolean.parseBoolean(response));
                            sendBroadcast(userExists);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error joining a lobby", Toast.LENGTH_LONG);
                }
            });
            login.add(userLogin);
        }
        catch (UnsupportedEncodingException e) {
            Log.e("Service", e.getMessage());
        }

    }

    private void handleActionRegister(String username, String password) {
        RequestQueue register = Volley.newRequestQueue(this);
        String urlUser;
        try {
            urlUser = URLEncoder.encode(username, "utf-8");
            // never pass passwords in a get request, only do with post,
            // but was unable to figure out adding parameters to volley post on short notice and will do later
            // ToDo don't pass password and username over get request
            String url = "webdev.cs.uwosh.edu/students/wregea63/Login.php?username=" + urlUser + "&password=" + password;
            StringRequest userRegister = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        public void onResponse(String response) {
                            Intent userExists = new Intent();
                            userExists.putExtra("SUCCESS", Boolean.parseBoolean(response));
                            sendBroadcast(userExists);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error joining a lobby", Toast.LENGTH_LONG);
                }
            });
            register.add(userRegister);
        }
        catch (UnsupportedEncodingException e) {
            Log.e("Service", e.getMessage());
        }

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetCards(String param1, int param2) {
        RequestQueue getPlayerCards = Volley.newRequestQueue(this);
        String url = "webdev.cs.uwosh.edu/students/wregea63/CheckFullLobby.php?tableId=" + param1;
        JsonArrayRequest getCardArray = new JsonArrayRequest(Request.Method.GET, url, new JSONArray()
                ,
                new Response.Listener<JSONArray>() {

                    public void onResponse(JSONArray response) {
                        Intent cardIntent = new Intent();
                        ArrayList<String> cards = new ArrayList<String>();
                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    cards.add(response.getString(i));
                                }
                                catch (JSONException e) {
                                    Log.e("getCards", e.getMessage());
                                }
                            }
                        }
                        cardIntent.putExtra("CARDS", cards);
                        sendBroadcast(cardIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error checking if lobby is full", Toast.LENGTH_LONG);
            }
        });
        getPlayerCards.add(getCardArray);
    }

    private void handleActionCheckFullLobby(int param1) {
        RequestQueue checkFullLobby = Volley.newRequestQueue(this);
            String url = "webdev.cs.uwosh.edu/students/wregea63/CheckFullLobby.php?tableId=" + param1;
            StringRequest getLobbyFull = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        public void onResponse(String response) {
                            Intent lobby = new Intent();
                                boolean lobbyFull = Boolean.parseBoolean(response);
                                lobby.putExtra("LOBBYFULL", lobbyFull);
                                sendBroadcast(lobby);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error checking if lobby is full", Toast.LENGTH_LONG);
                }
            });
            checkFullLobby.add(getLobbyFull);
    }
}
