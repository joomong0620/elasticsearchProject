package edu.og.es.common.exception;


public class UserNotFoundException extends RuntimeException{
	
	public UserNotFoundException() { // 생성자 == 클래스명
		super("존재하지 않는 사용자입니다.");
		
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}
	
	
}
