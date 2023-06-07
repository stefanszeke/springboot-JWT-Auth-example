package com.stefan.springjwt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserPrincipal {
  
  private final String userID;
  private final String username;

}
