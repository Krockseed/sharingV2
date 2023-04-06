package hello.sharingv2.global.common;

import hello.sharingv2.domain.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        String accessToken = jwtTokenProvider.createAccessToken(username);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        response.setStatus(HttpServletResponse.SC_OK);
        jwtTokenProvider.setAccessTokenHeader(response, accessToken);
        jwtTokenProvider.setRefreshTokenHeader(response, refreshToken);

        memberRepository.findByUsername(username)
                        .ifPresent(member -> member.updateRefreshToken(refreshToken));

        log.info("로그인에 성공했습니다.");

        response.getWriter().write("success!");
    }
}
