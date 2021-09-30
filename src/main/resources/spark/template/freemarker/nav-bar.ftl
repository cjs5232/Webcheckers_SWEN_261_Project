 <div class="navigation">
  <#if currentUser??>
    <a href="/">Home</a> |
    <form id="signout" action="/signout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">Log out [${currentUser.name}]</a>
    </form>
  <#else>
    <a href="/login">Log in</a>
  </#if>
 </div>
