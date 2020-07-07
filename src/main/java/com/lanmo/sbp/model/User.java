package com.lanmo.sbp.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * user
 * @author 
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Integer id;

    private String name;

    private String address;

    private String mobile;

    private String email;

    private Date createTime;

    private Integer role;

    private static final long serialVersionUID = 1L;

}