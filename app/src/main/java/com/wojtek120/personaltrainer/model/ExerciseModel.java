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
public class ExerciseModel implements Serializable {

    private String name;
    private int order;
    private double intensity;
    private double weight;
    private int reps;
    private int sets;
    private int setsDone;
    private String comment;

}
