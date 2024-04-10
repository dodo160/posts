package com.posts.restcontroller;

import com.posts.adapter.PostAdapter;
import com.posts.dto.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostRestController {

    private final PostAdapter postAdapter;

    public PostRestController(PostAdapter postAdapter) {
        this.postAdapter = postAdapter;
    }

    @Operation(summary = "Get a post by id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDTO> getById(@PathVariable final Integer id) {
        return ResponseEntity.ok(postAdapter.getById(id));
    }

    @Operation(summary = "Get list of posts by userid")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Posts not found with userId")})
    @GetMapping(value = "/userid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostDTO>> getByUserId(@PathVariable final Integer id) {
        return ResponseEntity.ok(postAdapter.getByUserId(id));
    }

    @Operation(summary = "Add post")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Post created"),
            @ApiResponse(responseCode = "409", description = "Post entity exists")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> add(@Valid @RequestBody final PostDTO dto) {
        postAdapter.add(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Update post")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Post updated"),
            @ApiResponse(responseCode = "404", description = "Post entity doesn't exist")})
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDTO> update(@Valid @RequestBody final PostDTO dto) {
        return ResponseEntity.ok(postAdapter.update(dto));
    }

    @Operation(summary = "Delete post")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Post deleted")})
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Integer id) {
        postAdapter.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

