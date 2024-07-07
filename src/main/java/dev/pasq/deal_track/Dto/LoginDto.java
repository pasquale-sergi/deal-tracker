package dev.pasq.deal_track.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotEmpty(message = "Username mandatory")
    @NotBlank(message = "Username mandatory")
    private String username;
    @NotEmpty(message = "Password mandatory")
    @NotBlank(message = "Password mandatory")
    @Size(min =8, message ="Password must be at least 8 characters")
    private String password;
}
