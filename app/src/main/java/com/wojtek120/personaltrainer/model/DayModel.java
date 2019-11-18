package com.wojtek120.personaltrainer.model;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class DayModel implements Serializable {

    private Date date;
    private String description;

    public String giveDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        return dateFormat.format(date);
    }

}
