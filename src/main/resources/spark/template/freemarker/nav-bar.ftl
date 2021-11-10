<div class="navigation">
  <#if currentUser??>
    <a href="/">Home</a> |
    <form id="signout" action="/logout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">Log out [${currentUser}]</a>
    </form> |
    <a href="/queue">Find Game</a> |
    <a href=/spectateBrowse>Spectate a Game</a>
  <#else>
    <a href="/login">Sign in</a>
  </#if>
</div>
