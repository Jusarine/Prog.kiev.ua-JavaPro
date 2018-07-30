<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Archiver</title>
  </head>
  <body>
     <div align="center">

         <form action="/upload_file" enctype="multipart/form-data" method="POST">
             File: <input type="file" name="file">
             <input type="submit" value="Upload file">
         </form>

         <form action="/upload_archive" enctype="multipart/form-data" method="POST">
             Archive: <input type="file" name="file" accept="application/zip">
             <input type="submit" value="Upload archive">
         </form>

         <input type="submit" value="View files" onclick="window.location='/files';" />
         <input type="submit" value="View archives" onclick="window.location='/archives';" />

      </div>
  </body>
</html>
