package com.idcodevalidator.backendapp.entity;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "validations")
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "time")
    private LocalDateTime timeStamp; //Fixme: think about time format
    @Column(name = "id_code")
    private String idCode;
    private boolean failed;
    private String verdict;

    public Validation() {
    }

    public Validation(LocalDateTime timeStamp, String idCode,
                      boolean failed, String verdict) {
        this.timeStamp = timeStamp;
        this.idCode = idCode;
        this.failed = failed;
        this.verdict = verdict;
    }
}
