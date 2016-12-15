(function() {
    'use strict';

    angular
        .module('organiseItApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'nemLogging',
            'ui-leaflet',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'mwl.calendar', 'ui.bootstrap', 'ui.calendar'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
