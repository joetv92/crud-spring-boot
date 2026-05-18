package com.example.crud_posts.service;

import com.example.crud_posts.dto.PostDTO;
import com.example.crud_posts.dto.PostResponseDTO;
import com.example.crud_posts.entity.Post;
import com.example.crud_posts.entity.User;
import com.example.crud_posts.repository.PostRepository;
import com.example.crud_posts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المنشور غير موجود: " + id));
        return convertToDTO(post);
    }

    public List<PostResponseDTO> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDTO createPost(PostDTO postDTO) {
        User user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود: " + postDTO.getUserId()));

        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    @Transactional
    public PostResponseDTO updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المنشور غير موجود: " + id));

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());

        // إذا تغير المستخدم
        if (postDTO.getUserId() != null && !postDTO.getUserId().equals(post.getUser().getId())) {
            User newUser = userRepository.findById(postDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("المستخدم غير موجود: " + postDTO.getUserId()));
            post.setUser(newUser);
        }

        Post updatedPost = postRepository.save(post);
        return convertToDTO(updatedPost);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("المنشور غير موجود: " + id));
        postRepository.delete(post);
    }

    private PostResponseDTO convertToDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());

        PostResponseDTO.UserSummaryDTO userDto = new PostResponseDTO.UserSummaryDTO();
        userDto.setId(post.getUser().getId());
        userDto.setUsername(post.getUser().getUsername());
        userDto.setEmail(post.getUser().getEmail());
        dto.setUser(userDto);

        return dto;
    }
}