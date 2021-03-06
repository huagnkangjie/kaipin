<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
 <%@ include file="/WEB-INF/application/certificate/common/regHeader.jsp" %>
	
	<script type="text/javascript" src="${BASE_PATH }${_PUBLIC_ }/web/common/js/jquery-1.11.1.min.js"></script>
	
	<!-- uploadify -->
	<link rel="stylesheet" type="text/css" href="${STATIC_SCH }/js/uploadify/Huploadify.css"/>
	<script type="text/javascript" src="${STATIC_SCH }/js/uploadify/jquery.Huploadify.js"></script>
	
	
	
	<script type="text/javascript">
		var r_path='<%=basePath%>';
	</script>


	<script type="text/javascript">
	
	
	
		var userId;
		var companyId;
		var flag =false;
		
		$(function(){
			
			userId = '${userId }';
			companyId = '${orgId }';
			$("#userId").val(userId);
			$("#companyId").val(companyId);
			$("#opering").val("-1");
			
			$("#entName").blur(function (){
				flag = false;
				if(checkValLength("entName")){
					entNameValidata("entName");
				}
			});
			
			$("#entSimpleName").blur(function (){
				flag = false;
				if(checkValLength("entSimpleName")){
					entNameValidata("entSimpleName");
				}
			});
			
			
			 /*判断提交值的长度*/
			function checkValLength(objId){
				var obj = $("#" + objId).val();
				if($.trim(obj).length == 0) {
					$("#" + objId).removeClass("error-input");
					$("#" + objId + "Tip").hide();
					flag = false;
					return flag;
				}else{
					flag = true;
					return flag;
				}
				//setTimeout(checkVal(objId),20);
				//return flag;
			}
			function checkVal(objId){
				var obj = $("#" + objId).val();
				if($.trim(obj).length == 0) {
					$("#" + objId).removeClass("error-input");
					$("#" + objId + "Tip").hide();
					flag = false;
					return flag;
				}else{
					flag = true;
					return flag;
				}
			}
			
			
			
			$("#reset-btn").click(function(){
				if(confirm("资质认证通过后，发布的宣讲会、宣传视频、职位等信息将会被优先推荐")){ 
					location.href= r_path + "/basicConctroller/init.do";
// 					location.href= r_path + "/loginController/index.do";
				} 
			});			
			
			
			
		});
		/* 返回登录 */
		function backIndex(){
			window.location.href="<%=path%>/loginController/index.do";
		}
		
		/* 企业名字校验 */
		function entNameValidata (objID){
			var entNameVal = $("#" + objID).val();
			if($.trim(entNameVal).length != 0){
				if(entNameVal.length <= 30){
					$("#"+ objID).removeClass("error-input");
					$("#"+ objID +"Tip").hide();
					return true;
				}else{
					$("#" + objID).addClass("error-input");
					$("#" + objID +"Tip").show();
					return false;
				}
			}else{
				$("#" + objID).addClass("error-input");
				$("#" + objID +"Tip").show();
				return false;
			}
		}
		
		function saveBtn(){
			//if(entNameValidata("entName")){
				//if(entNameValidata("entSimpleName")){
					var oper = $("#opering").val();
					if(oper == 'ing'){
						alert("正在上传中，请上传完毕后再保存");
						return;
					}
					var yyzzVal = $("#yyzzVal").val();
					if(yyzzVal.length == 0){
						alert("请上传营业执照");
						return;
					}
					$("#save").hide();
					$("#saveing").show();
					doSave();
				//}
			//}
		}
		
		function doSave(){
			$.ajax({                
				cache: false,    
				async: true, 
				type: "POST",                
				url:  '<%=path%>/certificate/certificateEnt',                
				data : $('#certificateForm').serialize(),            
				error: function(request) { 
				},
				beforeSend : function(request){
					$("#save").hide();
					$("#saveing").show();
				},
				complete: function(data) { 
					var dataStr = data.responseText;
					var datas = eval('('+dataStr+')');
					if(datas.success){
						location.href="<%=path%>/certificate/checkPageEnt?uploadAgin=regedit&userId="+userId+"&orgId="+companyId;
					}else{
						alert("上传失败，请重新上传");
						$("#save").show();
						$("#saveing").hide();
					}
				}
			});
		}
		
	</script>
  </head>
  
  <body>
		<!--header start-->
		<div class="lm-header-container">
			<div class="header">
				<h1>
					<a href="javascript:void(0)" class="logo" style="background-image: url('${STATIC_SCH }/images/reg-logs.png');"></a>
					<span class="tips-txt"> - 创建您的开频账户</span>
				</h1>
			</div>
		</div>
		<!--header end-->
		
		<div class="sessionRegister">
			<div class="flowsheet">
				<dl class="sheet1">
					<dt>
						<span class="ll-bg ll-bg-three">1</span>
					</dt>
					<dd class="lq-color-one">创建账号</dd>
				</dl>
				<dl class="sheet2">
					<dt>
						<span class="ll-bg ll-bg-three">2</span>
					</dt>
					<dd class="lq-color-one">选择身份</dd>
				</dl>
				<dl class="sheet3">
					<dt>
						<span class="ll-bg ll-bg-three">3</span>
					</dt>
					<dd class="lq-color-one">填基本信息</dd>
				</dl>
				<dl class="sheet4">
					<dt>
						<span class="ll-bg ll-bg-two">4</span>
					</dt>
					<dd class="lq-color-two">提交认证</dd>
				</dl>
				<div class="line line1 line-color-sure""></div>
				<div class="line line2 line-color-sure""></div>
				<div class="line line3 line-color-sure""></div>
			</div>
			
			<div class="submit-reg-infos">
				<div class="sri-contents">
					<form id="certificateForm" name="certificateForm" action="<%=path%>/certificate/certificateEnt" method="post">
						<div class="basic-informations upload-files">
							<div class="upf-areas">
								<div class="upf-tlt">
									上传营业执照照片或者扫描件
								</div>
								<div class="notice">
									<p>请上传清晰图片，尺寸不超过1600像素*2000像素，大小不超过2M</p>
									<p>格式为支持jpg、png</p>
								</div>
								<div class="load-upf">
									<a href="javascript:void(0)" class="upf-btn select-up-img-btn" data-tag="yyzz" id="yyzzSelect">上传营业执照
										<img id="yyzzIng" src="${STATIC_SCH }/images/loading.gif" style="display:none;"/>
									</a>
									<div class="file-prv" style="display: none;" id="yyzzShowDiv">
										<img  src="${STATIC_SCH }/images/qiye_zhizhao_b.png" id="yyzzShow" onclick="imgbox.show(this.src)"alt="预览文件"/>
									</div>
								</div>
							</div>
							
							<div class="exam-preview">
								<p>参考实例</p>
								<div class="file-prv">
									<img  src="${STATIC_SCH }/images/qiye_zhizhao_b.png"  onclick="imgbox.show(this.src)"alt="预览文件"/>
								</div>
							</div>
							<span class="clear"></span>
						</div>
						
						
						<div class="basic-informations upload-files">
							<div class="upf-areas">
								<div class="upf-tlt">
									<p>上传联系人身份证正面照片、工作证照片（选填）、名片照片（选填）</p>
									<p>（选填项上传有助于审核速度）</p>
								</div>
								<div class="notice">
									<p>请上传清晰图片，尺寸不超过1600像素*2000像素，大小不超过2M</p>
									<p>格式为支持jpg、png</p>
								</div>
								<div class="upf-file-lists">
									<div class="load-upf">
										<a href="javascript:void(0)" class="upf-btn select-up-img-btn" data-tag="sfz" id="sfzSelect">上传身份证
											<img id="sfzIng" src="${STATIC_SCH }/images/loading.gif" style="display:none;"/>
										</a>
										<div class="file-prv" id="sfzShowDiv" style="display:none;">
											<img  src="${STATIC_SCH }/images/qiye_shengfen_b.jpg" id="sfzShow" onclick="imgbox.show(this.src)"alt="预览文件"/>
										</div>
									</div>
									<div class="load-upf">
										<a href="javascript:void(0)" class="upf-btn select-up-img-btn" data-tag="gzz" id="gzzSelect">上传工作证
											<img id="gzzIng" src="${STATIC_SCH }/images/loading.gif" style="display:none;"/>
										</a>
										<div class="file-prv" id="gzzShowDiv" style="display:none;">
											<img  src="${STATIC_SCH }/images/qiye_gongzuozheng_b.jpg" id="gzzShow"  onclick="imgbox.show(this.src)"alt="预览文件"/>
										</div>
									</div>
									<div class="load-upf">
										<a href="javascript:void(0)" class="upf-btn select-up-img-btn" data-tag="mp" id="mpSelect">上传名片
											<img id="mpIng" src="${STATIC_SCH }/images/loading.gif" style="display:none;"/>
										</a>
										<div class="file-prv" id="mpShowDiv" style="display:none;">
											<img  src="${STATIC_SCH }/images/qiye_mingpian_b.jpg" id="mpShow" onclick="imgbox.show(this.src)"alt="预览文件"/>
										</div>
									</div>
									<span class="clear"></span>
								</div>
								
							</div>
							
							<div class="exam-preview exam-preview-lists">
								<p>参考实例</p>
								<div class="file-prv">
									<img  src="${STATIC_SCH }/images/qiye_shengfen_b.jpg"  onclick="imgbox.show(this.src)"alt="预览文件"/>
								</div>
								<div class="file-prv">
									<img  src="${STATIC_SCH }/images/qiye_gongzuozheng_b.jpg"  onclick="imgbox.show(this.src)"alt="预览文件"/>
								</div>
								<div class="file-prv">
									<img  src="${STATIC_SCH }/images/qiye_mingpian_b.jpg"  onclick="imgbox.show(this.src)"alt="预览文件"/>
								</div>
								
							</div>
							<span class="clear"></span>
						</div>
						
