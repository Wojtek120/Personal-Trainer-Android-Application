package com.wojtek120.personaltrainer.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
@Builder
public class PlanModel implements Serializable {

    private String userId;
    private String name;
    private Date created;

    public PlanModel(String userId, String name) {
        this.userId = userId;
        this.name = name;

        this.created = new Date();
    }



}
