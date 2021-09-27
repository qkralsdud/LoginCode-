<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp" %>

<div class="container">
	<form onsubmit="update(event, ${sessionScope.principal.id})">
	  <div class="form-group">
	    <input type="text"  value="${sessionScope.principal.username }"  class="form-control" placeholder="Enter username"   required="required"  maxlength="20" readonly="readonly">
	  </div>
	  <div class="form-group">
	    <input id="password" type="password"   value="${sessionScope.principal.password }"  class="form-control" placeholder="Enter password"  required="required" maxlength="20">
	  </div>
	  <div class="form-group">
	    <input  id="email"  type="email"   value="${sessionScope.principal.email }"  class="form-control" placeholder="Enter email" >
	  </div>
	  <button type="submit" class="btn btn-primary">회원수정</button>
	</form>
</div>

<script >
	function update(event, id) {
		 event.preventDefault();
		 let userUpdateDto = {
				 password: document.querySelector("#password").value,
				 email: document.querySelector("#email").value
		 };
		 
		 console.log(userUpdateDto);
         console.log(JSON.stringify(userUpdateDto));
         
         let response = await fetch("http://localhost:8080/board/"+id, {
             method: "put",
             body: JSON.stringify(userUpdateDto),
             headers: {
                "Content-Type": "application/json; charset=utf-8"
             }
          });
         
         let parseResponse = await response.json(); 
         
         console.log(parseResponse);
         
         if(parseResponse.code == 1){
            alert("업데이트 성공");
            location.href="/";
         }else{
            alert("업데이트 실패");
         }
	}
</script>

<%@ include file="../layout/footer.jsp" %>


   