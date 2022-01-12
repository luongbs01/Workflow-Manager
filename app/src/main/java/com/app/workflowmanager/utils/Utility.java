package com.app.workflowmanager.utils;

import com.app.workflowmanager.entity.GithubRepo;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<GithubRepo> searchRepoByName(List<GithubRepo> reposInput, String query) {
        List<GithubRepo> result = new ArrayList<>();
        for (GithubRepo githubRepo : reposInput) {
            if (githubRepo.getName().toLowerCase().contains(query.toLowerCase()))
                result.add(githubRepo);
        }
        return result;
    }
}
