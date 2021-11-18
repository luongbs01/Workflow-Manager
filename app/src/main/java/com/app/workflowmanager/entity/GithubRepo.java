package com.app.workflowmanager.entity;

public class GithubRepo {

    private int id;
    private String name;
    private String visibility;
    private Owner owner;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVisibility() {
        return visibility;
    }

    public Owner getOwner() {
        return owner;
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
