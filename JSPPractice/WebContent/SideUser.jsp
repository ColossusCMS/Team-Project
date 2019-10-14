<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>사용자 목록</title>
    <style>
        body {
            font: 14px "맑은 고딕";
            margin: 0;
        }
        .container {
            width: 125px;
        }
        .group {
            background-color: #ccc;
            border: 1px solid black;
        }
        .group h3 {
            font-weight: 700;
            padding: 5px;
            margin: 0;
        }
        .person {
            background-color: aliceblue;
            padding: 5px;
            border-bottom: 1px solid black;
        }
        .person h2, .person p{
            margin: 0;
        }
        .person p {
            text-align: right;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="group">
            <h3>그룹1</h3>
        </div>
        <div class="person">
            <h2>김대리</h2>
            <p>온라인</p>
        </div>
        <div class="person">
            <h2>김과장</h2>
            <p>다른 업무 중</p>
        </div>
        <div class="group">
            <h3>그룹2</h3>
        </div>
        <div class="person">
            <h2>김대리</h2>
            <p>온라인</p>
        </div>
        <div class="person">
            <h2>유과장</h2>
            <p>회의중</p>
        </div>
        <div class="group">
            <h3>그룹3</h3>
        </div>
        <div class="person">
            <h2>이사원</h2>
            <p>온라인</p>
        </div>
        <div class="person">
            <h2>박차장</h2>
            <p>휴가중</p>
        </div>
    </div>
</body>
</html>