package com.ponleu.jasperreport.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DemoDTO {
    private int id;
    private String name;
    private long price;
}
