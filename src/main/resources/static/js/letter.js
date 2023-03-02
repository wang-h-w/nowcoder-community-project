$(function(){
	$("#sendBtn").click(send_letter);
	$(".close-message").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");
	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post(
		CONTEXT_PATH + "/letter/send",
		{"toName": toName, "content": content},
		function (ret) {
			if (ret.code === 0) {
				$("#hintBody").text("私信发送成功！");
			} else {
				$("#hintBody").text(data.msg);
			}
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				location.reload();
			}, 2000);
		},
		'json'
	)
}

function delete_msg() {
	var id = $("#close-id").text()
	$.post(
		CONTEXT_PATH + "/letter/delete",
		{"id": id},
		function (ret) {
			location.reload();
		},
		'json'
	)
}