(function() {
    'use strict';

    angular
        .module('organiseItApp')
        .controller('CalendarEventDetailController', CalendarEventDetailController);

    CalendarEventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CalendarEvent', 'User'];

    function CalendarEventDetailController($scope, $rootScope, $stateParams, previousState, entity, CalendarEvent, User) {
        var vm = this;

        vm.calendarEvent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('organiseItApp:calendarEventUpdate', function(event, result) {
            vm.calendarEvent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
