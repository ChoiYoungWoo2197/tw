import $ from "jquery";
import dropzone from "dropzone";
import 'dropzone/dist/dropzone.css';

dropzone.autoDiscover = false; //초기화

const defaultOptions = {
    addRemoveLinks : true,
    dictRemoveFileConfirmation : "업로드한 파일을 삭제하시겠습니까?",
    dictRemoveFile : "삭제",
    dictCancelUpload : "업로드중..",
    uploadMultiple : true,
    parallelUploads : 10
}

$(".dropzone").each(function (index, item) {
    const id = $(this).attr("id");
    const url = $(this).closest("form").attr("action");
    const method = $(this).closest("form").attr("method");
    const autoProcessQueue = $(this).closest("form").find("input[name='asynchronous']").val();

    $("#"+id).dropzone({...defaultOptions,
        url : url,
        method : method,
        autoProcessQueue : autoProcessQueue=="true"?true:false,

        init : function () {
            const thisDropzone = this;

            thisDropzone.on("removedfile", function (file) {
                $.ajax({
                    url: "/admin/operating-cycle/file-delete",
                    type: "POST",
                    data: {"fileName" : file.name},
                })
            })

            thisDropzone.on("addedfile", function (file) {
                if(thisDropzone.files.length) {
                    for (var i=0; i<thisDropzone.files.length-1; i++) {
                        if (thisDropzone.files[i].name === file.name) {
                            alert("중복된 파일입니다.");
                            thisDropzone.removeFile(file);
                        }
                    }
                }
            })

            if(! thisDropzone.options.autoProcessQueue) {
                $("input[type='file']").on("change", function (e) {
                    thisDropzone.removeAllFiles();

                    for(var i=0; i<this.files.length; i++) {
                        thisDropzone.addFile(this.files[i]);
                    }
                });
            }
        }
    })
})

