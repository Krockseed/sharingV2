# sharingV2

<h1>file structure</h1>
<code>
├── src
│   ├── main
│   │   ├── generated
│   │   ├── java
│   │   │   └── hello
│   │   │       └── sharingv2
│   │   │           ├── SharingV2Application.java
│   │   │           ├── domain
│   │   │           │   ├── BaseTimeEntity.java
│   │   │           │   ├── comment
│   │   │           │   │   ├── Comment.java
│   │   │           │   │   ├── dto
│   │   │           │   │   │   └── CommentDto.java
│   │   │           │   │   ├── repository
│   │   │           │   │   │   └── CommentRepository.java
│   │   │           │   │   └── service
│   │   │           │   │       ├── CommentService.java
│   │   │           │   │       └── CommentServiceImpl.java
│   │   │           │   ├── member
│   │   │           │   │   ├── Member.java
│   │   │           │   │   ├── MemberDetails.java
│   │   │           │   │   ├── Role.java
│   │   │           │   │   ├── controller
│   │   │           │   │   │   └── AuthController.java
│   │   │           │   │   ├── dto
│   │   │           │   │   │   ├── MemberDefaultDto.java
│   │   │           │   │   │   └── MemberSignUpDto.java
│   │   │           │   │   ├── exception
│   │   │           │   │   │   ├── MemberException.java
│   │   │           │   │   │   └── MemberExceptionType.java
│   │   │           │   │   ├── repository
│   │   │           │   │   │   └── MemberRepository.java
│   │   │           │   │   └── service
│   │   │           │   │       ├── MemberService.java
│   │   │           │   │       └── MemberServiceImpl.java
│   │   │           │   └── post
│   │   │           │       ├── Post.java
│   │   │           │       ├── dto
│   │   │           │       │   └── PostDto.java
│   │   │           │       ├── exception
│   │   │           │       │   ├── PostException.java
│   │   │           │       │   └── PostExceptionType.java
│   │   │           │       ├── repository
│   │   │           │       │   └── PostRepository.java
│   │   │           │       └── service
│   │   │           │           ├── PostService.java
│   │   │           │           └── PostServiceImpl.java
│   │   │           └── global
│   │   │               ├── common
│   │   │               │   ├── JwtAccessDeniedHandler.java
│   │   │               │   ├── JwtAuthenticationEntryPoint.java
│   │   │               │   ├── JwtAuthenticationProcessingFilter.java
│   │   │               │   ├── JwtFilter.java
│   │   │               │   ├── JwtLoginFailureHandler.java
│   │   │               │   ├── JwtLoginSuccessHandler.java
│   │   │               │   ├── JwtTokenProvider.java
│   │   │               │   └── MemberDetailsService.java
│   │   │               ├── config
│   │   │               │   └── SecurityConfig.java
│   │   │               └── exception
│   │   │                   ├── BaseException.java
│   │   │                   └── BaseExceptionType.java
│   │   └── resources
│   │       ├── application.yml
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── hello
│               └── sharingv2
│                   ├── SharingV2ApplicationTests.java
│                   ├── domain
│                   │   ├── PostTest.java
│                   │   ├── comment
│                   │   │   └── service
│                   │   │       └── CommentServiceImplTest.java
│                   │   └── member
│                   │       ├── repository
│                   │       │   └── MemberRepositoryTest.java
│                   │       └── service
│                   │           └── MemberServiceImplTest.java
│                   └── global
│                       └── common
│                           └── JwtTokenProviderTest.java
</code>
