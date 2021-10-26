<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
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


  </div>

  <#if (gameController.inQueue(currentUser))>
      <script>
         window.location = '/'
      </script>
  <#else>
     <script>
        window.location = '/game'
     </script>
  </#if>

</div>
</body>


</script>
</html>