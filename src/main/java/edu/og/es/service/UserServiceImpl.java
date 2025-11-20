package edu.og.es.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.og.es.common.exception.UserNotFoundException;
import edu.og.es.document.UserDocument;
import edu.og.es.dto.UserDto;
import edu.og.es.dto.UserDto.CreateDto;
import edu.og.es.dto.UserDto.ResponseDto;
import edu.og.es.repository.UserDocumentRepository;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserDocumentRepository userRepository;

	// 유저 등록
	public String createUser(CreateDto createDto) {
		// createDto : 사용자가 입력한 정보
		// toDocument() : DTO를 UserDocument 객체로 변환하는 메소드
		UserDocument user = createDto.toDocument();
		return userRepository.save(user).getName();
	}

	// 유저 목록 조회
	@Override
	public Page<ResponseDto> findUserList(Pageable pageable) {
		Page<UserDocument> page = userRepository.findAll(pageable);
		return page.map(UserDto.ResponseDto::toDto); // Document -> DTO로 변환
		// :: -> 메소드 레퍼런스용 연산자
		// Page 안에 UserDocument만 꺼내서 반복문 돌려서 Dto에 있는 거 호출할게
		// 왜? -> map에서 하나씩 애초에 꺼내줌

	}

	// 이름이 일치하는 유저 한 명 조회
	@Transactional(readOnly = true)
	@Override
	public ResponseDto findUserByName(String name) {
		UserDocument user =  userRepository.findByName(name)					
		.orElseThrow(() -> new UserNotFoundException()); // 직접 생성
		return ResponseDto.toDto(user);
		
	}
}
