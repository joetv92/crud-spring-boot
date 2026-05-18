package com.example.crud_posts.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserSummaryDTO user;

    // ✅ كلاس داخلي static
    @Data
    public static class UserSummaryDTO {
        private Long id;
        private String username;
        private String email;
    }
}