package org.example.localstack.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class Address {

    private String postalCode;
    private String street;
    private String number;
    private String city;
    private String additionalInfo;

    @DynamoDbAttribute("postalCode")
    public String getPostalCode() {
        return postalCode;
    }

    @DynamoDbAttribute("street")
    public String getStreet() {
        return street;
    }

    @DynamoDbAttribute("number")
    public String getNumber() {
        return number;
    }


    @DynamoDbAttribute("city")
    public String getCity() {
        return city;
    }

    @DynamoDbAttribute("additionalInfo")
    public String getAdditionalInfo() {
        return additionalInfo;
    }

}
