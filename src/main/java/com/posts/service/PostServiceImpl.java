package com.posts.service;

import com.posts.dto.PostDTO;
import com.posts.dto.UserDTO;
import com.posts.exception.NotFoundException;
import com.posts.mapper.PostMapper;
import com.posts.model.Post;
import com.posts.repository.PostRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ExternalApiService<PostDTO> postApiService;

    private final ExternalApiService<UserDTO> userApiService;

    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, @Qualifier(value = "postExternalApiServiceImpl") ExternalApiService<PostDTO> postApiService,
                           @Qualifier(value = "userExternalApiServiceImpl") ExternalApiService<UserDTO> userApiService, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postApiService = postApiService;
        this.userApiService = userApiService;
        this.postMapper = postMapper;
    }

    @Override
    public Post findById(final Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post add(final Post post) {
        if (postRepository.existsById(post.getId())) {
            throw new ValidationException("Post entity exists with id: " + post.getId());
        }

        if (Objects.nonNull(userApiService.getById(post.getUserId()))) {
            return postRepository.save(post);
        }else {
            throw new ValidationException("Not valid userid");
        }
    }

    @Override
    @Transactional
    public Post update(final Post post) {
        if (!postRepository.existsById(post.getId())) {
            throw new NotFoundException("Post entity doesn't exist with id: " + post.getId());
        }
        postRepository.updatePostTitleAndBody(post.getId(), post.getTitle(), post.getBody());
        return postRepository.findById(post.getId()).orElse(null);
    }

    @Override
    public void delete(final Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> findByUserId(final Integer id) {
        final List<Post> result = postRepository.findByUserId(id);
        final List<Post> postExtApi = postMapper.fromDtoList(postApiService.getAll().stream().filter(x->id.equals(x.getUserId())).toList());
        if(Objects.nonNull(postExtApi) && !postExtApi.isEmpty()){
            result.addAll(postExtApi);
        }
        return result.stream().distinct().toList();
    }

    @Override
    public Post findByIdSaveFromExternalAPI(final Integer id) {
        final Post postDB = findById(id);
        if (Objects.nonNull(postDB)) {
            return postDB;
        } else {
            final Post postExtApi = postMapper.fromDto(postApiService.getById(id));
            if (Objects.nonNull(postExtApi)) {
                return postRepository.save(postExtApi);
            }
            return null;
        }
    }
}
