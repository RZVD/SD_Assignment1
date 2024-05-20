package com.utcn.StackOverflow.DTOs.users;

public class BanUserDTO {
    private Long banningUserId;
    private Long bannedUserId;

    public Long getBanningUserId() {
        return banningUserId;
    }

    public void setBanningUserId(Long banningUserId) {
        this.banningUserId = banningUserId;
    }

    public Long getBannedUserId() {
        return bannedUserId;
    }

    public void setBannedUserId(Long bannedUserId) {
        this.bannedUserId = bannedUserId;
    }

    public BanUserDTO() {
    }

    public BanUserDTO(Long banningUserId, Long bannedUserId) {
        this.banningUserId = banningUserId;
        this.bannedUserId = bannedUserId;
    }
}
