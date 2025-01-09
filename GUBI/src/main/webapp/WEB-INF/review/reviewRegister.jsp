<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
	//	   /GUBI
%>   
<script type="text/javascript" src="<%= ctxPath%>/js/jquery-3.7.1.min.js"></script>
<script src="../js/jquery-3.7.1.min.js" type="text/javascript"></script>
<script src="../bootstrap-4.6.2-dist/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../js/review/review.js" type="text/javascript"></script>

<link rel="stylesheet" href="<%= ctxPath%>/css/review/review.css">
	    
	  <!-- Modal -->
	<div class="modal fade" id="addReviewModal" data-backdrop="static">
	  <div class="modal-dialog modal-dialog-centered">
	    <div class="modal-content">
	      
	      <!-- Modal header -->
	      <div class="modal-header">
	        <h5 id="modal-title" class="modal-title">Review 작성</h5>
	        <button type="button" id="closeBtn" class="close" data-dismiss="modal" aria-label="Close" onclick="reset()">&times;</button> 
	      </div>
	      
	      <!-- Modal body -->
	      <div class="modal-body">
	        <form class="review-form" id="review-form" name="addReviewFrm" enctype="multipart/form-data">
	        	<input type="hidden" id="reviewno" name="reviewno" />
	        	<input type="hidden" id="optionno" name="optionno" />
				<div class="form-group">
		          <label for="review-title">Title</label>
		          <input type="text" name="title" class="infoData" />
		          <span class="error">필수입력</span>
		        </div>
		        <div class="form-group">
		          <label for="review-rating">Rating</label>
		        <div class="star-rating">
	              <input type="hidden" name="score" id="score" value="" />
	              <span class="star" data-value="1">★</span>
	              <span class="star" data-value="2">★</span>
	              <span class="star" data-value="3">★</span>
	              <span class="star" data-value="4">★</span>
	              <span class="star" data-value="5">★</span>
	            </div>
		          <span class="error">필수입력</span>
		        </div>
		        <div class="form-group">
		          <label for="review-message">Review</label>
		          <textarea
		            id="review-message"
		            name="content"
		            rows="4"
		            placeholder="Write your review here"
		            class="infoData"
		          ></textarea>
		          <span class="error">필수입력</span>
		        </div>
		        <div class="form-group">
		          <label for="review-file">attachment</label>
		          <input type="file" name="img" id="img_file">
		        </div> 
		        <div class="imgPreview"><img id="imgPreview" /></div>
		        <div class="button-wrapper">
		          <button type="button" name="addReview">Write</button>
		          <!-- data-dismiss 추가 Cancel 버튼 누르면 모달 닫힘 -->
		          <button type="button" id="modal_cancel" data-dismiss="modal">Cancel</button>
		        </div>
		      </form>
	      </div>
	    </div>
	  </div>
	</div>
