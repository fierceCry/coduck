package coduck.igochaja.Dto;

import lombok.Data;

@Data
public class NaverUserDto {
    private String id;
    private String name;
    private String email;

    public NaverUserDto(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
