(function() {
    'use strict';
    angular
        .module('organiseItApp')
        .factory('Note', Note);

    Note.$inject = ['$resource'];

    function Note ($resource) {
        var resourceUrl =  'api/notes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
