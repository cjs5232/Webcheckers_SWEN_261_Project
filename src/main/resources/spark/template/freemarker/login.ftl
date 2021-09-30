<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Login</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <label for="uname">Username:</label>
    <input type="text" id="uname" name="uname"><br><br>

    <label for="pwd">Password:</label>
    <input type="text" id="pwd" name="pwd"><br><br>

    <input type="submit" value="Submit">

  </div>

</div>
</body>

</html>