<div class="navigation">
  <#if currentUser??>
    <a href="/">Home</a> |
    <form id="signout" action="/logout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">Log out [${currentUser}]</a>
    </form> |
    <#if activeGame??>
      <a href="/game?gameID=${activeGame.id}">Back To Game</a> |
    <#else>
      <#if gameID??>
        Find Game |
      <#else>
        <a href="/queue">Find Game</a> |
      </#if>
    </#if>

    <#if gameID??>
      Spectate a Game | Replay a Game
    <#else>
      <a href=/spectateBrowse>Spectate a Game</a> |
      <a href="/replayBrowse">Replay a Game</a>
    </#if>
  <#else>
    <a href="/login">Sign in</a>
  </#if>
</div>
