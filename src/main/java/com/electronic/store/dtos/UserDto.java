package com.electronic.store.dtos;

import com.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto extends BaseEntityDto {

    private String userId;

    @NotEmpty
    @Size(min = 4,max = 20,message = "Username must be minimum 4 and maximum 20 characters")
    private String name;

    @Email(message = "Invalid Email..!!")
//    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid Email.")
    @NotBlank(message = "Email is Required.")
    private String email;

    @NotBlank(message = "Password Required")
	@Pattern(regexp = "^[a-z]{5}[0-9]{3}",message = "Password incorrect....")
    private String password;

   @NotBlank
    @Size(min = 10,max = 25,message = "Size should be minimum 10 and maximum 25 chracters")
    private String about;

    @Size(min = 4,max = 6,message = "Invalid gender.")
    private String gender;

    //Custom validation
    @ImageNameValid
    private String imageName;

}
