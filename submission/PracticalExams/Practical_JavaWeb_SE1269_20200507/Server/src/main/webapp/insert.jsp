<%-- 
    Document   : insert
    Created on : Jun 12, 2018, 12:38:05 PM
    Author     : USER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Insert new book Page</h1>

        <form action="MainController" method="POST">
            Book ID     <input type="text" name="txtBookID" value="${param.txtBookID}" />  
            <font color="red" >
            ${requestScope.INVALID.bookIDErr}
            </font><br/>
            Book Title  <input type="text" name="txtBookTitle"  value="${param.txtBookTitle}" />
            <font color="red" >
            ${requestScope.INVALID.titleErr}
            </font>
            <br/>
            Description <input type="text" name="txtDescription"  value="${param.txtDescription}" />
            <font color="red" >
            ${requestScope.INVALID.descriptionErr}
            </font>
            <br/>
            Author      <input type="text" name="txtAuthor"  value="${param.txtAuthor}" />
            <font color="red" >
            ${requestScope.INVALID.authorErr}
            </font>
            <br/>
            Publisher   <input type="text" name="txtPublisher" value="${param.txtPublisher}" />
            <font color="red" >
            ${requestScope.INVALID.publisherErr}
            </font>
            <br/>
            Price       <input type="text" name="txtPrice"  value="${param.txtPrice}" />
            <font color="red" >
            ${requestScope.INVALID.priceErr}
            </font>
            <br/>
            <input type="submit" name="btAction" value="Insert">
        </form>
    </body>
</html>
