$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// 发送AJAX请求之前，需要将CSRF令牌设置到请求的消息头中
	// var token = $("meta[name='_csrf']").attr("content");
	// var header = $("meta[name='_csrf_header']").attr("content");
	// $(document).ajaxSend(function (e, xhr, options) {
	// 	xhr.setRequestHeader(header, token);
	// });

	var title = $("#recipient-name").val()
	var content = $("#message-text").val()

	$.post(
		CONTEXT_PATH + "/discuss/add",
		{title: title, content : content},
		function (res) {
			$("#hintBody").text(res.msg);
			// 显示提示框
			$("#hintModal").modal("show");
			// 2秒后自动隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 刷新页面
				if (res.code === 0) {
					window.location.reload();
				}
			}, 2000);
		},
		'json'
	)
}