<!-- 						营业执照 yyzz -->
<!-- 						身份证 sfz -->
<!-- 						工作证 gzz -->
<!-- 						名片 mp -->
<!-- 						正在上传 ing -->
						<input type="hidden" id="oper" value="-1"/>
						<input type="hidden" id="opering" value="-1"/>
						<input type="hidden" id="uploadAgin" name="uploadAgin" value="${uploadAgin }"/>
						<input type="hidden" id="jyxkzVal" name="jyxkz" value=""/>
						<input type="hidden" id="yyzzVal" name="yyzz" value=""/>
						<input type="hidden" id="sfzVal" name="sfz" value=""/>
						<input type="hidden" id="gzzVal" name="gzz" value=""/>
						<input type="hidden" id="mpVal" name="mp" value=""/>
						<input type="hidden" id="companyId" name="compangy" value=""/>
						<input type="hidden" id="userId" name="userId" value="${userId }"/>
						
						<div class="submit-box nosel-color">
							<a href="javascript:void(0)" class="reset-btn" id="next-1" onclick="next();">返回</a>
							<a href="javascript:void(0)" class="sub-btn" id="save" onclick="saveBtn();">提交</a>
							<a href="javascript:void(0)" class="sub-btn" id="saveing" style="display:none;">
								提交...<img src="${STATIC_SCH }/images/loading.gif"/>
							</a>
						</div>
						
					</form>
				</div>
			</div>
		</div>
		<!--注册流程 end-->
		
		<div id="uploadImg" style="display:none;"></div>
		
		<!-- footer_reg start-->
		<%@ include file="/WEB-INF/application/main/footer.jsp" %>
		<!-- footer_reg end-->
		
		
		
		
