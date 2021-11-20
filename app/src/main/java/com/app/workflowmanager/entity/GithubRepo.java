package com.app.workflowmanager.entity;

public class GithubRepo {

    private int id;
    private String name;
    private Owner owner;
    private String description;
    private String visibility;
    private String created_at;
    private String updated_at;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getVisibility() {
        return visibility;
    }


    public class Owner {
        private String avatar_url;
        private String login;

        public String getAvatar_url() {
            return avatar_url;
        }

        public String getLogin() {
            return login;
        }
    }
}
