package com.email.writer.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
@NoArgsConstructor
public class EmailRequest {


    private String emailContent;
    private String tone;//professional, friendly etc etc...

}
