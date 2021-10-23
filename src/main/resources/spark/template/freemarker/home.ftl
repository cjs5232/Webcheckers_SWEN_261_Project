<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
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

    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->
    <#if otherUsers??>
      <h3>Other Players</h3>
      <ul title="Other Players">
        <#list otherUsers as user>
          <a id="gameLink" href="/game?user=${user}"><li>${user}</li></a>
        <#else>
          <p>No other users are logged in.
        </#list>
      </ul>
    <#else>
      <#if currentUser??>
        <h3>Other Players</h3>
        <p>No other players are logged in</p>
      <#else>
        <#if otherUsersQuantity??>
          <#if otherUsersQuantity == 1>
            <p>There is ${otherUsersQuantity} player logged in.
          <#else>
            <p>There are ${otherUsersQuantity} players currently logged in.
          </#if>
        </#if>
      </#if>
      
    </#if>

  </div>

</div>
</body>

</html>
