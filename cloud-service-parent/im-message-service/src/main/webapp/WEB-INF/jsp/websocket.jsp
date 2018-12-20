<%--
  Created by IntelliJ IDEA.
  User: 72429
  Date: 2018/12/7
  Time: 19:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>My WebSocket</title>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="./../js/websocket.js"></script>
</head>
<body> 测试一下WebSocket站点吧 <br/> <input id="message" type="text"/>
<button onclick="sendMessage()">发送消息</button>
<button onclick="closeWebSocket()">关闭WebSocket连接</button>
<div id="context"></div>
</body>
</html>