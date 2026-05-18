package com.example.crud_posts.dto;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Long userId;  // معرف المستخدم فقط
}