# 🍅 Redis를 적용한 OAuth2 소셜 로그인 프로젝트

## 📌 프로젝트 소개
이 프로젝트는 OAuth2를 이용한 소셜 로그인 기능을 구현하고, Redis 캐싱을 적용하여 성능을 향상시킨 Spring Boot 애플리케이션입니다. Spring Security와 Redis를 활용하여 안전하고 빠른 인증 시스템을 제공합니다.

## 🛠 기술 스택
- 🍃 Spring Boot
- 🛡 Spring Security
- 🍅 Redis
- 🔑 OAuth2
- 🗄 JPA
- 🌐 RESTful API
- 📚 Swagger
- 🐘 Gradle

## 🌟 주요 기능
1. 💾 Redis를 이용한 캐싱 적용
2. 🔐 OAuth2를 이용한 소셜 로그인
3. 🔄 RESTful API를 통한 사용자 인증 및 관리
4. 🚨 Custom Exception 처리
5. 📖 Swagger를 이용한 API 문서화

## 💼 캐싱 적용 예시

### 사용자 프로필 조회 (@Cacheable)
```java
@Cacheable(cacheNames = "userCache", key = "#id + ':profile'", cacheManager = "cacheManager")
@Transactional(readOnly = true)
public UserResponse getUserProfileById(Long id) {
    User findUser = userRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    return UserResponse.builder()
            .nickname(findUser.getNickname())
            .email(findUser.getEmail())
            .role(findUser.getRole().name())
            .picture(findUser.getPicture())
            .build();
}
```

### 사용자 프로필 업데이트 (@CacheEvict)
```java
@CacheEvict(cacheNames = "userCache", key = "#principal.getId() + ':profile'")
@Transactional
public UserResponse updateUserProfile(UserPrincipal principal, UserUpdateRequest request) {
    User findUser = userRepository.findById(principal.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    User updateUser = findUser.updateProfile(request);

    return UserResponse.builder()
            .nickname(updateUser.getNickname())
            .email(updateUser.getEmail())
            .role(updateUser.getRole().name())
            .picture(updateUser.getPicture())
            .build();
}
```

## 📊 성능 비교

| 지표 | 캐싱 적용 전 | 캐싱 적용 후 | 변화율 |
|------|-------------|-------------|--------|
| 평균 응답 시간 (ms) | 3610 | 1918 | -46.9% |
| 처리량 (요청/초) | 229.71 | 355.42 | +54.7% |

**주요 개선사항:**
1. 평균 응답 시간 46.9% 감소
2. 초당 처리 요청 수 54.7% 증가
3. 응답 시간의 일관성 향상 (표준 편차 43% 감소)

## 📚 API 문서
Swagger UI를 통해 API 문서를 확인할 수 있습니다.
- 🔗 URL: `http://localhost:8080/swagger-ui.html`