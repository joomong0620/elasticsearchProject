package edu.og.es.Controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.og.es.dto.PageResponse;
import edu.og.es.dto.UserDto;
import edu.og.es.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController // 요청과 응답을 제어하는 컨트롤러 
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	// 유저 등록
	@PostMapping 
	public ResponseEntity<String> createUser(@RequestBody UserDto.CreateDto createDto){
		log.info("createUser: {}", createDto);
		return ResponseEntity.ok(userService.createUser(createDto));
	}
	
  // 유저 목록 조회
   // 한 페이지에 10씩, 유저이름으로 내림차순 목록 조회
   @GetMapping()
   public ResponseEntity<PageResponse<UserDto.ResponseDto>> findUserList(
         @PageableDefault(size=10, page=0,
         sort="name", direction = Sort.Direction.DESC) Pageable pageable){
	     // Pageable : 페이징 정보(조건)를 가진 객체(클래스)
	      
      return ResponseEntity.ok(new PageResponse<>(userService.findUserList(pageable)));
      
   }
   
   // 이름이 일치하는 유저 한 명 조회
   @GetMapping("/name")
   public ResponseEntity<UserDto.ResponseDto> findUserByName(@RequestParam("name") String name){
	   System.out.println(name);
	   return ResponseEntity.ok(userService.findUserByName(name));
   }
   

   // 유저 정보 수정
   // 아이디가 일치하는 유저의 이름, 나이, 활동중
   // 존재하는 유저가 없을 경우 에러 메세지
   @PutMapping("/{id}") 
   public ResponseEntity<UserDto.ResponseDto> updateUser(
		   @PathVariable("id") String id, @RequestBody UserDto.UpdateDto updateDto){
	   return ResponseEntity.ok(userService.updateUser(id, updateDto));
   }
   
   
   // 유저 삭제
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteUser(
		   @PathVariable String id){
	   
	   userService.deleteUser(id);
	   return ResponseEntity.ok().build();
   }
   
   
}
