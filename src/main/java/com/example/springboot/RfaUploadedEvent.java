package com.example.springboot;

import java.io.Serializable;

public class RfaUploadedEvent implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
