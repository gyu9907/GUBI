<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">
   
   alert("${requestScope.message}");       // 메시지 출력해주기
   const jsonStr = "${requestScope.json}"; // json

   const jsonFrm = document.jsonFrm;
   jsonFrm.json.value = jsonStr;

   jsonFrm.action = "${requestScope.action}";
   jsonFrm.method = "post";
   jsonFrm.submit(); // form submit
   
</script>

<form name="jsonFrm">
   <input type="hidden" name="json"/>
   <input type="hidden"/>
</form>