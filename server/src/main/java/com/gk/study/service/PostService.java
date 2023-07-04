package com.gk.study.service;


import com.gk.study.entity.Post;

import java.util.List;

public interface PostService {
    List<Post> getPostList();
    void createPost(Post post);
    void deletePost(String id);

    void updatePost(Post post);

    List<Post> getUserPost(String userId);

    List<Post> getCompanyPost(String companyId);
}
