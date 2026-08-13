package model;

import lombok.Data;

@Data
public class Currency {

    private Integer id;

    private String code;

    private String fullName;

    private String sign;
}
