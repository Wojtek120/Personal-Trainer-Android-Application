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
public class ExerciseModel {

    private String exerciseId;
    private String name;
    private int order;
    private String intensity;
    private int reps;
    private int sets;
    private int setsDone;

}
