package com.sivalabs.devzone.domain.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Old password cannot be blank")
    String oldPassword;

    @NotBlank(message = "New password cannot be blank")
    String newPassword;

}
