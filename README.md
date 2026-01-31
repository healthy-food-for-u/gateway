# gateway

> 사용자가 헬스포유 서비스를 안전하고 편리하게 이용할 수 있도록 돕는 API Gateway입니다.

모든 클라이언트 요청의 단일 진입점으로서, 보안 인증과 라우팅을 담당합니다.
사용자의 JWT를 검증하여 유효한 요청인지 확인하고, 서비스 전반의 보안 가이드라인을 준수합니다.

- - - - - -

## 핵심 기능

- **인증 필터 (JWT Filter):** 모든 인가된 요청에 대해 JWT 유효성을 검사하고 사용자 정보를 헤더에 담아 전달합니다.
- **라우팅 관리:**
  - `Public`: 질병 정보 및 레시피 서비스 (인증 미필수)
  - `Private`: 레시피 즐겨찾기, 유저 정보 (인증 필수)
- **CORS 설정:** 프론트엔드 서비스와의 안전한 통신을 위한 교차 출처 리소스 공유 설정

---

## Security & CORS
- **JWT Filter**: `Private` 경로 접근 시 유효한 토큰인지 검증합니다.
- **CORS Allowed Origins**:
    - `http://localhost:5173` (Local Development)
    - `http://[EC2_PUBLIC_IP]` (Production)

---

## 기술 스택
- **Framework:** Java 21, Spring Boot, Spring Cloud Gateway (WebFlux), Spring Cloud Config
- **Security:** JJWT (io.jsonwebtoken) 0.12.6
- **Monitoring & Utilities:** Spring Boot Actuator
- **Build Tool:** Maven

---

## API 라우팅 설계

|서비스명|외부 요청 경로 (Path)|내부 변환 경로 (Rewrite)|인증(JWT) 여부|
| :--- | :--- | :--- | :---: |
|**Auth (Public)** |`/api/v1/auth/**`|`/api/auth/**`|X|
| **User (Private)**|`/api/v1/users/**`|`/api/users/**`|O|
|**Health (Public)**|`/api/v1/health/diseases/**`|`/api/diseases/**`|X|
|**Health (Private)**|`/api/v1/health/favorites/**`|`/api/favorites/**`|O|


---

## Roadmap & Updates
- [ ] Redis를 이용한 Rate Limiting (처리량 제한) 기능 추가
- [ ] 서비스별 상세 로깅 및 트레이싱 시스템 통합
- [ ] 테스트 코드 보완 및 API 문서화(Swagger) 연결