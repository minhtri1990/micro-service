package com.its.auth_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExtractDto {
    public UserExtractDto(UserFullDto userFullDto) {
        this.id = userFullDto.getId();
        this.userName = userFullDto.getUserName();
        this.fullName = userFullDto.getFullName();
        this.email = userFullDto.getEmail();
        this.phone = userFullDto.getPhone();
        this.role = userFullDto.getRole().getName();
        this.dob = userFullDto.getDob();
        switch (userFullDto.getGender()) {
            case 1: this.gender = "Nam"; break;
            case 2: this.gender = "Nữ"; break;
            default: this.gender = "Chưa xác định";
        }
        this.isActive = userFullDto.getIsActive()? "Đang hoạt động": "Đã bị xóa";
    }

    private Integer id;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String avatar;
    private String isActive;
    private String gender;
    private String dob;
}
