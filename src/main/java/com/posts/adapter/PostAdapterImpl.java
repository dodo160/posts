package com.posts.adapter;

import com.posts.dto.PostDTO;
import com.posts.exception.NotFoundException;
import com.posts.mapper.PostMapper;
import com.posts.model.Post;
import com.posts.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostAdapterImpl implements PostAdapter {

    private final PostService postService;
    private final PostMapper postMapper;

    public PostAdapterImpl(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }


    @Override
    public PostDTO getById(final Integer id) {
        return postMapper.toDto(postService.findByIdSaveFromExternalAPI(id));
    }

    @Override
    public List<PostDTO> getByUserId(final Integer id) {
        List<Post> posts = postService.findByUserId(id);
        if (!posts.isEmpty()) {
            return postMapper.toDtoList(posts);
        } else {
            throw new NotFoundException("Posts not found with userId: " + id);
        }
    }

    @Override
    public void add(final PostDTO dto) {
        postService.add(postMapper.fromDto(dto));
    }

    @Override
    public PostDTO update(final PostDTO dto) {
        return postMapper.toDto(postService.update(postMapper.fromDto(dto)));
    }

    @Override
    public void delete(final Integer id) {
        postService.delete(id);
    }
}
