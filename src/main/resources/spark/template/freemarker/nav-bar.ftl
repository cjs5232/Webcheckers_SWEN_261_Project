 <div class="navigation">
  <#if currentUser??>
    <a href="/">Home</a> |
    <form id="logout" action="/logout" method="post">
      <a href="#" onclick="event.preventDefault(); logout.submit();">Log out [${currentUser}]</a>
    </form>
  <#else>
    <a href="/login">Log in</a>
  </#if>
 </div>
