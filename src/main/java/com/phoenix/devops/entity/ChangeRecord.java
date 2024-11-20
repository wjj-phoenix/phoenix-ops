package com.phoenix.devops.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wjj-phoenix
 * @since 2024-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRecord {
    private String createName;
    private LocalDateTime changeTime;

    private String changeField;
    private String beforeChange;
    private String afterChange;
    private Long typeId;
    private String remark;

}
