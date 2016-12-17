(function() {
    'use strict';

    angular
        .module('organiseItApp')
        .controller('NoteDetailController', NoteDetailController);

    NoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Note', 'User'];

    function NoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Note, User) {
        var vm = this;

        vm.note = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('organiseItApp:noteUpdate', function(event, result) {
            vm.note = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
