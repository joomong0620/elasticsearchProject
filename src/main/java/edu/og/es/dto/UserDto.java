package edu.og.es.dto;

import edu.og.es.document.UserDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public class UserDto {
	
	// 생성
	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	public static class CreateDto{
		private String id;
		private String name;
		private Integer age;
		private Boolean isActive;
		
		// toDocument(): DTO를 document 객체로 변환하는 메소드
		public UserDocument toDocument() {
			return UserDocument.builder()
					.id(this.id)
					.name(this.name)
					.age(this.age)
					.isActive(this.isActive)
					.build();
		}
	}
	
	// 조회
	@Builder
	@Getter
	@Setter
	@ToString
	public static class ResponseDto{
		private String id;
		private String name;
		private Integer age;
		
		// Document -> DTO로 변환하는 메소드
		public static ResponseDto toDto(UserDocument user) {
			return ResponseDto.builder()
					.id(user.getId())
					.name(user.getName())
					.age(user.getAge())
					.build();
			
					
		}
	}
	
	
	// 수정
	@Getter
	@Setter
	@ToString
	public static class UpdateDto{
		private String name;
		private Integer age;
		private Boolean isActive;
	
	
	}
	
	

}
