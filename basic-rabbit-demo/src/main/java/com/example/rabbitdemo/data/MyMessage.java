package com.example.rabbitdemo.data;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class MyMessage implements Serializable {

    private String id = UUID.randomUUID().toString();
    private String content;
}
