package com.posts.adapter;

import com.posts.dto.PostDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface PostAdapter {

    PostDTO getById(@NotNull Integer id);

    List<PostDTO> getByUserId(@NotNull Integer id);

    void add(@NotNull PostDTO dto);

    PostDTO update(@NotNull PostDTO dto);

    void delete(@NotNull Integer id);
}
