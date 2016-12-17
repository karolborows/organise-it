(function() {
    'use strict';

    angular
        .module('organiseItApp')
        .controller('NoteController', NoteController);

    NoteController.$inject = ['$scope', '$state', 'Note'];

    function NoteController ($scope, $state, Note) {
        var vm = this;

        vm.notes = [];

        loadAll();

        function loadAll() {
            Note.query(function(result) {
                vm.notes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
