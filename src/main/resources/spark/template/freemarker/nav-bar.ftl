 <div class="navigation">
  <#if currentUser??>${currentUser}</#if>
    <a href="/">Home</a> |
    <form id="signout" action="/signout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">[Log out]</a>
    </form>
    <a href="/login">Log in</a>
 </div>
