import './basePath.js';
import 'ckeditor4';

function loadEditor() {
    CKEDITOR.replace( 'description', {
        customConfig: 'customConfig.js'
    } );
}

window.loadEditor = loadEditor;
