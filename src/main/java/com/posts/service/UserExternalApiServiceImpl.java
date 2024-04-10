package com.posts.service;

import com.posts.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserExternalApiServiceImpl extends ExternalApiAbstractService<UserDTO> {

    private static final String ENTITY = "users";

    @Override
    public String getEntityPath() {
        return ENTITY;
    }

    @Override
    String getValidationMsg(final Integer id, final String url) {
        return String.format("User not found with id: %d. URL: %s", id, url);
    }
}
