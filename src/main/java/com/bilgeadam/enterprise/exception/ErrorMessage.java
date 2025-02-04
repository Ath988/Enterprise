package com.bilgeadam.enterprise.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorMessage {
	int code;
	String messsage;
	Boolean success;
	List<String> fields;
}