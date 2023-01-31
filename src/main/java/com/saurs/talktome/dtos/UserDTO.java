package com.saurs.talktome.dtos;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import com.saurs.talktome.models.User;
import com.saurs.talktome.models.enums.Gender;

import lombok.Data;

@Data
public class UserDTO {

  private Long id;
  private String email;
  private String phone;
  private String password;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Long partnerId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static UserDTO converter(User u) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(u, UserDTO.class);
  }
  
}
