<div class="navigation">
  <#if currentUser??>
    <a href="/">Home</a> |
    <form id="signout" action="/logout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">Log out [${currentUser}]</a>
    </form>
  <#else>
    <a href="/login">Sign in</a>
  </#if>
</div>
