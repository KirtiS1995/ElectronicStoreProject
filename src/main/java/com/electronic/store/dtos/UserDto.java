package com.electronic.store.dtos;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @NotEmpty
    @Size(min = 4,max = 20,message = "Username must be minimum 4 and maximum 20 characters")
    private String name;

    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty
	@Size(min = 3, max = 10,message = "Password must be minimum 3 and max 10 characters.")
	@Pattern(regexp = "^[a-z]{5}[0-9]{3}",message = "Password incorrect....")
    private String password;

    @NotEmpty
    @Size(min = 10,max = 25,message = "Size should be minimum 10 and maximum 25 chracters")
    private String about;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String imageName;

}
