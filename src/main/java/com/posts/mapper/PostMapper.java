package com.posts.mapper;

import com.posts.dto.PostDTO;
import com.posts.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    PostDTO toDto(Post entity);

    Post fromDto(PostDTO dto);

    List<PostDTO> toDtoList(List<Post> list);

    List<Post> fromDtoList(List<PostDTO> list);
}
