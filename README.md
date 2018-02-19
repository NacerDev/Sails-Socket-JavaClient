# Sails-Socket-JavaClient
This is a java class  using Socket.IO v1.x Client Library for Java, which is simply ported from the  JavaScript sails.io client SDK.

## https://github.com/socketio/socket.io-client-java


# Usage
```java
SailsSocket socket= new SailsSocket("http://localhost:1337");
socket.setApiPrefix("/api");
socket.onConnect(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("socket","connected to the server");
            }
        });
socket.on("client", new SailsSocket.SailsSocketEvent() {
            @Override
            public void onEvent( JSONObject data) {
                Log.d("response",data.toString());
            }
});
socket.onDisconnect(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("socket","disconnected from the server");
            }
});
socket.connect();
.....
 socket.get("/client", null, new SailsSocket.SailsSocketResponse() {
            @Override
            public void onResponse(JSONObject jwres)  {
                Log.d("response",jwres.toString());
            }
});
```
