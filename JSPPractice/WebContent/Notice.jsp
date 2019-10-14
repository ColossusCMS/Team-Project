<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>알림</title>
    <style>
        body {
            width: 270px;
            margin: 0;
            margin-left: 1px;
            font-family: "맑은 고딕";
        }
        body::-webkit-scrollbar {
            display: none;
        }
        header {
            width: 270px;
            height: 75px;
            border: 1px solid black;
        }
        #noticeTitle {
            display: inline-block;
            float: left;
            background-color: skyblue;
            padding: 5px;
        }
        #noticeTitle h3 {
            margin: 20px 0;
        }
        
        #noticeContent {
            float: left;
            margin-left: 3px;
            width: 180px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        #noticeContent h4 {
            margin: 5px 0;
        }
        #noticeContent p {
            margin: 0;
            font-size: 10px;
        }
        
        article {
            clear: both;
        }
        
        #feedTitle {
            border: 1px solid black;
            height: 30px;
            padding-top: 1px;
            padding-left: 5px;
            width: 265px;
        }
        #feedTitle h3 {
            margin: 0;
        }
        
        a {
            text-decoration: none;
            color: black;
        }
        a div:hover {
            background-color: burlywood;
        }
        .feedContent {
            border: 1px solid #ccc;
            background-color: beige;
            position: relative;
            width: 255px;
            height: 60px;
            margin: 5px auto;
            padding: 0;
        }
        span {
            position: absolute;
        }
        .m {
            left: 10px;
            top: 15px;
            width: 30px;
            height: 30px;
        }
        
        .t {
            left: 100px;
            top: 5px;
            font-size: 15px;
            font-weight: bold;
            width: 150px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        
        .w {
            width: 40px;
            left: 50px;
            top: 7px;
            font-size: 12px;
            text-align: center;
            background-color: skyblue;
        }

        .d {
            left: 50px;
            top: 30px;
            font-size: 10px;
            width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    </style>
</head>
<body>
    <header>
        <div id="noticeTitle">
            <h3>공지사항</h3>
        </div>
        <div id="noticeContent">
            <h4>알립니다.</h4>
            <p>헌혈 참여 캠페인이 진행중입니다.<br />
            참여를 원하시는 분들께서는 관리자에게<br />
            신청 메시지를 보내주세요.</p>
        </div>
    </header>
    <article>
        <div id="feedTitle">
            <h3>뉴스피드</h3>
        </div>
        <a href="#">
            <div class = "feedContent">
                <span class="m">
                    <img src="http://yaahq.dothome.co.kr/images/circle.png">
                </span>
                <span class="w">일정</span>
                <span class="t">여기는 제목</span>
                <span class="d">휴가 신청이 승인되었습니다.<br />일정을 확인하세요.</span>
            </div>
        </a>
        <a href="#">
            <div class = "feedContent">
                <span class="m">
                    <img src="http://yaahq.dothome.co.kr/images/circle.png">
                </span>
                <span class="w">게시판</span>
                <span class="t">여기는 제목 자리입니다.</span>
                <span class="d">"...."게시물에 답글이 달렸습니다.</span>
            </div>
        </a>
        <a href="#">
            <div class = "feedContent">
                <span class="m">
                    <img src="http://yaahq.dothome.co.kr/images/circle.png">
                </span>
                <span class="w">업무</span>
                <span class="t">여기는 제목 자리입니다.</span>
                <span class="d">기안서 제출이 오늘까지입니다.<br />확인하시고 제출완료해주세요.</span>
            </div>
        </a>
    </article>
</body>
</html>