<%-- 
    Document   : update
    Created on : Jun 17, 2018, 3:37:08 PM
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
        <h1>Update page</h1>
        <form action="MainController" method="POST"><br/>
            Book ID<input type="text" name="txtBookID" value="${requestScope.DTO.bookID}" readonly="true"/>
            <font color="red" >
            ${requestScope.INVALID.bookIDErr}
            </font><br/>
            Book Title<input type="text" name="txtBookTitle" value="${requestScope.DTO.bookTitle}"/>
            <font color="red" >
            ${requestScope.INVALID.titleErr}
            </font>
            <br/>
            Description<input type="text" name="txtDescription" value="${requestScope.DTO.description}"/>
            <font color="red" >
            ${requestScope.INVALID.descriptionErr}
            </font>
            <br/>
            Author<input type="text" name="txtAuthor" value="${requestScope.DTO.author}"/>
            <font color="red" >
            ${requestScope.INVALID.authorErr}
            </font>
            <br/>
            Publisher<input type="text" name="txtPublisher" value="${requestScope.DTO.publisher}"/>
            <font color="red" >
            ${requestScope.INVALID.publisherErr}
            </font>
            <br/>
            Price<input type="text" name="txtPrice" value="${requestScope.DTO.price}"/>
            <font color="red" >
            ${requestScope.INVALID.priceErr}
            </font>
            <input type="submit" name="btAction" value="Update"/>
            <input type="hidden" name="txtSearch" value="${param.txtSearch}">
        </form>
    </body>
</html>
