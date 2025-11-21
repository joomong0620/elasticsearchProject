package edu.og.es.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.og.es.dto.UserDto;
import edu.og.es.dto.UserDto.CreateDto;
import edu.og.es.dto.UserDto.UpdateDto;


public interface UserService {
	
	// 유저 등록
	String createUser(CreateDto createDto);

	// 유저 조회
	Page<UserDto.ResponseDto> findUserList(Pageable pageable);
	
	// 이름이 일치하는 유저 한 명 조회
	UserDto.ResponseDto  findUserByName(String name);
	
	// 유저 정보 수정
	UserDto.ResponseDto updateUser(String id, UpdateDto updateDto);

	// 유저 삭제
	void deleteUser(String id);


	


	


}
