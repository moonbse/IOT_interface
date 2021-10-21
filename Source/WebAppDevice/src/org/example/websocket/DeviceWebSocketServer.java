package org.example.websocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.example.model.Device;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
//import javax.inject.Inject;


@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
    private static  DeviceSessionHandler sessionHandler = new DeviceSessionHandler();
    @OnOpen
    public void open(Session session) {
//        session.getBasicRemote().
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);

    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("add".equals(jsonMessage.getString("action"))) {
                Device device = new Device();

                device.setId(0);
                device.setName(jsonMessage.getString("name"));
                device.setDescription(jsonMessage.getString("description"));
                device.setType(jsonMessage.getString("type"));
                device.setStatus("Off");
                sessionHandler.addDevice(device);
//                sessionHandler.addDevice(device);
//                Thread t = new Thread(device);
//                t.start();
            }

            if ("remove".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                Device device = sessionHandler.getDeviceById(id);
                sessionHandler.removeDevice(id);
//                device.stop();
            }

            if ("toggle".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.toggleDevice(id);
            }
        }
    }
}
