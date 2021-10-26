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
      <#if disappearingMessages??>
        <h3>Messages</h3>
        <ul title="Messages">
          <#list disappearingMessages as dm>
            <li>${dm}</li>
          </#list>
        </ul>
      </#if>
      <h3>Other Players</h3>
      <ul title="Other Players">
        <#list otherUsers as user>
          <a id="gameLink" href="/sendPrompt?user=${user}"><li>${user}</li></a>
        <#else>
          <p>No other users are logged in.
        </#list>
      </ul>
      <#if activePrompts??>
        <h3>Game Requests<h3>
        <ul title="Game Requests">
          <#list activePrompts as prompt>
            <a id="accept" href="/acceptPrompt?prompt=${prompt}"><li>${prompt}</li></a>
          </#list>
        </ul>
      </#if>
    <#else>
      <#if currentUser??>
        <h3>Other Players</h3>
        <p>No other players are currently logged in</p>
      <#else>
        <#if otherUsersQuantity??>
          <#if otherUsersQuantity == 1>
            <p>There is ${otherUsersQuantity} player currently logged in.
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
