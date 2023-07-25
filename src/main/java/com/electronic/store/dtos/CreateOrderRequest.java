package com.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateOrderRequest extends BaseEntityDto {


    @NotBlank(message= "User Id is required!!")
    private String userId;

    @NotBlank(message= "Cart id is required!!")
    private String cartId;

    private String orderStatus="PENDING";

    private String paymentStatus="NOTPAID";

    @NotBlank(message= "Address is required!!")
    private String billingAddress;

    @NotEmpty
    private String billingPhone;

    @NotBlank
    private String billingName;

}
