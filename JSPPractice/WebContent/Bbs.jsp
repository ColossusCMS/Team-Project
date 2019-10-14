<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="WebViewTest.BbsDao" %>
<%@ page import="WebViewTest.Bbs" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 테스트 중</title>
<style>
        body {
            width: 270px;
            margin: 0;
        }
        body::-webkit-scrollbar {
            display: none;
        }
        a {
            text-decoration: none;
            color: black;
        }
        a div:hover {
            background-color: burlywood;
        }
        a div {
            border: 1px solid #ccc;
            background-color: beige;
            position: relative;
            width: 270px;
            height: 50px;
            margin: auto;
            margin-bottom: 2px;
            padding: 0;
        }
        span {
            position: absolute;
        }
        .m {
            left: 0;
            top: 17px;
            font-size: 10px;
            width: 60px;
        }
        
        .t {
            left: 60px;
            top: 15px;
            font-size: 15px;
            width: 155px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        
        .w {
            right: 5px;
            top: 5px;
            font-size: 12px;
        }

        .d {
            right: 5px;
            top: 25px;
            font-size: 12px;
        }
    </style>
</head>
<body>
	<%
		BbsDao bbsDao = new BbsDao();
		ArrayList<Bbs> list = bbsDao.getList();
	%>
	<a href="#">
        <div>
            <span class="m">[자유게시판]</span>
            <span class="t">여기는 제목 자리입니다. 제목이라고</span>
            <span class="w">홍길동</span>
            <span class="d">2019-10-10</span>
        </div>
    </a>
</body>
</html>