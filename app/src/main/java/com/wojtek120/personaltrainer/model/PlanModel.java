package com.wojtek120.personaltrainer.model;

import java.io.Serializable;

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
public class PlanModel implements Serializable {

    private String userId;
    private String name;

}
