<%-- 
    Document   : admin
    Created on : Jun 16, 2018, 2:08:44 PM
    Author     : USER
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="thucnh" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Welcome to Book Store !</h2>
        <a href="insert.jsp" >Click here to insert new book</a>
        <thucnh:if test="${requestScope.STATUS !=null}" >
            <font color="red" >
            <span>${requestScope.STATUS}</span>
            </font>
        </thucnh:if>
        <form action="MainController" method="POST"><br/>
            Search by Title<input type="text" name="txtSearch" value=""/>
            <input type="submit" name="btAction" value="Search"/>
        </form>
        <thucnh:if test="${requestScope.INFO !=null}">
            <thucnh:if test="${not empty requestScope.INFO}" var="checkData">
                <table border="1">
                    <thead>
                        <tr>
                            <th>No</th>
                            <th>Book ID</th>
                            <th>Book Title</th>
                            <th>Description</th>
                            <th>Author</th>
                            <th>Publisher</th>
                            <th>Price</th>
                            <th>Delete</th>
                            <th>Edit</th>
                        </tr>
                    </thead>
                    <thucnh:forEach items="${requestScope.INFO}" var="dto" varStatus="counter">
                        <tbody>
                            <tr>
                                <td>${counter.count}</td>
                                <td>${dto.bookID}</td>
                                <td>${dto.bookTitle }</td>
                                <td>${dto.description}</td>
                                <td>${dto.author}</td>
                                <td>${dto.publisher}</td>
                                <td>${dto.price}</td>
                                <td>
                                    <thucnh:url var="deleteLink" value="MainController">
                                        <thucnh:param name="txtSearch" value="${param.txtSearch}"></thucnh:param>
                                        <thucnh:param name="idDelete" value="${dto.bookID}"></thucnh:param>
                                        <thucnh:param name="btAction" value="Delete"></thucnh:param>
                                    </thucnh:url>
                                    <a href="${deleteLink}">Delete</a>
                                </td>
                                <td>
                                    <form method="POST" action="MainController">
                                        <input type="hidden" name="idEdit" value="${dto.bookID}"/>
                                        <input type="hidden" name="txtSearch" value="${param.txtSearch}"/>
                                        <input type="submit" name="btAction" value="Edit"/>
                                    </form>
                                </td>
                            </tr>
                        </tbody>
                    </thucnh:forEach>
                </table>
            </thucnh:if>
            <thucnh:if test="${!checkData}">
                <h2>No record found</h2>
            </thucnh:if>
        </thucnh:if>
    </body>
</html>
