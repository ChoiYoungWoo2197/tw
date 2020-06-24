import $ from "jquery";
import flatpickr from "flatpickr";
import 'flatpickr/dist/themes/airbnb.css';
import { Korean } from "flatpickr/dist/l10n/ko.js"

const defaultConfigs = {
    locale : Korean,
    enableTime : false,
    dateFormat : "Y-m-d"
}

flatpickr(".flatpickr", defaultConfigs);

function flatpickrCompare(object) {
    const startFlatpickr = $(object.start).flatpickr({...defaultConfigs,
        defaultDate : $(object.start).val(),
        maxDate : $(object.end).val(),
        onChange: function (selectedDates, dateStr) {
            endFlatpickr.set('minDate', dateStr);
        },
        altInput : true,
        altFormat : "Y년 F J"
    });

    const endFlatpickr = $(object.end).flatpickr({...defaultConfigs,
        defaultDate : $(object.end).val(),
        minDate : $(object.start).val(),
        onChange: function(selectedDates, dateStr) {
            startFlatpickr.set('maxDate', dateStr);
        },
        altInput : true,
        altFormat : "Y년 F J"
    });
}

window.flatpickrCompare = flatpickrCompare;


