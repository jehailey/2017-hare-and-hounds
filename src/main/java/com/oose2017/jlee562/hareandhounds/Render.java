package com.oose2017.jlee562.hareandhounds;

import com.google.gson.Gson;

// class to save response code and message and convert into Json format
public class Render {

    public int Code;        // store response code
    public Object Message;  // store response message

    private Gson gson = new Gson();

    public Render(){}

    /**
     * Convert (Object) message into a Json format
     *
     * @return a Json format of Object
     */
    public String Json(){
        if (Message instanceof String){     // if Message is String, then reason: "Message"
            Reason failReason = new Reason();
            failReason.reason = (String) Message;
            return gson.toJson(failReason);
        }
        return gson.toJson(Message);
    }
}
