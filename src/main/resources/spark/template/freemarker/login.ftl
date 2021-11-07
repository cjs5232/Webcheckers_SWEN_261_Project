<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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

    <#if addUserError??>
      <div id="errorMessage" class="ERROR">${addUserError}</div>
    </#if>
  
    <form method="POST" action="/addPlayer">
        <label for="name">Username:</label>
        <#if username??>
          <input name="name" value="${username}" />
        <#else>
          <input name="name" id="name">
        </#if>

        <input type="submit" value="Submit"/>
    </form>

  </div>

</div>
</body>

<script>
  window.onload = function(){
    document.getElementById("name").focus();
  }
</script>

</html>