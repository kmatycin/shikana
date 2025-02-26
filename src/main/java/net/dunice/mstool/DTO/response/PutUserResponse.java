package net.dunice.mstool.DTO.response;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class PutUserResponse {

    private String avatar;

    private String email;

    private UUID id;

    private String name;

    private String role;
}

