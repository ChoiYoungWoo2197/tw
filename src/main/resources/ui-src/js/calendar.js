import $ from 'jquery'
import { Calendar } from '@fullcalendar/core';
import interactionPlugin from '@fullcalendar/interaction';
import dayGridPlugin from '@fullcalendar/daygrid';
import moment from "moment";
import '../css/calendar-style.css'

document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    if(calendarEl == null) {
        return false;
    }

    var calendar = new Calendar(calendarEl, {
        selectable: true, //셀 선택 여부
        businessHours: true, //주말, 평일 구분
        defaultView: 'dayGridMonth',
        editable: true, //이벤트 drag, resize 여부
        plugins: [ interactionPlugin, dayGridPlugin ],
        eventSources: [{
            events : function(info, successCallback) {
                $.ajax({
                    url : '/admin/calendar-event/index',
                    type : 'GET',
                    data : {"calendarId": $('input[name=calendarId]').val()},
                    dataType : 'json',
                    async : false,
                    success : function (data) {
                        //console.log(data);
                        var events = [];
                        $(data).each(function () {
                            events.push({
                                id : $(this).attr("id"),
                                /*groupId : $(this).attr("calendarId"),*/
                                title : $(this).attr("eventHeader"),
                                start : $(this).attr("eventStart"),
                                end : $(this).attr("eventEnd")
                            });
                        });
                        successCallback(events);
                    },
                    error:function(request,status,error){
                        console.log("error");
                    }
                });
            },
            textColor: 'white'
        }],

        eventClick: function(info) {
            //console.log(info.event.title + ' : ' + info.event.start + ' : ' + info.event.end);
            $('h4[name=myModalLabel]').text(moment(info.event.start).format('YYYY-MM-DD'));
            $('input[name=myModalContent]').val(info.event.title);

            $('.modal-footer').children('.btn-primary').hide();
            $('button[name=update]').show();
            $('button[name=destroy]').show();
            $('div[name=myModal]').modal('show');
        },

        //셀, 드래그 셀 했을 경우
        select : function (info) {
            //console.log(info);

            $('h4[name=myModalLabel]').text(moment(info.start).format('YYYY-MM-DD'));
            $('input[name=start]').val(moment(info.start).format('YYYY-MM-DD'));
            $('input[name=end]').val(moment(info.end).format('YYYY-MM-DD'));

            $('.modal-footer').children('.btn-primary').hide();
            $('button[name=create]').show();
            $('div[name=myModal]').modal('show');
        },

        eventDrop: function(info) {
            // console.log(info);
            $('h4[name=myModalLabel]').text(moment(info.event.start).format('YYYY-MM-DD'));
            $('input[name=myModalContent]').val(info.event.title);

            $('.modal-footer').children('.btn-primary').hide();
            $('button[name=update]').show();
            $('button[name=destroy]').show();
            $('div[name=myModal]').modal('show');

        }

    });

    /*
    * 생성 front
    */
    $('button[name=create]').on('click', function (e) {
        // console.log($('select[name=selectCalendar]').val());
        var newEvent = {
            "title": $('input[name=myModalContent]').val(),
            "start":  $('input[name=start]').val(),
            "end":  $('input[name=end]').val(),
            "groupId" : $('input[name=calendarId]').val(),
            "textColor" : "white"
        };

        calendar.addEvent(newEvent);
        $('div[name=myModal]').modal('hide');

        $('#calendar').trigger('create.event',newEvent);
    })

    /*
    * 생성 트리거
    * 트리거에서 백단 로직 구현.
    */
    $('#calendar').on('create.event', function (event,info) {
        //console.log(event,info);

        $.ajax({
            url		: "/admin/calendar-event",
            type		: "post",
            contentType: 'application/json; charset=utf-8',
            data		: JSON.stringify(
                {
                    "calendarId": info.groupId,
                    "eventStart":info.start,
                    "eventEnd" : info.end,
                    "eventHeader": info.title
                }
            ),
            async : false,
            success	: function(data){
                //새로운 이벤트를 추가할때만 새로고침 한다.
                location.reload();
                console.log('success');
            },
            error : function(){
                console.log('error');
            }
        });
    })

    /*
    * 수정 front
    */
    $('button[name=update]').on('click', function (e) {
        var events =calendar.getEvents();

        console.log(events)
        var eventExist = events.find(i => moment(i.start).format('YYYY-MM-DD') === $('h4[name=myModalLabel]').text());
        eventExist.setProp("title",$('input[name=myModalContent]').val());
        $('div[name=myModal]').modal('hide');

        $('#calendar').trigger('update.event',eventExist);
    })

    /*
    * 수정 트리거
    * 트리거에서 백단 로직 구현.
    * 아작스에서 PUT, DELETE 처리 방법 (https://wondongho.tistory.com/78 참고)
    */
    $('#calendar').on('update.event', function (event,info) {
        // console.log(event,info);
        $.ajax({
            url		: "/admin/calendar-event",
            type		: "put",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(
                {
                    "id" : info.id,
                    "eventStart" : info.start,
                    "eventEnd" : info.end,
                    "eventHeader" : info.title,
                    "calendarId" : info.groupId
                    // "groupId" : info.groupId
                }
            ),
            async : false,
            success	: function(data){
                calendar.refetchEvents();
                console.log("success");
            },
            error:function(request,status,error){
                console.log("error");
            }
        });
    })

    /*
    * 삭제 front
    */
    $('button[name=destroy]').on('click', function (e) {
        var events =calendar.getEvents();
        var eventExist = events.find(i => moment(i.start).format('YYYY-MM-DD') === $('h4[name=myModalLabel]').text());
        eventExist.remove();
        $('div[name=myModal]').modal('hide');

        $('#calendar').trigger('destroy.event',eventExist);
    })

    /*
    * 삭제 트리거
    * 트리거에서 백단 로직 구현.
    * 아작스에서 PUT, DELETE 처리 방법 (https://wondongho.tistory.com/78 참고)
    */
    $('#calendar').on('destroy.event', function (event,info) {
        // console.log(event,info);

        $.ajax({
            url		: "/admin/calendar-event",
            type	: "delete",
            contentType: 'application/json; charset=utf-8',
            data : JSON.stringify({
                "id" : info.id
            }),
            async : false,
            success	: function(data){
                calendar.refetchEvents();
                console.log("success");
            },
            error : function(){
               console.log("error");
            }
        });
    })

    $('div[name=myModal]').on('hidden.bs.modal', function (e) {
        $('input[name=myModalContent]').val('');
        $('.modal-footer').children('.btn-primary').hide();
    })

    calendar.render();
});



