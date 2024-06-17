package com.onlinemarket.server.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    //1 for success, 0 for fail
    private Integer code;
    private String msg;
    private Object data;
}
