package com.s1350.sooljangmacha.user.service;


import com.s1350.sooljangmacha.global.exception.BaseException;
import com.s1350.sooljangmacha.global.exception.BaseResponseCode;
import com.s1350.sooljangmacha.global.utils.JwtUtil;
import com.s1350.sooljangmacha.user.dto.request.LoginReq;
import com.s1350.sooljangmacha.user.dto.response.LoginRes;
import com.s1350.sooljangmacha.user.entity.Provider;
import com.s1350.sooljangmacha.user.entity.User;
import com.s1350.sooljangmacha.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 로그인
    public LoginRes login(LoginReq request) {
        User user = userRepository.findByEmailAndProviderAndIsEnable(request.getEmail(), Provider.valueOf(request.getProvider()), true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        return LoginRes.toEntity(jwtUtil.issuedAccessToken(user.getId()));
    }

    // 로그아웃

    // 회원탈퇴

    // 프로필 불러오기

    // 프로필 편집

    // 포장마차 좋아요 목록 조회
}
