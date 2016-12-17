(function() {
    'use strict';

    angular
        .module('organiseItApp')
        .controller('CalendarEventDialogController', CalendarEventDialogController);

    CalendarEventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CalendarEvent', 'User', 'Principal'];

    function CalendarEventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CalendarEvent, User, Principal) {
        var vm = this;

        vm.calendarEvent = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.userLogin = {};

        init();

        function init(){
            Principal.identity().then(function(principal){
                vm.userLogin = principal;
            })
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.calendarEvent.id !== null) {
                CalendarEvent.update(vm.calendarEvent, onSaveSuccess, onSaveError);
            } else {
                vm.calendarEvent.owner = vm.userLogin;
                CalendarEvent.save(vm.calendarEvent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('organiseItApp:calendarEventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.start = false;
        vm.datePickerOpenStatus.end = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
