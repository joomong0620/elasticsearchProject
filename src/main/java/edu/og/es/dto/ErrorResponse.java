package edu.og.es.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
	private String code; //에러코드
	private String message; //사용자에게 보여줄 메세지
	private LocalDateTime timestamp; // 에러 발생 시각
	
	// 정적 팩토리 메소드 : new 대신 쓰는 객체 생성용 static 메소드
	// of : ErrorResponse.of(500, 서버에러) -> 500번이랑 서버에러로 이루어진 에러 만들어줘 라는 것과 같음
	public static ErrorResponse of(String code, String message) {
		return ErrorResponse.builder()
				.code(code)
				.message(message)
				.timestamp(LocalDateTime.now())
				.build();
	}
}
