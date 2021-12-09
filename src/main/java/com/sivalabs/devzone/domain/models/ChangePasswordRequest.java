package com.sivalabs.devzone.domain.models;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequest {

    @NotBlank(message = "Old password cannot be blank")
    String oldPassword;

    @NotBlank(message = "New password cannot be blank")
    String newPassword;
}