<%-- 		<script type="text/javascript" src="<%=path%>/js/base.js?v.<%=System.currentTimeMillis()%>"></script> --%>
		<script type="text/javascript" src="${STATIC_SCH }/js/regedit/regedit.js?v.<%=System.currentTimeMillis()%>"></script>
		<script type="text/javascript" src="${STATIC_SCH }/js/imgZoom.js"></script>
		<script type="text/javascript" src="${STATIC_SCH }/js/regedit/fiexdFoot.js"></script>
		<script type="text/javascript">
			$(function(){
				
				//切换选中
				$("#lable-select").find("label").click(function(){
					$(this).addClass("bg-img").siblings().removeClass("bg-img");
				});
				
				//开关
				$("#favor-tips").click(function(){
					$('.mwui-switch-btn').trigger("click");
				});
				
	           $('.mwui-switch-btn').on("click", function(){ 
	                var btn = $(this).find("span");
	                btn.toggleClass('off'); 
	                if(btn.attr("class") == 'off') { 
	                    $(this).find("input").val("0");
	                } else { 
	                    $(this).find("input").val("1");
	                }  
	                return false;
		        });
				
				
				 // 判断浏览器是否支持 placeholder
				if(!placeholderSupport()){  
					    $('[placeholder]').focus(function() {
					        var input = $(this);
					        if (input.val() == input.attr('placeholder')) {
					            input.val('');
					            input.removeClass('placeholder');
					        }
					    }).blur(function() {
					        var input = $(this);
					        if (input.val() == '' || input.val() == input.attr('placeholder')) {
					            input.addClass('placeholder');
					            input.val(input.attr('placeholder'));
					        }
					    }).blur();
					};
				function placeholderSupport() {
				    return 'placeholder' in document.createElement('input');
				};
				
			});
			
			function next(){
				//推荐关注
				location.href="<%=path%>/company/home";
			}
		</script>
		
		<!-- 附件上传 -->
		<script type="text/javascript">
					var upImg ;
					var oper = "0";
					var entId ;
					$(function (){
						entId = '${orgId }';
						upImg = $('#uploadImg').Huploadify({
							auto:false,
							fileTypeExts:'*.jpg;*.png;',
							multi:false,
							auto:true,
							formData:{key:123456,key2:'vvvv',entId:entId},
							fileSizeLimit:2048,
							removeCompleted:false,
							removeTimeout: 50,//上传完成后进度条的消失时间
							showUploadedPercent:true,
							showUploadedSize:true,
							removeTimeout:9999999,
							uploader:'<%=path%>/annex/uploadImg.do?oper=regedit&type=12&orgId=' + entId,
							onUploadStart:function(file){
								$(".showDiv").hide();
								var oper = $("#oper").val();
								$("#" + oper + "Ing").show()
								$("#opering").val("ing");
							},
							onInit:function(obj){
							},
							onSelect:function(file){
							},
							onUploadComplete:function(file,data,response){
								cancle();
								var datas = data.split(",");
								var path = datas[0];
								var oper = $("#oper").val();
								$("#"+oper+"Ing").hide();//正在加载
								$("#"+oper+"Show").attr("src",path);//显示
								$("#"+oper+"ShowDiv").show();//显示
								$("#"+oper+"Val").val(path);//显示
								$("#oper").val("-1");
								$("#opering").val("-1");
							}
						});
						
						$(".select-up-img-btn").click(function(){
							var type = $(this).data("tag");
							//var oper = $("#opering").val();
							checkStatus(type);
						});
						
						function checkStatus(oper){
							var opering = $("#opering").val();
							if(opering == 'ing'){
								alert("正在上传，请上传完毕后再继续");
								return;
							}
							$("#oper").val(oper);
							$("#uploadImg a").trigger("click");
						}
						
						
						function cancle(){
							upImg.cancel('*');
						}
					});	
				</script>
	</body>
</html>
