import $ from "jquery";

$("body").on("click", "[data-destroy-btn]", function (event) {
    event.preventDefault()

    const button = $(this);
    const who = button.data("destroy-btn").trim();
    let message = (who === ""  ? "선택한" : who) + " 항목을 삭제하시겠습니까?";
    if ( ! confirm(message)) {
        return false;
    }
    const form = $("<form>");
    form.attr("action", button.attr("href"));
    form.attr("method", "post")
    form.append("<input type='hidden' name='_method' value='DELETE'>")
    $("body").append(form);
    form.submit()

    return false;
})