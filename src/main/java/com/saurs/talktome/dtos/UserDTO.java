package com.saurs.talktome.dtos;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.saurs.talktome.models.User;
import com.saurs.talktome.models.enums.Gender;

import lombok.Data;

@Data
public class UserDTO {

  private Long id;
  private String email;
  private String phone;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Long partnerId;
  private List<MessageDTO> messages;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
  private LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
  private LocalDateTime updatedAt;

  public static UserDTO converter(User u) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(u, UserDTO.class);
  }

  public static User converterToEntity(UserDTO dto) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(dto, User.class);
  }
  
}
