package com.idcodevalidator.backendapp.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Validation object, which holds information about:
 * 1. Time of validation creation.
 * 2. Validated ID code.
 * 3. If validation failed.
 * 4. Written out verdict in text format.
 */
@Getter
@Setter
@Entity
@Table(name = "validations")
public class ValidationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "time")
    // @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime timeStamp;
    @Column(name = "id_code")
    private String idCode;
    private boolean failed;
    private String verdict;

    public ValidationResult() {
    }

    public ValidationResult(LocalDateTime timeStamp, String idCode,
                            boolean failed, String verdict) {
        this.timeStamp = timeStamp;
        this.idCode = idCode;
        this.failed = failed;
        this.verdict = verdict;
    }
}
