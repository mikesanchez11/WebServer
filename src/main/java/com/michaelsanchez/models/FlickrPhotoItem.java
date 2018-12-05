package com.michaelsanchez.models;

public class FlickrPhotoItem {

    private String id;
    private String owner;
    private String secret;
    private String server;
    private int farm;
    private String title;
    private boolean ispublic;
    private boolean isfriend;
    private boolean isfamily;

    public String getUrl() {
        return String.format("http://farm%s.static.flickr.com/%s/%s_%s.jpg", farm, server, id, secret);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIspublic() {
        return ispublic;
    }

    public void setIspublic(boolean ispublic) {
        this.ispublic = ispublic;
    }

    public boolean isIsfriend() {
        return isfriend;
    }

    public void setIsfriend(boolean isfriend) {
        this.isfriend = isfriend;
    }

    public boolean isIsfamily() {
        return isfamily;
    }

    public void setIsfamily(boolean isfamily) {
        this.isfamily = isfamily;
    }
}
