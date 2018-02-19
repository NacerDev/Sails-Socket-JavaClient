package tn.isp.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by mednaceur on 19/02/2018.
 */

public class SailsSocket {

    private static String _version="__sails_io_sdk_version=0.13.8";
    private static String _language="__sails_io_sdk_language=java";
    private static String _platform="__sails_io_sdk_platform=android";

    private Socket _socket;
    private String _apiPrfix="";
    private JSONObject _headers=new JSONObject();



    public SailsSocket(String url, IO.Options options) throws URISyntaxException {
        options.query=_version+"&"+_platform+"&"+_language;
        _socket=IO.socket(url,options);
    }
    public SailsSocket(String url) throws URISyntaxException {
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.transports=new String[]{"polling", "websocket"};
        opts.reconnection = false;
        opts.query=_version+"&"+_platform+"&"+_language;
        _socket=IO.socket(url,opts);
    }
    public String id(){
        return _socket.id();
    }
    private void setHeaders(String key,String value) throws JSONException {
        _headers.put(key,value);
    }
    public void setApiPrefix(String prfix){
        this._apiPrfix=prfix;
    }
    public void onConnect(Emitter.Listener listener){
        _socket.on(Socket.EVENT_CONNECT,listener);
    }
    public void onDisconnect(Emitter.Listener listener){
        _socket.on(Socket.EVENT_DISCONNECT,listener);
    }

    public void on(final String event, final SailsSocketEvent listener){
        _socket.on(event, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(args.length>0){
                    JSONObject data= (JSONObject) args[0];
                    listener.onEvent(data);
                }
            }
        });
    }
    private void request(String method,JSONObject ctx, final SailsSocketResponse callback)  {
        _socket.emit(method, ctx, new Ack() {
            @Override
            public void call(Object... args) {
                    JSONObject jwres= (JSONObject) args[0];
                    callback.onResponse(jwres);
            }
        });
    }
    public void get(String url, JSONObject data, SailsSocketResponse callback) throws JSONException {
        JSONObject ctx=new JSONObject();
        ctx.put("method","get");
        ctx.put("headers",_headers);
        ctx.put("data",data);
        ctx.put("url",_apiPrfix+url);
        request("get",ctx,callback);
    }
    public void post(String url, JSONObject data, SailsSocketResponse callback) throws JSONException {
        JSONObject ctx=new JSONObject();
        ctx.put("method","post");
        ctx.put("headers",_headers);
        ctx.put("data",data);
        ctx.put("url",_apiPrfix+url);
        request("post",ctx,callback);
    }
    public void put(String url, JSONObject data, SailsSocketResponse callback) throws JSONException {
        JSONObject ctx=new JSONObject();
        ctx.put("method","put");
        ctx.put("headers",_headers);
        ctx.put("data",data);
        ctx.put("url",_apiPrfix+url);
        request("put",ctx,callback);
    }

    public void delete(String url, JSONObject data, SailsSocketResponse callback) throws JSONException {
        JSONObject ctx=new JSONObject();
        ctx.put("method","delete");
        ctx.put("headers",_headers);
        ctx.put("data",data);
        ctx.put("url",_apiPrfix+url);
        request("delete",ctx,callback);
    }

    public boolean connected(){
        return _socket.connected();
    }
    public void connect(){
        _socket.connect();
    }
    public  void disconnect(){
        _socket.disconnect();
    }
    public void open(){
        _socket.open();
    }
    public  void close(){
        _socket.close();
    }

    public  interface SailsSocketResponse {
        void onResponse(JSONObject jwRes) ;
    }
    public  interface SailsSocketEvent{
        void onEvent(JSONObject data) ;
    }
}
