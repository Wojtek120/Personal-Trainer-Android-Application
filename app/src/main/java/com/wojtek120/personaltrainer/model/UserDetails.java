package com.wojtek120.personaltrainer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
@Builder
public class UserDetails {

    private String userId;
    private String username;
    private String description;
    private String profilePhoto;
    private Double squatMax;
    private Double benchMax;
    private Double deadliftMax;

}
