package com.skeleton.Command;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;

@Introspected
@Serdeable
public class QueryCommand {
  @Email
  @Nullable
  private String email;
  @Nullable
  private String name;

  public QueryCommand() {
  }

  @Creator
  public QueryCommand(@Nullable String email, @Nullable String name) {
    this.email = email;
    this.name = name;
  }

  @Nullable
  @Email
  public String getEmail() {
    return email;
  }

  public void setEmail(@Email @Nullable String email) {
    this.email = email;
  }

  @Nullable
  public String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }
}