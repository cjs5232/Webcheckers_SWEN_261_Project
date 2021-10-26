<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Queue</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<script>
window.data = {
    "currentUser" : "${currentUser}",
    "gameController" : "${gameController}"
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

</div>
</body>

//TODO
<script>
    if (currentUser.getPlayerStatus() == 0) {
        gameController.putInQueue(currentUser)
        currentUser.setPlayerStatus(1)
   }

    if (gameController.inQueue(currentUser)) {
        location.replace = '/queue'
        currentUser.setPlayerStatus(2)
    }

    if (currentUser.getPlayerStatus() == 2) {
        location.replace = '/home'
    }

</script>
</html>