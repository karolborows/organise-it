(function() {
    'use strict';

    angular
        .module('organiseItApp')
        .controller('NoteDialogController', NoteDialogController);

    NoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Note', 'User', 'Principal'];

    function NoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Note, User, Principal) {
        var vm = this;

        vm.note = entity;
        vm.clear = clear;
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
            console.log(vm.userLogin);
            vm.isSaving = true;
            if (vm.note.id !== null) {
                Note.update(vm.note, onSaveSuccess, onSaveError);
            } else {
                vm.note.owner = vm.userLogin;
                Note.save(vm.note, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('organiseItApp:noteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
