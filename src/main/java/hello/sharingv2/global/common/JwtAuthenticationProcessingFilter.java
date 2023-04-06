package hello.sharingv2.global.common;

import hello.sharingv2.domain.member.Member;
import hello.sharingv2.domain.member.MemberDetails;
import hello.sharingv2.domain.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 1. refresh-token 이 존재하는 경우
 *  - access-token 이 존재한다면 인증 성공 -> access-token 재발급
 *  - access-token 이 존재하지 않는다면 -> access-token 재발급
 * 2. refresh-token 이 존재하지 않는 경우
 *  - access-token 이 존재하는 경우 -> 인증은 성공, refresh 재발급 x
 *  - access-token 이 존재하지 않는다면 인증 실패
 */

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    private static final String NO_CHECK_URL = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtTokenProvider.getRefreshToken(request)
                .filter(jwtTokenProvider::validateToken)
                .orElse(null);

        //refresh-token 존재한다면 유효한지 검증 후 access-token 재발급
        if (refreshToken != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            memberRepository.findByRefreshToken(refreshToken).ifPresent(
                    m -> jwtTokenProvider.setAccessTokenHeader(response, jwtTokenProvider.createAccessToken(m.getUsername()))
            );
            return;
        }

        String accessToken = jwtTokenProvider.getAccessToken(request)
                .filter(jwtTokenProvider::validateToken)
                .orElse(null);

        if (accessToken != null) {
            String payload = jwtTokenProvider.getPayload(accessToken);
            memberRepository.findByUsername(payload).ifPresent(this::saveAuthentication);
        }
    }

    private void saveAuthentication(Member member) {
        MemberDetails memberDetails = new MemberDetails(member);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(memberDetails.getUsername(), memberDetails.getPassword(), memberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
