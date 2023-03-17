package com.project.sync.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImgurAccountInfo {
    private Integer id;
    private String url;
    private String bio;
    private String avatar;
    private Integer reputation;
    private String reputation_name;
    private Integer created;
    private Boolean pro_expiration;
    private UserFollow user_follow;
}
