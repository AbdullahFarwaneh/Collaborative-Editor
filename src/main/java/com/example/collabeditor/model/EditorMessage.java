package com.example.collabeditor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditorMessage {
    private String sender;
    private String content;
    private String type;
    private int line;
    private int column;
}