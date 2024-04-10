package com.posts.service;

import com.posts.dto.PostDTO;
import org.springframework.stereotype.Service;

@Service
public class PostExternalApiServiceImpl extends ExternalApiAbstractService<PostDTO> {

    private static final String ENTITY = "posts";

    @Override
    public String getEntityPath() {
        return ENTITY;
    }

    @Override
    public String getValidationMsg(final Integer id, final String url){
        return String.format("Post not found with id : %d. URL: %s", id, url);
    }

}
