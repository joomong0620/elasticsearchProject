package edu.og.es.common.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j 
public class LoggingFilter implements Filter{@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(request instanceof HttpServletRequest req &&
				response instanceof HttpServletResponse resp) {
			// instanceof 체크 + 다운캐스팅을 한번에 진행
			// -> (HttpServletRequest)req 
			// => true면 다운캐스팅 가능
			// URL, 메소드, 시간
			String url = req.getRequestURI();
			String method = req.getMethod();
			long startTime = System.currentTimeMillis();
			
			
			
			// 요청 로그
			log.trace("[Incoming Request] : {} {}", method, url);
			
			chain.doFilter(request, response);
			   long duration = System.currentTimeMillis() - startTime;
	            int status = resp.getStatus();
	
	            // 응답 로그 (status + 처리시간)
	            // 예: [Outgoing Response] : POST /products 201 35ms
	            log.trace("[Outgoing Response] : {} {} {} {}ms", method, url, status, duration);
		}else {
			// HttpServletRequest가 아닌 경우
			chain.doFilter(request, response); // => 원래 하던 일 함
		}
	}

}
