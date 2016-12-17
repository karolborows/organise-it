(function() {
    'use strict';
    angular
        .module('organiseItApp')
        .factory('CalendarEvent', CalendarEvent);

    CalendarEvent.$inject = ['$resource', 'DateUtils'];

    function CalendarEvent ($resource, DateUtils) {
        var resourceUrl =  'api/calendar-events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.start = DateUtils.convertDateTimeFromServer(data.start);
                        data.end = DateUtils.convertDateTimeFromServer(data.end);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
