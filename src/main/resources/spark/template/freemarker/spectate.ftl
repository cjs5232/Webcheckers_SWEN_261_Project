<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="refresh" content="10">
  <title>Queue</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<script>
window.data = {
  };
</script>

<body>
  <div class="page">

    <h1>Web Checkers | ${title}</h1>

    <!-- Provide a navigation bar -->
    <#include "nav-bar.ftl" />

    <div class="body">

      <!-- Provide a message to the user, if supplied. -->
      <#include "message.ftl" />

        <#if activeGames??>
            <h3>Active Games</h3>
            <ul title="Active Games">
                <#list activeGames as game>
                    <a id="gameLink" href="/spectate?id=${game.getIdAsString()}"><li>${game.getPlayersPretty()}</li></a>
                <#else>
                    <p>No games are currently being played</p>
                </#list>
            </ul>
        </#if>
    </div>

  </div>
</body>

</html>