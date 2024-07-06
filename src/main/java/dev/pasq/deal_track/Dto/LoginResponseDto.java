package dev.pasq.deal_track.Dto;

import dev.pasq.deal_track.entity.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private ApplicationUser user;
    private String jwt;
}
