package org.example.model;


import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Random;
import java.net.URI;

import static java.lang.Thread.sleep;


public class Device implements Runnable{
    private int id;
    private String name;
    private String status;
    private String type;
    private String description;
//    string to store json data
//    private String data;
//    to stop the thread
    private boolean exit;

    public Device() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return id == device.id &&
                Objects.equals(name, device.name) &&
                Objects.equals(status, device.status) &&
                Objects.equals(type, device.type) &&
                Objects.equals(description, device.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, type, description);
    }

    private JsonObject createAddMessage(int data1, int data2, int data3) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("id", id)
                .add("data1", data1)
                .add("data2", data2)
                .add("data3", data3)
                .build();
        return addMessage;
    }
    @Override
    public void run() {
        Random rand = new Random();
        int max = 100;
        int min = 10;
        int flag = 0;
        String post_url = "http://localhost:8080/api/devices";
        String put_url = "http://localhost:8080/api/devices/" + id;
        while(!exit) {
//            broken pipe needs to be fixed, django api
            int data1 = rand.nextInt(100);
            int data2 = rand.nextInt(200);
            int data3 = rand.nextInt(10);
            JsonObject data = createAddMessage(data1, data2, data3);
            if(flag == 0){
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(post_url))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
                        .build();

                var client = HttpClient.newHttpClient();
//
                try {
                    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag = 1;
            }
            else{
                var request = HttpRequest.newBuilder()
                        .uri(URI.create(put_url))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(data.toString()))
                        .build();

                var client = HttpClient.newHttpClient();

                try {
                    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//
            }
            System.out.println("Device " + getId() + " data " + rand.nextInt());
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        exit = true;
    }
}
