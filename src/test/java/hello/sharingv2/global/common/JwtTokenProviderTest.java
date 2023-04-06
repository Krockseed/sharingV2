package hello.sharingv2.global.common;

import hello.sharingv2.domain.member.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JwtTokenProviderTest {

    @Autowired JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secret}")
    private String secretKey;

    private final JwtTokenProvider inValidJwtTokenProvider = new JwtTokenProvider(
            "b3dmb3JvZ3VoYWVvZ2hvbmJ2b3ViZG13cWxAcWZsbmlqaW8zOW9qMDAyMC0yLWYyamlvc2VqMjM5MTIxMTFsYwo=",
            100, 1000
    );

    @Test
    void 토큰생성_성공() throws Exception {
        //given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        Authentication authentication = new UsernamePasswordAuthenticationToken("ww@mail", "1234", authorities);

        //when
        String token = jwtTokenProvider.createAccessToken(authentication.getName());

        //then
        assertNotNull(token);
        System.out.println("token = " + token);
    }

    @Test
    void 토큰_payload_조회() throws Exception {
        //given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        Authentication authentication = new UsernamePasswordAuthenticationToken("ww@mail", "1234", authorities);

        //when
        String token = jwtTokenProvider.createAccessToken(authentication.getName());
        String payload = jwtTokenProvider.getPayload(token);

        //then
        assertNotNull(payload);
        System.out.println("payload = " + payload);
    }

    @Test
    void 유효하지않은_시크릿키로_payload_조회() throws Exception {
        //given
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        Authentication authentication = new UsernamePasswordAuthenticationToken("ww@mail", "1234", authorities);

        //when
        String token = jwtTokenProvider.createAccessToken(authentication.getName());

        //then
//        inValidJwtTokenProvider.getAuthentication(token);
        assertThrows(io.jsonwebtoken.security.SignatureException.class, () -> inValidJwtTokenProvider.getAuthentication(token));
    }

    @Test
    void 만료된_토큰으로_조회() throws Exception {
        //given
        byte[] keyBytes =  Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        final String expiredToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(String.valueOf(1L))
                .setExpiration(new Date((new Date()).getTime() - 1))	// 8
                .compact();

        //then
//        jwtTokenProvider.getAuthentication(expiredToken);
        assertThrows(ExpiredJwtException.class, () -> jwtTokenProvider.getAuthentication(expiredToken));
    }
}