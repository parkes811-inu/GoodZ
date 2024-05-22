# 프로젝트 구조

1. fragments 폴더 내에서 기능 단위로 html 파일 작성
- 각자 맡은 기능 폴더 내에 작업
- 공통적으로 들어가는 부분은 common 폴더에 추가
ex) footer, header 등

2. layouts 폴더
- 각 기능들을 하나의 표준 포맷으로 정의

3. 각 기능 별 폴더에서 피그마 디자인 참고하여 화면 정의
- html 화면 정의 시 아래 코드 추가
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{/layouts/--- 여기에 내가 작업한 layout 파일 이름  ---}"
      >
pay - 나연
post - 도희
product - 은서
user - 정용